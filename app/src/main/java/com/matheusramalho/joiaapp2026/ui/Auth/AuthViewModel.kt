package com.matheusramalho.joiaapp2026.ui.Auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matheusramalho.joiaapp2026.data.api.LoginResponse
import com.matheusramalho.joiaapp2026.data.repository.AuthRepository
import com.matheusramalho.joiaapp2026.utils.Resource
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<LoginResponse>>()
    val loginState: LiveData<Resource<LoginResponse>> = _loginState

    fun login(email: String, senha: String) {
        // Validação local antes de chamar a API
        if (email.isBlank()) {
            _loginState.value = Resource.Error("Informe o e-mail")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _loginState.value = Resource.Error("E-mail inválido")
            return
        }
        if (senha.length < 6) {
            _loginState.value = Resource.Error("Senha deve ter ao menos 6 caracteres")
            return
        }

        viewModelScope.launch {
            _loginState.value = Resource.Loading
            _loginState.value = repository.login(email.trim(), senha)
        }
    }
}