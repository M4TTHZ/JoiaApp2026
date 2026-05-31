package com.matheusramalho.joiaapp2026.ui.ranking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matheusramalho.joiaapp2026.data.model.ModalidadeResponse
import com.matheusramalho.joiaapp2026.data.model.RankingGeralItem
import com.matheusramalho.joiaapp2026.data.model.RankingModalidadeResponse
import com.matheusramalho.joiaapp2026.data.repository.GameRepository
import com.matheusramalho.joiaapp2026.data.repository.RankingRepository
import com.matheusramalho.joiaapp2026.utils.Resource
import kotlinx.coroutines.launch

class RankingViewModel(
    private val rankingRepo: RankingRepository,
    private val gameRepo: GameRepository
) : ViewModel() {

    var abaAtiva: String = "geral"
    var modalidadeSelecionada: ModalidadeResponse? = null

    private val _rankingGeral = MutableLiveData<Resource<List<RankingGeralItem>>>()
    val rankingGeral: LiveData<Resource<List<RankingGeralItem>>> = _rankingGeral

    private val _rankingModalidade = MutableLiveData<Resource<RankingModalidadeResponse>>()
    val rankingModalidade: LiveData<Resource<RankingModalidadeResponse>> = _rankingModalidade

    private val _modalidades = MutableLiveData<List<ModalidadeResponse>>()
    val modalidades: LiveData<List<ModalidadeResponse>> = _modalidades

    fun init() {
        viewModelScope.launch {
            when (val r = gameRepo.getModalidades()) {
                is Resource.Success -> _modalidades.value = r.data
                else -> Unit
            }
        }
        carregarRankingGeral()
    }

    fun carregarRankingGeral() {
        viewModelScope.launch {
            _rankingGeral.value = Resource.Loading
            _rankingGeral.value = rankingRepo.getRankingGeral()
        }
    }

    fun carregarRankingModalidade(modalidadeId: String) {
        viewModelScope.launch {
            _rankingModalidade.value = Resource.Loading
            _rankingModalidade.value = rankingRepo.getRankingModalidade(modalidadeId)
        }
    }

    fun refresh() {
        if (abaAtiva == "geral") carregarRankingGeral()
        else modalidadeSelecionada?.let { carregarRankingModalidade(it.id) }
    }
}
