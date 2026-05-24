package com.matheusramalho.joiaapp2026.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ModalidadeResponse(
    @Json(name = "id")   val id: String   = "",
    @Json(name = "nome") val nome: String = ""
)