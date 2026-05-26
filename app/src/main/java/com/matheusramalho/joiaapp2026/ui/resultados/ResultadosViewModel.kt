package com.matheusramalho.joiaapp2026.ui.resultados

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matheusramalho.joiaapp2026.data.model.JogoResponse
import com.matheusramalho.joiaapp2026.data.model.ModalidadeResponse
import com.matheusramalho.joiaapp2026.data.repository.GameRepository
import com.matheusramalho.joiaapp2026.utils.Resource
import kotlinx.coroutines.launch

class ResultadosViewModel(private val repository: GameRepository) : ViewModel() {

    private var todosEncerrados: List<JogoResponse> = emptyList()

    private val _resultados = MutableLiveData<Resource<List<JogoResponse>>>()
    val resultados: LiveData<Resource<List<JogoResponse>>> = _resultados

    private val _modalidades = MutableLiveData<List<ModalidadeResponse>>()
    val modalidades: LiveData<List<ModalidadeResponse>> = _modalidades

    var filtroModalidadeId: String? = null

    fun init() {
        viewModelScope.launch {
            // Carrega modalidades para os chips
            when (val r = repository.getModalidades()) {
                is Resource.Success -> _modalidades.value = r.data
                else -> Unit
            }
            carregarResultados()
        }
    }

    fun carregarResultados() {
        viewModelScope.launch {
            _resultados.value = Resource.Loading
            when (val r = repository.getJogos()) {
                is Resource.Success -> {
                    // Filtra apenas os encerrados e ordena do mais recente
                    todosEncerrados = r.data
                        .filter { it.status.uppercase() == "FINALIZADO" }
                        .sortedByDescending { it.iniciaEm }
                    aplicarFiltro()
                }
                is Resource.Error -> _resultados.value = Resource.Error(r.message)
                else -> Unit
            }
        }
    }

    fun aplicarFiltro() {
        val filtrado = if (filtroModalidadeId == null) {
            todosEncerrados
        } else {
            todosEncerrados.filter { it.modalidadeId == filtroModalidadeId }
        }
        _resultados.value = Resource.Success(filtrado)
    }

    fun refresh() = carregarResultados()
}