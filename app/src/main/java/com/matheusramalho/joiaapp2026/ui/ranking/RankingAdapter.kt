package com.matheusramalho.joiaapp2026.ui.ranking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.matheusramalho.joiaapp2026.data.model.RankingGeralItem
import com.matheusramalho.joiaapp2026.databinding.ItemRankingBinding

class RankingAdapter : ListAdapter<RankingGeralItem, RankingAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<RankingGeralItem>() {
            override fun areItemsTheSame(a: RankingGeralItem, b: RankingGeralItem) = a.cursoId == b.cursoId
            override fun areContentsTheSame(a: RankingGeralItem, b: RankingGeralItem) = a == b
        }
    }

    inner class VH(private val b: ItemRankingBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: RankingGeralItem, pos: Int) {
            b.tvPosicao.text = "${pos + 1}"
            b.tvSigla.text   = item.sigla.take(3)
            b.tvNome.text    = item.nome
            b.tvPontos.text  = "${item.pontos} pts"

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
        VH(ItemRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position), position)
}
