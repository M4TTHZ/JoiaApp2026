package com.matheusramalho.joiaapp2026.ui.perfil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matheusramalho.joiaapp2026.data.model.UserResponse
import com.matheusramalho.joiaapp2026.data.repository.AuthRepository
import com.matheusramalho.joiaapp2026.utils.Resource
import kotlinx.coroutines.launch

class PerfilViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _perfil = MutableLiveData<Resource<UserResponse>>()
    val perfil: LiveData<Resource<UserResponse>> = _perfil

    private val _updateState = MutableLiveData<Resource<String>>()
    val updateState: LiveData<Resource<String>> = _updateState

    fun loadPerfil() {
        viewModelScope.launch {
            _perfil.value = Resource.Loading
            _perfil.value = repo.getMe()
        }
    }

    fun updatePerfil(nome: String, cpf: String?, telefone: String?, cursoId: String?) {
        if (nome.isBlank()) { _updateState.value = Resource.Error("Nome não pode ser vazio"); return }
        viewModelScope.launch {
            _updateState.value = Resource.Loading
            when (val r = repo.updateMe(nome, cpf, telefone, cursoId)) {
                is Resource.Success -> {
                    _updateState.value = Resource.Success("Perfil atualizado!")
                    loadPerfil()
                }
                is Resource.Error -> _updateState.value = Resource.Error(r.message)
                else -> Unit
            }
        }
    }
}
