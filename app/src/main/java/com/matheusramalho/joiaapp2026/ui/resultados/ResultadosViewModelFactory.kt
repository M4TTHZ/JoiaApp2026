package com.matheusramalho.joiaapp2026.ui.resultados

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.matheusramalho.joiaapp2026.data.repository.GameRepository

class ResultadosViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ResultadosViewModel(GameRepository(context)) as T
}