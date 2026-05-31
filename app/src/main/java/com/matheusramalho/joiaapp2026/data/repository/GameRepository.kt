package com.matheusramalho.joiaapp2026.data.repository

import android.content.Context
import com.matheusramalho.joiaapp2026.data.api.RetrofitClient
import com.matheusramalho.joiaapp2026.data.model.EquipeResponse
import com.matheusramalho.joiaapp2026.data.model.JogoResponse
import com.matheusramalho.joiaapp2026.data.model.ModalidadeResponse
import com.matheusramalho.joiaapp2026.utils.Resource
import com.matheusramalho.joiaapp2026.utils.SessionManager

class GameRepository(context: Context) {

    private val api   = RetrofitClient.gameApi
    private val session = SessionManager(context)
    private val token   get() = "Bearer ${session.getToken()}"

    suspend fun getJogos(): Resource<List<JogoResponse>> {
        return try {
            val r = api.getJogos(token)
            if (r.isSuccessful) Resource.Success(r.body() ?: emptyList())
            else when (r.code()) {
                401  -> Resource.Error("Sessão expirada — faça login novamente")
                else -> Resource.Error("Erro ${r.code()}")
            }
        } catch (e: java.net.UnknownHostException) { Resource.Error("Sem conexão com a internet") }
          catch (e: java.net.SocketTimeoutException) { Resource.Error("Tempo de conexão esgotado") }
          catch (e: Exception) { Resource.Error("Erro: ${e.message}") }
    }

    suspend fun getJogo(id: String): Resource<JogoResponse> {
        return try {
            val r = api.getJogo(token, id)
            if (r.isSuccessful) Resource.Success(r.body()!!)
            else Resource.Error("Erro ${r.code()}")
        } catch (e: Exception) { Resource.Error("Erro: ${e.message}") }
    }

    suspend fun getModalidades(): Resource<List<ModalidadeResponse>> {
        return try {
            val r = api.getModalidades(token)
            if (r.isSuccessful) Resource.Success(r.body() ?: emptyList())
            else Resource.Error("Erro ${r.code()}")
        } catch (e: Exception) { Resource.Error("Erro: ${e.message}") }
    }

    suspend fun getEquipes(
        cursoId: String? = null,
        modalidadeId: String? = null
    ): Resource<List<EquipeResponse>> {
        return try {
            val r = api.getEquipes(token, cursoId, modalidadeId)
            if (r.isSuccessful) Resource.Success(r.body() ?: emptyList())
            else Resource.Error("Erro ${r.code()}")
        } catch (e: Exception) { Resource.Error("Erro: ${e.message}") }
    }
}
