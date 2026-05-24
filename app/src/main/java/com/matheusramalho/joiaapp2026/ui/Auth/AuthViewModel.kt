package com.matheusramalho.joiaapp2026.ui.Auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matheusramalho.joiaapp2026.data.model.CursoResponse
import com.matheusramalho.joiaapp2026.data.model.LoginResponse
import com.matheusramalho.joiaapp2026.data.repository.AuthRepository
import com.matheusramalho.joiaapp2026.utils.Resource
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<LoginResponse>>()
    val loginState: LiveData<Resource<LoginResponse>> = _loginState

    fun login(email: String, senha: String) {
        if (email.isBlank()) { _loginState.value = Resource.Error("Informe o e-mail"); return }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _loginState.value = Resource.Error("E-mail inválido"); return
        }
        if (senha.length < 6) { _loginState.value = Resource.Error("Senha muito curta"); return }
        viewModelScope.launch {
            _loginState.value = Resource.Loading
            _loginState.value = repository.login(email.trim(), senha)
        }
    }

    private val _cursosState = MutableLiveData<Resource<List<CursoResponse>>>()
    val cursosState: LiveData<Resource<List<CursoResponse>>> = _cursosState

    fun loadCursos() {
        viewModelScope.launch {
            _cursosState.value = Resource.Loading
            _cursosState.value = repository.getCursos()
        }
    }

    private val _registerState = MutableLiveData<Resource<LoginResponse>>()
    val registerState: LiveData<Resource<LoginResponse>> = _registerState

    fun register(
        nome: String, email: String, senha: String,
        confirma: String, cpf: String, telefone: String, cursoId: String
    ) {
        if (nome.isBlank())    { _registerState.value = Resource.Error("Informe seu nome"); return }
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _registerState.value = Resource.Error("E-mail inválido"); return
        }
        if (senha.length < 6)  { _registerState.value = Resource.Error("Senha deve ter ao menos 6 caracteres"); return }
        if (senha != confirma) { _registerState.value = Resource.Error("As senhas não coincidem"); return }
        if (cpf.replace(Regex("[^0-9]"), "").length != 11) {
            _registerState.value = Resource.Error("CPF inválido"); return
        }
        if (telefone.replace(Regex("[^0-9]"), "").length < 10) {
            _registerState.value = Resource.Error("Telefone inválido"); return
        }
        if (cursoId.isBlank()) { _registerState.value = Resource.Error("Selecione seu curso"); return }

        viewModelScope.launch {
            _registerState.value = Resource.Loading
            _registerState.value = repository.register(
                nome.trim(), email.trim(), senha,
                cpf.replace(Regex("[^0-9]"), ""),
                telefone.replace(Regex("[^0-9]"), ""),
                cursoId
            )
        }
    }
}
