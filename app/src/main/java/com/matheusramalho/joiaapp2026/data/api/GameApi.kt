package com.matheusramalho.joiaapp2026.data.api

import com.matheusramalho.joiaapp2026.data.model.JogoResponse
import com.matheusramalho.joiaapp2026.data.model.ModalidadeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface GameApi {

    @GET("jogos")
    suspend fun getJogos(
        @Header("Authorization") token: String
    ): Response<List<JogoResponse>>

    @GET("modalidades")
    suspend fun getModalidades(
        @Header("Authorization") token: String
    ): Response<List<ModalidadeResponse>>
}
