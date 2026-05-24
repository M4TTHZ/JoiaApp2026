package com.matheusramalho.joiaapp2026.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matheusramalho.joiaapp2026.data.model.JogoResponse
import com.matheusramalho.joiaapp2026.data.model.ModalidadeResponse
import com.matheusramalho.joiaapp2026.data.repository.GameRepository
import com.matheusramalho.joiaapp2026.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class HomeViewModel(private val repository: GameRepository) : ViewModel() {

    // --- Todos os jogos brutos da API ---
    private var todosOsJogos: List<JogoResponse> = emptyList()

    // --- Carrossel: só jogos AO_VIVO ---
    private val _jogosAoVivo = MutableLiveData<Resource<List<JogoResponse>>>()
    val jogosAoVivo: LiveData<Resource<List<JogoResponse>>> = _jogosAoVivo

    // --- Lista filtrada: AGENDADO + filtros aplicados ---
    private val _proximosJogos = MutableLiveData<List<JogoResponse>>()
    val proximosJogos: LiveData<List<JogoResponse>> = _proximosJogos

    // --- Modalidades para os chips de filtro ---
    private val _modalidades = MutableLiveData<List<ModalidadeResponse>>()
    val modalidades: LiveData<List<ModalidadeResponse>> = _modalidades

    // --- Filtros ativos ---
    var filtroModalidadeId: String? = null
    var filtroCursoId: String?      = null  // mandante ou visitante
    var filtroDia: String?          = null  // "hoje", "amanha" ou null

    companion object {
        private const val POLLING_MS = 30_000L
        const val STATUS_AO_VIVO   = "AO_VIVO"
        const val STATUS_AGENDADO  = "AGENDADO"
    }

    fun init() {
        carregarModalidades()
        startPolling()
    }

    private fun carregarModalidades() {
        viewModelScope.launch {
            when (val result = repository.getModalidades()) {
                is Resource.Success -> _modalidades.value = result.data
                else -> Unit
            }
        }
    }

    fun startPolling() {
        viewModelScope.launch {
            while (true) {
                fetchJogos()
                delay(POLLING_MS)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch { fetchJogos() }
    }

    private suspend fun fetchJogos() {
        if (_jogosAoVivo.value == null) {
            _jogosAoVivo.value = Resource.Loading
        }
        when (val result = repository.getJogos()) {
            is Resource.Success -> {
                todosOsJogos = result.data
                _jogosAoVivo.value = Resource.Success(
                    result.data.filter { it.status.uppercase() == STATUS_AO_VIVO }
                )
                aplicarFiltros()
            }
            is Resource.Error -> {
                _jogosAoVivo.value = Resource.Error(result.message)
            }
            else -> Unit
        }
    }

    // Chamado sempre que um chip de filtro muda
    fun aplicarFiltros() {
        val agora = ZonedDateTime.now()
        val amanha = agora.plusDays(1)

        val filtrado = todosOsJogos
            .filter { it.status.uppercase() == STATUS_AGENDADO }
            .filter { jogo ->
                // Filtro modalidade
                filtroModalidadeId == null || jogo.modalidadeId == filtroModalidadeId
            }
            .filter { jogo ->
                // Filtro curso (meu curso como mandante ou visitante)
                filtroCursoId == null ||
                        jogo.mandanteId == filtroCursoId ||
                        jogo.visitanteId == filtroCursoId
            }
            .filter { jogo ->
                // Filtro dia
                if (filtroDia == null) return@filter true
                try {
                    val dataJogo = ZonedDateTime.parse(jogo.iniciaEm)
                    when (filtroDia) {
                        "hoje"   -> dataJogo.toLocalDate() == agora.toLocalDate()
                        "amanha" -> dataJogo.toLocalDate() == amanha.toLocalDate()
                        else     -> true
                    }
                } catch (e: Exception) { true }
            }
            .sortedBy { it.iniciaEm } // ordena por horário

        _proximosJogos.value = filtrado
    }
}