package com.matheusramalho.joiaapp2026.data.repository

import android.content.Context
import com.matheusramalho.joiaapp2026.data.api.LoginRequest
import com.matheusramalho.joiaapp2026.data.api.LoginResponse
import com.matheusramalho.joiaapp2026.data.api.RetrofitClient
import com.matheusramalho.joiaapp2026.utils.Resource
import com.matheusramalho.joiaapp2026.utils.SessionManager

class AuthRepository(context: Context) {

    private val api     = RetrofitClient.authApi
    private val session = SessionManager(context)

    suspend fun login(email: String, senha: String): Resource<LoginResponse> {
        return try {
            val response = api.login(LoginRequest(email, senha))

            if (response.isSuccessful) {
                val body = response.body()
                    ?: return Resource.Error("Resposta vazia do servidor")

                // Persiste a sessão localmente
                session.saveSession(
                    token   = body.token,
                    nome    = body.user.nome,
                    email   = body.user.email,
                    role    = body.user.role,
//                    cursoId = body.user.cursoId
                )

                Resource.Success(body)

            } else {
                // 401, 403, 422...
                val code = response.code()
                when (code) {
                    401  -> Resource.Error("E-mail ou senha incorretos")
                    422  -> Resource.Error("Dados inválidos")
                    else -> Resource.Error("Erro $code — tente novamente")
                }
            }

        } catch (e: java.net.UnknownHostException) {
            Resource.Error("Sem conexão com a internet")
        } catch (e: java.net.SocketTimeoutException) {
            Resource.Error("Tempo de conexão esgotado")
        } catch (e: Exception) {
            Resource.Error("Erro inesperado: ${e.message}")
        }
    }
}