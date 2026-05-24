package com.matheusramalho.joiaapp2026.data.repository


import android.content.Context
import com.matheusramalho.joiaapp2026.data.api.RetrofitClient
import com.matheusramalho.joiaapp2026.data.model.RankingGeralItem
import com.matheusramalho.joiaapp2026.data.model.RankingModalidadeResponse
import com.matheusramalho.joiaapp2026.utils.Resource
import com.matheusramalho.joiaapp2026.utils.SessionManager

class RankingRepository(private val context: Context) {

    private val api   = RetrofitClient.rankingApi
    private val token get() = "Bearer ${SessionManager(context).getToken()}"

    suspend fun getRankingGeral(): Resource<List<RankingGeralItem>> {
        return try {
            val response = api.getRankingGeral(token)
            if (response.isSuccessful)
                Resource.Success(response.body() ?: emptyList())
            else
                Resource.Error("Erro ${response.code()}")
        } catch (e: java.net.UnknownHostException) {
            Resource.Error("Sem conexão com a internet")
        } catch (e: Exception) {
            Resource.Error("Erro: ${e.message}")
        }
    }

    suspend fun getRankingModalidade(modalidadeId: String): Resource<RankingModalidadeResponse> {
        return try {
            val response = api.getRankingModalidade(token, modalidadeId)
            if (response.isSuccessful)
                Resource.Success(response.body() ?: RankingModalidadeResponse())
            else
                Resource.Error("Erro ${response.code()}")
        } catch (e: java.net.UnknownHostException) {
            Resource.Error("Sem conexão com a internet")
        } catch (e: Exception) {
            Resource.Error("Erro: ${e.message}")
        }
    }
}