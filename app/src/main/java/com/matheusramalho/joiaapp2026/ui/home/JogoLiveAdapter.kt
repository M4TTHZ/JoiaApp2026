package com.matheusramalho.joiaapp2026.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.matheusramalho.joiaapp2026.data.model.JogoResponse
import com.matheusramalho.joiaapp2026.databinding.ItemJogoLiveBinding

class JogoLiveAdapter : ListAdapter<JogoResponse, JogoLiveAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<JogoResponse>() {
            override fun areItemsTheSame(a: JogoResponse, b: JogoResponse) = a.id == b.id
            override fun areContentsTheSame(a: JogoResponse, b: JogoResponse) = a == b
        }
    }

    inner class VH(private val b: ItemJogoLiveBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(j: JogoResponse) {
            b.tvLiveFase.text     = j.fase.ifBlank { "Ao vivo" }
            b.tvLiveMandante.text = j.mandanteId.take(8)   // substitua por nome real
            b.tvLiveVisitante.text= j.visitanteId.take(8)
            b.tvLiveLocal.text    = j.local.ifBlank { "Local não informado" }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemJogoLiveBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}