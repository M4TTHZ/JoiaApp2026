package com.matheusramalho.joiaapp2026.ui.resultados

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.matheusramalho.joiaapp2026.data.model.JogoResponse
import com.matheusramalho.joiaapp2026.databinding.ItemResultadoBinding
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class ResultadosAdapter : ListAdapter<JogoResponse, ResultadosAdapter.VH>(DIFF) {

    // Mapa de modalidadeId → nome (populado pelo Fragment)
    var modalidadesMap: Map<String, String> = emptyMap()

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<JogoResponse>() {
            override fun areItemsTheSame(a: JogoResponse, b: JogoResponse) = a.id == b.id
            override fun areContentsTheSame(a: JogoResponse, b: JogoResponse) = a == b
        }
        private val FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy · HH:mm", Locale("pt", "BR"))
    }

    inner class VH(private val b: ItemResultadoBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(j: JogoResponse) {
            b.tvMandante.text  = j.mandanteId.take(8)   // substitua por nome real futuramente
            b.tvVisitante.text = j.visitanteId.take(8)
            b.tvLocal.text     = j.local.ifBlank { "Local não informado" }
            b.tvFase.text      = j.fase.ifBlank { "—" }
            b.tvModalidade.text = modalidadesMap[j.modalidadeId] ?: "Modalidade"

            b.tvData.text = try {
                ZonedDateTime.parse(j.iniciaEm).format(FMT)
            } catch (e: Exception) { j.iniciaEm }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemResultadoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}