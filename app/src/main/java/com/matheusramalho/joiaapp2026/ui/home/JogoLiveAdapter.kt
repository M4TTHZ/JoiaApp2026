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
            // ← compara placar também para forçar rebind quando muda
            override fun areContentsTheSame(a: JogoResponse, b: JogoResponse) =
                a.placarMandante  == b.placarMandante  &&
                        a.placarVisitante == b.placarVisitante &&
                        a.status          == b.status          &&
                        a.id              == b.id
        }
    }

    inner class VH(private val b: ItemJogoLiveBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(j: JogoResponse) {
            val faseLabel = buildString {
                append(j.nomeModalidade())
                if (j.fase.isNotBlank()) append(" · ${j.faseFormatada()}")
            }
            b.tvLiveFase.text      = faseLabel
            b.tvLiveMandante.text  = j.nomeMandante()
            b.tvLiveVisitante.text = j.nomeVisitante()
            b.tvLiveLocal.text     = j.localFormatado()

            // ← Placar direto no campo, sem reflection
            b.tvPlacar.text = "${j.placarMandante} × ${j.placarVisitante}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemJogoLiveBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}