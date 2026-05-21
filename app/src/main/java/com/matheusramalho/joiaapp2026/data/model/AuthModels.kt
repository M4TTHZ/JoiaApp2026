package com.matheusramalho.joiaapp2026.data.api

data class LoginRequest(
    val email: String,
    val senha: String
)

data class UserResponse(
    val id: String,
    val nome: String,
    val email: String,
    val role: String,
//    val cursoId: String
)

data class LoginResponse(
    val user: UserResponse,
    val token: String
)