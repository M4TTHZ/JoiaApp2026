package com.matheusramalho.joiaapp2026.ui.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.matheusramalho.joiaapp2026.data.model.ModalidadeResponse
import com.matheusramalho.joiaapp2026.data.model.RankingGeralItem
import com.matheusramalho.joiaapp2026.data.model.RankingTabelaItem
import com.matheusramalho.joiaapp2026.data.repository.GameRepository
import com.matheusramalho.joiaapp2026.data.repository.RankingRepository
import com.matheusramalho.joiaapp2026.databinding.FragmentRankingBinding
import com.matheusramalho.joiaapp2026.utils.Resource

class RankingFragment : Fragment() {

    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RankingViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(c: Class<T>): T =
                RankingViewModel(
                    RankingRepository(requireContext()),
                    GameRepository(requireContext())
                ) as T
        }
    }

    private val adapterGeral   = RankingAdapter()
    private val adapterTabela  = RankingTabelaAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvRanking.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRanking.adapter = adapterGeral
        binding.rvRanking.isNestedScrollingEnabled = false

        setupAbas()
        setupSwipeRefresh()
        observeData()
        viewModel.init()
    }

    private fun setupAbas() {
        binding.tabGeral.setOnClickListener {
            if (viewModel.abaAtiva == "geral") return@setOnClickListener
            viewModel.abaAtiva = "geral"
            atualizarAbas()
            binding.scrollChips.visibility = View.GONE
            binding.tvSubtitulo.text = "Classificação geral dos cursos"
            // Troca para adapter geral
            binding.rvRanking.adapter = adapterGeral
            viewModel.carregarRankingGeral()
        }
        binding.tabModalidade.setOnClickListener {
            if (viewModel.abaAtiva == "modalidade") return@setOnClickListener
            viewModel.abaAtiva = "modalidade"
            atualizarAbas()
            binding.scrollChips.visibility = View.VISIBLE
            binding.tvSubtitulo.text = "Classificação por modalidade"
            // Troca para adapter de tabela
            binding.rvRanking.adapter = adapterTabela
            binding.layoutPodio.visibility = View.GONE
            viewModel.modalidadeSelecionada?.let {
                viewModel.carregarRankingModalidade(it.id)
            } ?: run {
                viewModel.modalidades.value?.firstOrNull()?.let { mod ->
                    viewModel.modalidadeSelecionada = mod
                    viewModel.carregarRankingModalidade(mod.id)
                    construirChips(viewModel.modalidades.value ?: emptyList())
                }
            }
        }
    }

    private fun atualizarAbas() {
        val isGeral = viewModel.abaAtiva == "geral"
        binding.tabGeral.apply {
            setTextColor(if (isGeral) android.graphics.Color.WHITE
                         else android.graphics.Color.parseColor("#A898FB"))
            setBackgroundResource(if (isGeral) com.matheusramalho.joiaapp2026.R.drawable.bg_tab_active else 0)
        }
        binding.tabModalidade.apply {
            setTextColor(if (!isGeral) android.graphics.Color.WHITE
                         else android.graphics.Color.parseColor("#A898FB"))
            setBackgroundResource(if (!isGeral) com.matheusramalho.joiaapp2026.R.drawable.bg_tab_active else 0)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
    }

    private fun observeData() {
        // Ranking geral
        viewModel.rankingGeral.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    exibirRankingGeral(resource.data)
                }
                is Resource.Error -> {
                    setLoading(false)
                    mostrarVazio(resource.message)
                }
            }
        }

        // Ranking por modalidade (tabela detalhada)
        viewModel.rankingModalidade.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    binding.layoutPodio.visibility = View.GONE
                    val tabela = resource.data.tabela.sortedByDescending { it.pontos }
                    val modalNome = resource.data.modalidade.nome
                    val tipo = resource.data.modalidade.tipo
                    binding.tvSubtitulo.text = "$modalNome · $tipo"
                    if (tabela.isEmpty()) {
                        mostrarVazio("Nenhuma equipe nesta modalidade")
                    } else {
                        binding.tvEmpty.visibility   = View.GONE
                        binding.rvRanking.visibility = View.VISIBLE
                        adapterTabela.submitList(tabela)
                    }
                }
                is Resource.Error -> {
                    setLoading(false)
                    mostrarVazio(resource.message)
                }
            }
        }

        // Modalidades para chips
        viewModel.modalidades.observe(viewLifecycleOwner) { modalidades ->
            if (viewModel.abaAtiva == "modalidade") construirChips(modalidades)
        }
    }

    private fun exibirRankingGeral(lista: List<RankingGeralItem>) {
        if (lista.isEmpty()) { mostrarVazio("Nenhuma classificação disponível"); return }

        binding.tvEmpty.visibility   = View.GONE
        binding.rvRanking.visibility = View.VISIBLE
        val top = lista.sortedByDescending { it.pontos }

        if (top.size >= 3) {
            binding.layoutPodio.visibility = View.VISIBLE
            binding.tvSigla1.text = top[0].sigla.take(3)
            binding.tvNome1.text  = top[0].nome
            binding.tvPts1.text   = "${top[0].pontos} pts"
            binding.tvSigla2.text = top[1].sigla.take(3)
            binding.tvNome2.text  = top[1].nome
            binding.tvPts2.text   = "${top[1].pontos} pts"
            binding.tvSigla3.text = top[2].sigla.take(3)
            binding.tvNome3.text  = top[2].nome
            binding.tvPts3.text   = "${top[2].pontos} pts"
            adapterGeral.submitList(top.drop(3))
        } else {
            binding.layoutPodio.visibility = View.GONE
            adapterGeral.submitList(top)
        }
    }

    private fun construirChips(modalidades: List<ModalidadeResponse>) {
        binding.layoutChips.removeAllViews()
        modalidades.forEach { mod ->
            val chip = Chip(requireContext()).apply {
                text        = mod.nome
                isCheckable = true
                isChecked   = viewModel.modalidadeSelecionada?.id == mod.id
                setOnCheckedChangeListener { _, checked ->
                    if (checked) {
                        viewModel.modalidadeSelecionada = mod
                        viewModel.carregarRankingModalidade(mod.id)
                        construirChips(modalidades)
                    }
                }
            }
            binding.layoutChips.addView(chip)
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility    = if (loading) View.VISIBLE else View.GONE
        binding.swipeRefresh.isRefreshing = false
        if (loading) {
            binding.layoutPodio.visibility = View.GONE
            binding.rvRanking.visibility   = View.GONE
            binding.tvEmpty.visibility     = View.GONE
        }
    }

    private fun mostrarVazio(msg: String) {
        binding.layoutPodio.visibility = View.GONE
        binding.rvRanking.visibility   = View.GONE
        binding.tvEmpty.visibility     = View.VISIBLE
        try { binding.tvEmpty.text = msg } catch (_: Exception) {}
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
