package com.matheusramalho.joiaapp2026.data.repository

import android.content.Context
import com.matheusramalho.joiaapp2026.data.api.RetrofitClient
import com.matheusramalho.joiaapp2026.data.model.RankingGeralItem
import com.matheusramalho.joiaapp2026.data.model.RankingModalidadeResponse
import com.matheusramalho.joiaapp2026.utils.Resource
import com.matheusramalho.joiaapp2026.utils.SessionManager

class RankingRepository(context: Context) {

    private val api   = RetrofitClient.rankingApi
    private val session = SessionManager(context)
    private val token   get() = "Bearer ${session.getToken()}"

    suspend fun getRankingGeral(): Resource<List<RankingGeralItem>> {
        return try {
            val r = api.getRankingGeral(token)
            if (r.isSuccessful) Resource.Success(r.body() ?: emptyList())
            else Resource.Error("Erro ${r.code()}")
        } catch (e: java.net.UnknownHostException) { Resource.Error("Sem conexão com a internet") }
          catch (e: Exception) { Resource.Error("Erro: ${e.message}") }
    }

    suspend fun getRankingModalidade(modalidadeId: String): Resource<RankingModalidadeResponse> {
        return try {
            val r = api.getRankingModalidade(token, modalidadeId)
            if (r.isSuccessful) Resource.Success(r.body() ?: RankingModalidadeResponse())
            else Resource.Error("Erro ${r.code()}")
        } catch (e: java.net.UnknownHostException) { Resource.Error("Sem conexão com a internet") }
          catch (e: Exception) { Resource.Error("Erro: ${e.message}") }
    }
}
