package com.matheusramalho.joiaapp2026.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// Objeto curso aninhado dentro de Equipe
@JsonClass(generateAdapter = true)
data class CursoSimples(
    @Json(name = "id")    val id: String    = "",
    @Json(name = "nome")  val nome: String  = "",
    @Json(name = "sigla") val sigla: String = ""
)

// Objeto modalidade aninhado dentro de Jogo
@JsonClass(generateAdapter = true)
data class ModalidadeSimples(
    @Json(name = "id")   val id: String   = "",
    @Json(name = "nome") val nome: String = "",
    @Json(name = "tipo") val tipo: String = ""
)

// Equipe com curso e modalidade aninhados
@JsonClass(generateAdapter = true)
data class EquipeResponse(
    @Json(name = "id")           val id: String              = "",
    @Json(name = "nome")         val nome: String            = "",
    @Json(name = "genero")       val genero: String          = "",
    @Json(name = "modalidadeId") val modalidadeId: String    = "",
    @Json(name = "cursoId")      val cursoId: String         = "",
    @Json(name = "curso")        val curso: CursoSimples?    = null,
    @Json(name = "modalidade")   val modalidade: ModalidadeSimples? = null
)

// Jogo completo com equipes aninhadas
@JsonClass(generateAdapter = true)
data class JogoResponse(
    @Json(name = "id")              val id: String              = "",
    @Json(name = "modalidadeId")    val modalidadeId: String    = "",
    @Json(name = "mandanteId")      val mandanteId: String      = "",
    @Json(name = "visitanteId")     val visitanteId: String     = "",
    @Json(name = "placarMandante")  val placarMandante: Int     = 0,
    @Json(name = "placarVisitante") val placarVisitante: Int    = 0,
    @Json(name = "status")          val status: String          = "",
    @Json(name = "fase")            val fase: String            = "",
    @Json(name = "local")           val local: String?          = null,
    @Json(name = "iniciaEm")        val iniciaEm: String        = "",
    @Json(name = "iniciadoEm")      val iniciadoEm: String?     = null,
    @Json(name = "finalizadoEm")    val finalizadoEm: String?   = null,
    @Json(name = "observacoes")     val observacoes: String?    = null,
    @Json(name = "modalidade")      val modalidade: ModalidadeSimples? = null,
    @Json(name = "mandante")        val mandante: EquipeResponse?      = null,
    @Json(name = "visitante")       val visitante: EquipeResponse?     = null
) {
    // Helpers para exibição
    fun nomeMandante()  = mandante?.nome  ?: mandanteId.take(6)
    fun nomeVisitante() = visitante?.nome ?: visitanteId.take(6)
    fun siglaMandante()  = mandante?.curso?.sigla  ?: ""
    fun siglaVisitante() = visitante?.curso?.sigla ?: ""
    fun nomeModalidade() = modalidade?.nome ?: ""
    fun localFormatado() = local.takeIf { !it.isNullOrBlank() } ?: "Local não informado"
    fun faseFormatada()  = when(fase.uppercase()) {
        "CLASSIFICATORIA"    -> "Classificatória"
        "OITAVAS"            -> "Oitavas"
        "QUARTAS"            -> "Quartas"
        "SEMIFINAL"          -> "Semifinal"
        "DISPUTA_TERCEIRO"   -> "3º Lugar"
        "FINAL"              -> "Final"
        else                 -> fase
    }
    fun placares() = "$placarMandante × $placarVisitante"
    fun isEmAndamento() = status.uppercase() == "EM_ANDAMENTO"
    fun isFinalizado()  = status.uppercase() == "FINALIZADO"
    fun isAgendado()    = status.uppercase() == "AGENDADO"
}
