package com.matheusramalho.joiaapp2026.data.api

import com.matheusramalho.joiaapp2026.data.model.CursoResponse
import com.matheusramalho.joiaapp2026.data.model.LoginRequest
import com.matheusramalho.joiaapp2026.data.model.LoginResponse
import com.matheusramalho.joiaapp2026.data.model.RegisterRequest
import com.matheusramalho.joiaapp2026.data.model.UpdateProfileRequest
import com.matheusramalho.joiaapp2026.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @GET("cursos")
    suspend fun getCursos(): Response<List<CursoResponse>>

    @GET("users/me")
    suspend fun getMe(
        @Header("Authorization") token: String
    ): Response<UserResponse>

    @PATCH("users/me")
    suspend fun updateMe(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<UserResponse>
}
