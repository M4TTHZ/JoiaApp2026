package com.matheusramalho.joiaapp2026.ui.resultados

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.matheusramalho.joiaapp2026.data.model.JogoResponse
import com.matheusramalho.joiaapp2026.databinding.ItemResultadoBinding
import java.time.ZonedDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class ResultadosAdapter : ListAdapter<JogoResponse, ResultadosAdapter.VH>(DIFF) {

    lateinit var modalidadesMap: Map<String, String>

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<JogoResponse>() {
            override fun areItemsTheSame(a: JogoResponse, b: JogoResponse) = a.id == b.id
            override fun areContentsTheSame(a: JogoResponse, b: JogoResponse) = a == b
        }
        private val FMT = DateTimeFormatter.ofPattern("\uD83D\uDCC5 dd/MM ⏰HH:mm", Locale("pt", "BR"))
        private val BR  = ZoneId.of("America/Cuiaba")
    }

    inner class VH(private val b: ItemResultadoBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(j: JogoResponse) {
            b.tvMandante.text   = j.nomeMandante()
            b.tvVisitante.text  = j.nomeVisitante()
            b.tvLocal.text      = j.localFormatado()
            b.tvFase.text       = j.faseFormatada()
            b.tvModalidade.text = j.nomeModalidade()

            // Placar do resultado
            try {
                b.tvPlacar.text    = j.placares()
                b.tvPlacar.visibility = android.view.View.VISIBLE
            } catch (_: Exception) {}

            b.tvData.text = try {
                ZonedDateTime.parse(j.iniciaEm).withZoneSameInstant(BR).format(FMT)
            } catch (e: Exception) { j.iniciaEm }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemResultadoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
}
