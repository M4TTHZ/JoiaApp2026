package com.matheusramalho.joiaapp2026.data.api

import com.matheusramalho.joiaapp2026.data.model.EquipeResponse
import com.matheusramalho.joiaapp2026.data.model.JogoResponse
import com.matheusramalho.joiaapp2026.data.model.ModalidadeResponse
import retrofit2.Response
import retrofit2.http.*

interface GameApi {

    @GET("jogos")
    suspend fun getJogos(
        @Header("Authorization") token: String
    ): Response<List<JogoResponse>>

    @GET("jogos/{id}")
    suspend fun getJogo(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<JogoResponse>

    @GET("modalidades")
    suspend fun getModalidades(
        @Header("Authorization") token: String
    ): Response<List<ModalidadeResponse>>

    @GET("equipes")
    suspend fun getEquipes(
        @Header("Authorization") token: String,
        @Query("cursoId") cursoId: String? = null,
        @Query("modalidadeId") modalidadeId: String? = null
    ): Response<List<EquipeResponse>>
}
