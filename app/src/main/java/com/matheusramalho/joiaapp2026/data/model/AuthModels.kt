package com.matheusramalho.joiaapp2026.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "email") val email: String,
    @Json(name = "senha") val senha: String
)

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "id")        val id: String       = "",
    @Json(name = "nome")      val nome: String     = "",
    @Json(name = "email")     val email: String    = "",
    @Json(name = "role")      val role: String     = "",
    @Json(name = "cursoId")   val cursoId: String? = null,
    @Json(name = "cpf")       val cpf: String?     = null,
    @Json(name = "telefone")  val telefone: String?= null,
    @Json(name = "criadoEm")  val criadoEm: String?= null,
    @Json(name = "curso")     val curso: CursoSimples? = null
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "user")  val user: UserResponse = UserResponse(),
    @Json(name = "token") val token: String      = ""
)

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    @Json(name = "nome")     val nome: String,
    @Json(name = "email")    val email: String,
    @Json(name = "senha")    val senha: String,
    @Json(name = "cpf")      val cpf: String,
    @Json(name = "telefone") val telefone: String,
    @Json(name = "cursoId")  val cursoId: String
)

@JsonClass(generateAdapter = true)
data class UpdateProfileRequest(
    @Json(name = "nome")     val nome: String,
    @Json(name = "cpf")      val cpf: String?,
    @Json(name = "telefone") val telefone: String?,
    @Json(name = "cursoId")  val cursoId: String?
)

@JsonClass(generateAdapter = true)
data class CursoResponse(
    @Json(name = "id")    val id: String    = "",
    @Json(name = "nome")  val nome: String  = "",
    @Json(name = "sigla") val sigla: String = ""
)
