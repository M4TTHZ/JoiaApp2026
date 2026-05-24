package com.matheusramalho.joiaapp2026.data.repository

import android.content.Context
import com.matheusramalho.joiaapp2026.data.api.RetrofitClient
import com.matheusramalho.joiaapp2026.data.model.CursoResponse
import com.matheusramalho.joiaapp2026.data.model.LoginRequest
import com.matheusramalho.joiaapp2026.data.model.LoginResponse
import com.matheusramalho.joiaapp2026.data.model.RegisterRequest
import com.matheusramalho.joiaapp2026.utils.Resource
import com.matheusramalho.joiaapp2026.utils.SessionManager

class AuthRepository(context: Context) {

    private val api     = RetrofitClient.authApi
    private val session = SessionManager(context)

    suspend fun login(email: String, senha: String): Resource<LoginResponse> {
        return try {
            val response = api.login(LoginRequest(email, senha))
            if (response.isSuccessful) {
                val body = response.body() ?: return Resource.Error("Resposta vazia do servidor")
                session.saveSession(
                    token   = body.token,
                    nome    = body.user.nome,
                    email   = body.user.email,
                    role    = body.user.role,
                    cursoId = body.user.cursoId
                )
                Resource.Success(body)
            } else {
                when (response.code()) {
                    401  -> Resource.Error("E-mail ou senha incorretos")
                    else -> Resource.Error("Erro ${response.code()}")
                }
            }
        } catch (e: java.net.UnknownHostException) {
            Resource.Error("Sem conexão com a internet")
        } catch (e: Exception) {
            Resource.Error("Erro inesperado: ${e.message}")
        }
    }

    suspend fun getCursos(): Resource<List<CursoResponse>> {
        return try {
            val response = api.getCursos()
            if (response.isSuccessful) Resource.Success(response.body() ?: emptyList())
            else Resource.Error("Erro ao buscar cursos: ${response.code()}")
        } catch (e: Exception) {
            Resource.Error("Sem conexão: ${e.message}")
        }
    }

    suspend fun register(
        nome: String, email: String, senha: String,
        cpf: String, telefone: String, cursoId: String
    ): Resource<LoginResponse> {
        return try {
            val response = api.register(RegisterRequest(nome, email, senha, cpf, telefone, cursoId))
            if (response.isSuccessful) {
                val body = response.body() ?: return Resource.Error("Resposta vazia do servidor")
                session.saveSession(
                    token   = body.token,
                    nome    = body.user.nome,
                    email   = body.user.email,
                    role    = body.user.role,
                    cursoId = body.user.cursoId
                )
                Resource.Success(body)
            } else {
                when (response.code()) {
                    409  -> Resource.Error("E-mail já cadastrado")
                    422  -> Resource.Error("Dados inválidos — verifique os campos")
                    else -> Resource.Error("Erro ${response.code()}")
                }
            }
        } catch (e: java.net.UnknownHostException) {
            Resource.Error("Sem conexão com a internet")
        } catch (e: Exception) {
            Resource.Error("Erro inesperado: ${e.message}")
        }
    }
}
