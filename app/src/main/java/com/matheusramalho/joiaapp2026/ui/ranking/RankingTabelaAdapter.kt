package com.matheusramalho.joiaapp2026.ui.ranking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.matheusramalho.joiaapp2026.data.model.RankingTabelaItem
import com.matheusramalho.joiaapp2026.databinding.ItemRankingTabelaBinding

class RankingTabelaAdapter : ListAdapter<RankingTabelaItem, RankingTabelaAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<RankingTabelaItem>() {
            override fun areItemsTheSame(a: RankingTabelaItem, b: RankingTabelaItem) = a.equipeId == b.equipeId
            override fun areContentsTheSame(a: RankingTabelaItem, b: RankingTabelaItem) = a == b
        }
    }

    inner class VH(private val b: ItemRankingTabelaBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: RankingTabelaItem, pos: Int) {
            b.tvPosicao.text   = "${pos + 1}"
            b.tvEquipe.text    = item.equipe
            b.tvCurso.text     = item.curso?.sigla ?: ""
            b.tvJogos.text     = "${item.jogos}"
            b.tvVitorias.text  = "${item.vitorias}"
            b.tvEmpates.text   = "${item.empates}"
            b.tvDerrotas.text  = "${item.derrotas}"
            b.tvSaldo.text     = if (item.saldo >= 0) "+${item.saldo}" else "${item.saldo}"
            b.tvPontos.text    = "${item.pontos}"

//            val bgColor = when (pos) {
//                0 -> android.graphics.Color.parseColor("#FFC107")
//                1 -> android.graphics.Color.parseColor("#B0BEC5")
//                2 -> android.graphics.Color.parseColor("#A97142")
//                else -> android.graphics.Color.TRANSPARENT
//            }
//            b.root.setCardBackgroundColor(bgColor)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemRankingTabelaBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position), position)
}
