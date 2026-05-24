package com.matheusramalho.joiaapp2026.ui.ranking

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.matheusramalho.joiaapp2026.data.repository.GameRepository
import com.matheusramalho.joiaapp2026.data.repository.RankingRepository

class RankingViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        RankingViewModel(
            RankingRepository(context),
            GameRepository(context)
        ) as T
}