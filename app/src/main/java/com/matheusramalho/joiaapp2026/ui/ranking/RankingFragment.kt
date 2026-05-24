package com.matheusramalho.joiaapp2026.ui.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.matheusramalho.joiaapp2026.R
import com.matheusramalho.joiaapp2026.data.model.ModalidadeResponse
import com.matheusramalho.joiaapp2026.data.model.RankingGeralItem
import com.matheusramalho.joiaapp2026.databinding.FragmentRankingBinding
import com.matheusramalho.joiaapp2026.utils.Resource

class RankingFragment : Fragment() {

    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RankingViewModel by viewModels {
        RankingViewModelFactory(requireContext())
    }

    private val adapter = RankingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupAbas()
        setupSwipeRefresh()
        observeData()

        viewModel.init()
    }

    private fun setupRecyclerView() {
        binding.rvRanking.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRanking.adapter = adapter
        binding.rvRanking.isNestedScrollingEnabled = false
    }

    private fun setupAbas() {
        binding.tabGeral.setOnClickListener {
            viewModel.abaAtiva = "geral"
            atualizarEstiloAbas()
            binding.scrollChips.visibility = View.GONE
            binding.tvSubtitulo.text = "Classificação geral dos cursos"
            viewModel.carregarRankingGeral()
        }

        binding.tabModalidade.setOnClickListener {
            viewModel.abaAtiva = "modalidade"
            atualizarEstiloAbas()
            binding.scrollChips.visibility = View.VISIBLE
            binding.tvSubtitulo.text = "Classificação por modalidade"
            // Carrega a primeira modalidade se ainda não selecionada
            if (viewModel.modalidadeSelecionada == null) {
                val primeira = viewModel.modalidades.value?.firstOrNull()
                primeira?.let {
                    viewModel.modalidadeSelecionada = it
                    viewModel.carregarRankingModalidade(it.id)
                    construirChips(viewModel.modalidades.value ?: emptyList())
                }
            }
        }
    }

    private fun atualizarEstiloAbas() {
        val isGeral = viewModel.abaAtiva == "geral"
        binding.tabGeral.apply {
            setTextColor(if (isGeral) android.graphics.Color.WHITE
            else android.graphics.Color.parseColor("#AFA9EC"))
            setBackgroundResource(if (isGeral) R.drawable.bg_tab_active else 0)
        }
        binding.tabModalidade.apply {
            setTextColor(if (!isGeral) android.graphics.Color.WHITE
            else android.graphics.Color.parseColor("#AFA9EC"))
            setBackgroundResource(if (!isGeral) R.drawable.bg_tab_active else 0)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
    }

    private fun observeData() {
        viewModel.rankingGeral.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    exibirRanking(resource.data)
                }
                is Resource.Error -> {
                    setLoading(false)
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.tvEmpty.text = resource.message
                }
            }
        }

        viewModel.rankingModalidade.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    val tabela = resource.data.tabela
                    binding.tvSubtitulo.text = resource.data.modalidade.nome
                    exibirRanking(tabela)
                }
                is Resource.Error -> {
                    setLoading(false)
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.tvEmpty.text = resource.message
                }
            }
        }

        viewModel.modalidades.observe(viewLifecycleOwner) { modalidades ->
            if (viewModel.abaAtiva == "modalidade") {
                construirChips(modalidades)
            }
        }
    }

    private fun exibirRanking(lista: List<RankingGeralItem>) {
        if (lista.isEmpty()) {
            binding.layoutPodio.visibility = View.GONE
            binding.rvRanking.visibility   = View.GONE
            binding.tvEmpty.visibility     = View.VISIBLE
            return
        }

        binding.tvEmpty.visibility = View.GONE
        binding.rvRanking.visibility = View.VISIBLE

        // Pódio com top 3
        if (lista.size >= 3) {
            binding.layoutPodio.visibility = View.VISIBLE
            val top = lista.sortedByDescending { it.pontos }

            binding.tvSigla1.text = top[0].sigla.take(3)
            binding.tvNome1.text  = top[0].nome
            binding.tvPts1.text   = "${top[0].pontos} pts"

            binding.tvSigla2.text = top[1].sigla.take(3)
            binding.tvNome2.text  = top[1].nome
            binding.tvPts2.text   = "${top[1].pontos} pts"

            binding.tvSigla3.text = top[2].sigla.take(3)
            binding.tvNome3.text  = top[2].nome
            binding.tvPts3.text   = "${top[2].pontos} pts"

            // Lista a partir do 4º
            adapter.submitList(lista.sortedByDescending { it.pontos }.drop(3))
        } else {
            binding.layoutPodio.visibility = View.GONE
            adapter.submitList(lista.sortedByDescending { it.pontos })
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
                        construirChips(modalidades) // atualiza estado dos chips
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}