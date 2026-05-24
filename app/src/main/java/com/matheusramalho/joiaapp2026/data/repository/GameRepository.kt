package com.matheusramalho.joiaapp2026.data.repository

import android.content.Context
import com.matheusramalho.joiaapp2026.data.api.RetrofitClient
import com.matheusramalho.joiaapp2026.data.model.JogoResponse
import com.matheusramalho.joiaapp2026.data.model.ModalidadeResponse
import com.matheusramalho.joiaapp2026.utils.Resource
import com.matheusramalho.joiaapp2026.utils.SessionManager

class GameRepository(context: Context) {

    private val api     = RetrofitClient.gameApi
    private val session = SessionManager(context)
    private val token get() = "Bearer ${session.getToken()}"

    suspend fun getJogos(): Resource<List<JogoResponse>> {
        return try {
            val response = api.getJogos(token)
            if (response.isSuccessful) Resource.Success(response.body() ?: emptyList())
            else Resource.Error("Erro ${response.code()}")
        } catch (e: java.net.UnknownHostException) {
            Resource.Error("Sem conexão com a internet")
        } catch (e: Exception) {
            Resource.Error("Erro: ${e.message}")
        }
    }

    suspend fun getModalidades(): Resource<List<ModalidadeResponse>> {
        return try {
            val response = api.getModalidades(token)
            if (response.isSuccessful) Resource.Success(response.body() ?: emptyList())
            else Resource.Error("Erro ${response.code()}")
        } catch (e: Exception) {
            Resource.Error("Erro: ${e.message}")
        }
    }
}