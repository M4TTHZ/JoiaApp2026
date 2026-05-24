package com.matheusramalho.joiaapp2026.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JogoResponse(
    @Json(name = "id")           val id: String          = "",
    @Json(name = "modalidadeId") val modalidadeId: String = "",
    @Json(name = "mandanteId")   val mandanteId: String   = "",
    @Json(name = "visitanteId")  val visitanteId: String  = "",
    @Json(name = "iniciaEm")     val iniciaEm: String     = "",
    @Json(name = "local")        val local: String        = "",
    @Json(name = "fase")         val fase: String         = "",
    @Json(name = "observacoes")  val observacoes: String  = "",
    @Json(name = "status")       val status: String       = ""
)