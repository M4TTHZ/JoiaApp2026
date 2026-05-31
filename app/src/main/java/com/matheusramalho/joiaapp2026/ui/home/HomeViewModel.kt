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
import java.time.ZoneId

class HomeViewModel(private val repository: GameRepository) : ViewModel() {

    private var todosOsJogos: List<JogoResponse> = emptyList()

    private val _jogosAoVivo = MutableLiveData<Resource<List<JogoResponse>>>()
    val jogosAoVivo: LiveData<Resource<List<JogoResponse>>> = _jogosAoVivo

    private val _proximosJogos = MutableLiveData<List<JogoResponse>>()
    val proximosJogos: LiveData<List<JogoResponse>> = _proximosJogos

    private val _modalidades = MutableLiveData<List<ModalidadeResponse>>()
    val modalidades: LiveData<List<ModalidadeResponse>> = _modalidades

    var filtroModalidadeId: String? = null
    var filtroCursoId: String?      = null
    var filtroDia: String?          = null

    companion object {
        private const val POLLING_MS = 30_000L
        // Status da API real
        const val STATUS_EM_ANDAMENTO = "EM_ANDAMENTO"
        const val STATUS_AGENDADO     = "AGENDADO"
        const val STATUS_FINALIZADO   = "FINALIZADO"
    }

    fun init() {
        viewModelScope.launch {
            when (val r = repository.getModalidades()) {
                is Resource.Success -> _modalidades.value = r.data
                else -> Unit
            }
        }
        startPolling()
    }

    fun startPolling() {
        viewModelScope.launch {
            while (true) {
                fetchJogos()
                delay(POLLING_MS)
            }
        }
    }

    fun refresh() { viewModelScope.launch { fetchJogos() } }

    private suspend fun fetchJogos() {
        if (_jogosAoVivo.value == null) _jogosAoVivo.value = Resource.Loading
        when (val r = repository.getJogos()) {
            is Resource.Success -> {
                todosOsJogos = r.data
                _jogosAoVivo.value = Resource.Success(
                    r.data.filter { it.status.uppercase() == STATUS_EM_ANDAMENTO }
                )
                aplicarFiltros()
            }
            is Resource.Error -> _jogosAoVivo.value = Resource.Error(r.message)
            else -> Unit
        }
    }

    fun aplicarFiltros() {
        val BR    = ZoneId.of("America/Cuiaba")
        val agora = ZonedDateTime.now(BR)

        val filtrado = todosOsJogos
            .filter { it.status.uppercase() == STATUS_AGENDADO }
            .filter { jogo -> filtroModalidadeId == null || jogo.modalidadeId == filtroModalidadeId }
            .filter { jogo ->
                filtroCursoId == null ||
                jogo.mandante?.cursoId == filtroCursoId ||
                jogo.visitante?.cursoId == filtroCursoId
            }
            .filter { jogo ->
                if (filtroDia == null) return@filter true
                try {
                    val dataJogo = ZonedDateTime.parse(jogo.iniciaEm).withZoneSameInstant(BR).toLocalDate()
                    when (filtroDia) {
                        "hoje"   -> dataJogo == agora.toLocalDate()
                        "amanha" -> dataJogo == agora.toLocalDate().plusDays(1)
                        else     -> true
                    }
                } catch (_: Exception) { true }
            }
            .sortedBy { it.iniciaEm }

        _proximosJogos.value = filtrado
    }
}
