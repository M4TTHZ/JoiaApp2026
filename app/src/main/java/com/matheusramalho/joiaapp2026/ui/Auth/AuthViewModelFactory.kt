package com.matheusramalho.joiaapp2026.ui.Auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.matheusramalho.joiaapp2026.data.repository.AuthRepository

class AuthViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(AuthRepository(context)) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido")
    }
}