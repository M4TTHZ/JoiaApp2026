package com.matheusramalho.joiaapp2026.data.repository

import android.content.Context
import com.matheusramalho.joiaapp2026.data.api.RetrofitClient
import com.matheusramalho.joiaapp2026.data.model.*
import com.matheusramalho.joiaapp2026.utils.Resource
import com.matheusramalho.joiaapp2026.utils.SessionManager

class AuthRepository(context: Context) {

    private val api     = RetrofitClient.authApi
    private val session = SessionManager(context)
    private val token   get() = "Bearer ${session.getToken()}"

    suspend fun login(email: String, senha: String): Resource<LoginResponse> {
        return try {
            val r = api.login(LoginRequest(email, senha))
            if (r.isSuccessful) {
                val body = r.body() ?: return Resource.Error("Resposta vazia")
                session.saveSession(
                    token   = body.token,
                    nome    = body.user.nome,
                    email   = body.user.email,
                    role    = body.user.role,
                    cursoId = body.user.cursoId ?: ""
                )
                Resource.Success(body)
            } else {
                when (r.code()) {
                    401  -> Resource.Error("E-mail ou senha incorretos")
                    else -> Resource.Error("Erro ${r.code()}")
                }
            }
        } catch (e: java.net.UnknownHostException) { Resource.Error("Sem conexão com a internet") }
          catch (e: Exception) { Resource.Error("Erro: ${e.message}") }
    }

    suspend fun register(
        nome: String, email: String, senha: String,
        cpf: String, telefone: String, cursoId: String
    ): Resource<LoginResponse> {
        return try {
            val r = api.register(RegisterRequest(nome, email, senha, cpf, telefone, cursoId))
            if (r.isSuccessful) {
                val body = r.body() ?: return Resource.Error("Resposta vazia")
                session.saveSession(
                    token   = body.token,
                    nome    = body.user.nome,
                    email   = body.user.email,
                    role    = body.user.role,
                    cursoId = body.user.cursoId ?: ""
                )
                Resource.Success(body)
            } else {
                when (r.code()) {
                    409  -> Resource.Error("E-mail já cadastrado")
                    422  -> Resource.Error("Dados inválidos — verifique os campos")
                    else -> Resource.Error("Erro ${r.code()}")
                }
            }
        } catch (e: java.net.UnknownHostException) { Resource.Error("Sem conexão com a internet") }
          catch (e: Exception) { Resource.Error("Erro: ${e.message}") }
    }

    suspend fun getCursos(): Resource<List<CursoResponse>> {
        return try {
            val r = api.getCursos()
            if (r.isSuccessful) Resource.Success(r.body() ?: emptyList())
            else Resource.Error("Erro ${r.code()}")
        } catch (e: Exception) { Resource.Error("Erro: ${e.message}") }
    }

    suspend fun getMe(): Resource<UserResponse> {
        return try {
            val r = api.getMe(token)
            if (r.isSuccessful) Resource.Success(r.body()!!)
            else Resource.Error("Erro ${r.code()}")
        } catch (e: Exception) { Resource.Error("Erro: ${e.message}") }
    }

    suspend fun updateMe(nome: String, cpf: String?, telefone: String?, cursoId: String?): Resource<UserResponse> {
        return try {
            val r = api.updateMe(token, UpdateProfileRequest(nome, cpf, telefone, cursoId))
            if (r.isSuccessful) {
                val body = r.body()!!
                session.saveSession(
                    token   = session.getToken() ?: "",
                    nome    = body.nome,
                    email   = body.email,
                    role    = body.role,
                    cursoId = body.cursoId ?: ""
                )
                Resource.Success(body)
            } else Resource.Error("Erro ${r.code()}")
        } catch (e: Exception) { Resource.Error("Erro: ${e.message}") }
    }
}
