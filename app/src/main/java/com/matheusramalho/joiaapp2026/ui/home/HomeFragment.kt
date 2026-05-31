package com.matheusramalho.joiaapp2026.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.matheusramalho.joiaapp2026.R
import com.matheusramalho.joiaapp2026.data.model.ModalidadeResponse
import com.matheusramalho.joiaapp2026.databinding.FragmentHomeBinding
import com.matheusramalho.joiaapp2026.utils.Resource
import com.matheusramalho.joiaapp2026.utils.SessionManager
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(requireContext())
    }

    private val liveAdapter   = JogoLiveAdapter()
    private val proximoAdapter = JogoProximoAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeader()
        setupCarrossel()
        setupProximos()
        setupSwipeRefresh()
        observeData()

        viewModel.init()
    }

    // ── Header com nome e curso do usuário ──────────────────────────────────
    private fun setupHeader() {
        val session = SessionManager(requireContext())
        val nome    = session.getNome() ?: "Usuário"

        binding.tvHeaderNome.text  = "Olá, ${nome.split(" ").first()}"
        binding.tvAvatar.text      = nome.take(1).uppercase()
    }

    // ── Carrossel horizontal com PagerSnapHelper ────────────────────────────
    private fun setupCarrossel() {
        val lm = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvLive.layoutManager = lm
        binding.rvLive.adapter        = liveAdapter

        // Snap: um card por vez como um carrossel
        PagerSnapHelper().attachToRecyclerView(binding.rvLive)

        // Atualiza dots conforme o scroll
        binding.rvLive.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                val pos = (lm.findFirstCompletelyVisibleItemPosition()).coerceAtLeast(0)
                atualizarDots(pos)
            }
        })
    }

    private fun atualizarDots(posAtiva: Int) {
        val count = liveAdapter.itemCount
        binding.layoutDots.removeAllViews()
        repeat(count) { i ->
            val dot = View(requireContext()).apply {
                val w = if (i == posAtiva) 20.dp else 6.dp
                layoutParams = ViewGroup.MarginLayoutParams(w, 6.dp).apply { marginEnd = 4.dp }
                background = ContextCompat.getDrawable(
                    requireContext(),
                    if (i == posAtiva) R.drawable.bg_dot_active else R.drawable.bg_dot
                )
            }
            binding.layoutDots.addView(dot)
        }
    }

    // ── RecyclerView de próximos jogos ──────────────────────────────────────
    private fun setupProximos() {
        binding.rvProximos.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProximos.adapter        = proximoAdapter
        binding.rvProximos.isNestedScrollingEnabled = false
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
    }

    // ── Observers ───────────────────────────────────────────────────────────
    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.jogosAoVivo.observe(viewLifecycleOwner) { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.swipeRefresh.isRefreshing = false
                            liveAdapter.submitList(resource.data)
                            binding.tvLiveCount.text = "${resource.data.size} jogos"
                            binding.tvLiveEmpty.visibility =
                                if (resource.data.isEmpty()) View.VISIBLE else View.GONE
                            binding.rvLive.visibility =
                                if (resource.data.isEmpty()) View.GONE else View.VISIBLE
                            atualizarDots(0)
                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.swipeRefresh.isRefreshing = false
                        }
                        else -> Unit
                    }
                }

                viewModel.proximosJogos.observe(viewLifecycleOwner) { lista ->
                    proximoAdapter.submitList(lista)
                    binding.tvProximosEmpty.visibility =
                        if (lista.isEmpty()) View.VISIBLE else View.GONE
                    binding.rvProximos.visibility =
                        if (lista.isEmpty()) View.GONE else View.VISIBLE
                }

                viewModel.modalidades.observe(viewLifecycleOwner) { modalidades ->
                    construirChips(modalidades)
                }
            }
        }
    }

    // ── Chips de filtro gerados dinamicamente ───────────────────────────────
    private fun construirChips(modalidades: List<ModalidadeResponse>) {
        binding.layoutChips.removeAllViews()

        // Chip "Todos"
        adicionarChip("Todos", null, selecionado = viewModel.filtroModalidadeId == null)

        // Chip "Hoje"
        adicionarChipDia("Hoje", "hoje")

        // Chip "Amanhã"
        adicionarChipDia("Amanhã", "amanha")

        // Chip "Meu curso"
        adicionarChipCurso()

        // Chips por modalidade
        modalidades.forEach { mod ->
            adicionarChip(mod.nome, mod.id, selecionado = viewModel.filtroModalidadeId == mod.id)
        }
    }

    private fun adicionarChip(label: String, modalidadeId: String?, selecionado: Boolean) {
        val chip = Chip(requireContext()).apply {
            text      = label
            isChecked = selecionado
            isCheckable = true
            setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    viewModel.filtroModalidadeId = modalidadeId
                    viewModel.aplicarFiltros()
                }
            }
        }
        binding.layoutChips.addView(chip)
    }

    private fun adicionarChipDia(label: String, dia: String) {
        val chip = Chip(requireContext()).apply {
            text        = label
            isChecked   = viewModel.filtroDia == dia
            isCheckable = true
            setOnCheckedChangeListener { _, checked ->
                viewModel.filtroDia = if (checked) dia else null
                viewModel.aplicarFiltros()
            }
        }
        binding.layoutChips.addView(chip)
    }

    private fun adicionarChipCurso() {
        val cursoId = SessionManager(requireContext()).getCursoId()
        val chip = Chip(requireContext()).apply {
            text        = "Meu curso"
            isChecked   = viewModel.filtroCursoId != null
            isCheckable = true
            setOnCheckedChangeListener { _, checked ->
                viewModel.filtroCursoId = if (checked) cursoId else null
                viewModel.aplicarFiltros()
            }
        }
        binding.layoutChips.addView(chip)
    }

    // Extensão útil para dp → px
    private val Int.dp get() = (this * resources.displayMetrics.density).toInt()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}