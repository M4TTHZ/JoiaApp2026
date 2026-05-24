package com.matheusramalho.joiaapp2026.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// GET /ranking/geral
@JsonClass(generateAdapter = true)
data class RankingGeralItem(
    @Json(name = "cursoId") val cursoId: String = "",
    @Json(name = "nome")    val nome: String    = "",
    @Json(name = "sigla")   val sigla: String   = "",
    @Json(name = "pontos")  val pontos: Int      = 0
)

// GET /ranking/modalidade/{id}
@JsonClass(generateAdapter = true)
data class RankingModalidadeResponse(
    @Json(name = "modalidade") val modalidade: ModalidadeDetalhe    = ModalidadeDetalhe(),
    @Json(name = "tabela")     val tabela: List<RankingGeralItem>   = emptyList()
)

@JsonClass(generateAdapter = true)
data class ModalidadeDetalhe(
    @Json(name = "id")                 val id: String                  = "",
    @Json(name = "nome")               val nome: String                = "",
    @Json(name = "tipo")               val tipo: String                = "",
    @Json(name = "descricao")          val descricao: String           = "",
    @Json(name = "maxEquipesPorCurso") val maxEquipesPorCurso: Int?    = null,
    @Json(name = "maxAtletasPorCurso") val maxAtletasPorCurso: Int?    = null
)