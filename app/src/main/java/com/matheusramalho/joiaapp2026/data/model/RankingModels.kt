package com.matheusramalho.joiaapp2026.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// GET /ranking/geral
@JsonClass(generateAdapter = true)
data class RankingGeralItem(
    @Json(name = "cursoId") val cursoId: String = "",
    @Json(name = "nome")    val nome: String    = "",
    @Json(name = "sigla")   val sigla: String   = "",
    @Json(name = "pontos")  val pontos: Int     = 0
)

// GET /ranking/modalidade/{id} — item da tabela
@JsonClass(generateAdapter = true)
data class RankingTabelaItem(
    @Json(name = "equipeId")   val equipeId: String         = "",
    @Json(name = "equipe")     val equipe: String           = "",
    @Json(name = "curso")      val curso: CursoSimples?     = null,
    @Json(name = "jogos")      val jogos: Int               = 0,
    @Json(name = "vitorias")   val vitorias: Int            = 0,
    @Json(name = "empates")    val empates: Int             = 0,
    @Json(name = "derrotas")   val derrotas: Int            = 0,
    @Json(name = "golsPro")    val golsPro: Int             = 0,
    @Json(name = "golsContra") val golsContra: Int          = 0,
    @Json(name = "saldo")      val saldo: Int               = 0,
    @Json(name = "pontos")     val pontos: Int              = 0
)

@JsonClass(generateAdapter = true)
data class RankingModalidadeResponse(
    @Json(name = "modalidade") val modalidade: ModalidadeDetalhe     = ModalidadeDetalhe(),
    @Json(name = "tabela")     val tabela: List<RankingTabelaItem>   = emptyList()
)

@JsonClass(generateAdapter = true)
data class ModalidadeDetalhe(
    @Json(name = "id")                 val id: String    = "",
    @Json(name = "nome")               val nome: String  = "",
    @Json(name = "tipo")               val tipo: String  = "",
    @Json(name = "descricao")          val descricao: String? = null,
    @Json(name = "maxEquipesPorCurso") val maxEquipesPorCurso: Int? = null,
    @Json(name = "maxAtletasPorCurso") val maxAtletasPorCurso: Int? = null
)
