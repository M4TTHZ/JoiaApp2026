package com.matheusramalho.joiaapp2026.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.matheusramalho.joiaapp2026.data.model.JogoResponse
import com.matheusramalho.joiaapp2026.databinding.ItemJogoProximoBinding
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class JogoProximoAdapter : ListAdapter<JogoResponse, JogoProximoAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<JogoResponse>() {
            override fun areItemsTheSame(a: JogoResponse, b: JogoResponse) = a.id == b.id
            override fun areContentsTheSame(a: JogoResponse, b: JogoResponse) = a == b
        }
        private val FMT = DateTimeFormatter.ofPattern("dd/MM HH:mm", Locale("pt", "BR"))
    }

    inner class VH(private val b: ItemJogoProximoBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(j: JogoResponse) {
            b.tvProxMandante.text   = j.mandanteId.take(8)   // substitua por nome real
            b.tvProxVisitante.text  = j.visitanteId.take(8)
            b.tvProxLocal.text      = j.local.ifBlank { "Local não informado" }
            b.tvProxModalidade.text = j.modalidadeId.take(6) // substitua por nome real

            b.tvProxHorario.text = try {
                ZonedDateTime.parse(j.iniciaEm).format(FMT)
            } catch (e: Exception) { j.iniciaEm }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemJogoProximoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}