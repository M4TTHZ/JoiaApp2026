package com.matheusramalho.joiaapp2026.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.matheusramalho.joiaapp2026.data.model.JogoResponse
import com.matheusramalho.joiaapp2026.databinding.ItemJogoProximoBinding
import java.time.ZonedDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class JogoProximoAdapter : ListAdapter<JogoResponse, JogoProximoAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<JogoResponse>() {
            override fun areItemsTheSame(a: JogoResponse, b: JogoResponse) = a.id == b.id
            override fun areContentsTheSame(a: JogoResponse, b: JogoResponse) = a == b
        }
        private val FMT_HORA = DateTimeFormatter.ofPattern(" ⏰ HH:mm", Locale("pt", "BR"))
        private val FMT_DATA = DateTimeFormatter.ofPattern("\uD83D\uDCC5 dd/MM", Locale("pt", "BR"))
        private val BR = ZoneId.of("America/Cuiaba")
    }

    inner class VH(private val b: ItemJogoProximoBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(j: JogoResponse) {
            b.tvProxMandante.text  = j.nomeMandante()
            b.tvProxVisitante.text = j.nomeVisitante()
            b.tvProxLocal.text     = j.localFormatado()
            b.tvProxModalidade.text = j.nomeModalidade()

            b.tvProxHorario.text = try {
                val zdt = ZonedDateTime.parse(j.iniciaEm).withZoneSameInstant(BR)
                val hoje = ZonedDateTime.now(BR).toLocalDate()
                val dataJogo = zdt.toLocalDate()
                when {
                    dataJogo == hoje           -> "Hoje ${zdt.format(FMT_HORA)}"
                    dataJogo == hoje.plusDays(1) -> "Amanhã ${zdt.format(FMT_HORA)}"
                    else -> "${zdt.format(FMT_DATA)} ${zdt.format(FMT_HORA)}"
                }
            } catch (e: Exception) { j.iniciaEm }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemJogoProximoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}
