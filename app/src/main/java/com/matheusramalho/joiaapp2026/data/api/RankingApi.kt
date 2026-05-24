package com.matheusramalho.joiaapp2026.data.api

import com.matheusramalho.joiaapp2026.data.model.RankingGeralItem
import com.matheusramalho.joiaapp2026.data.model.RankingModalidadeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface RankingApi {

    @GET("ranking/geral")
    suspend fun getRankingGeral(
        @Header("Authorization") token: String
    ): Response<List<RankingGeralItem>>

    @GET("ranking/modalidade/{modalidadeId}")
    suspend fun getRankingModalidade(
        @Header("Authorization") token: String,
        @Path("modalidadeId") modalidadeId: String
    ): Response<RankingModalidadeResponse>
}