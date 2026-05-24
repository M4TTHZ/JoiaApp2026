package com.matheusramalho.joiaapp2026.ui.resultados

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.matheusramalho.joiaapp2026.data.model.ModalidadeResponse
import com.matheusramalho.joiaapp2026.databinding.FragmentResultadosBinding
import com.matheusramalho.joiaapp2026.utils.Resource

class ResultadosFragment : Fragment() {

    private var _binding: FragmentResultadosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ResultadosViewModel by viewModels {
        ResultadosViewModelFactory(requireContext())
    }

    private val adapter = ResultadosAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultadosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvResultados.layoutManager = LinearLayoutManager(requireContext())
        binding.rvResultados.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }

        observeData()
        viewModel.init()
    }

    private fun observeData() {
        viewModel.resultados.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvResultados.visibility = View.GONE
                    binding.tvEmpty.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    if (resource.data.isEmpty()) {
                        binding.rvResultados.visibility = View.GONE
                        binding.tvEmpty.visibility = View.VISIBLE
                    } else {
                        binding.tvEmpty.visibility = View.GONE
                        binding.rvResultados.visibility = View.VISIBLE
                        adapter.submitList(resource.data)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false
                    binding.tvEmpty.text = resource.message
                    binding.tvEmpty.visibility = View.VISIBLE
                }
            }
        }

        viewModel.modalidades.observe(viewLifecycleOwner) { modalidades ->
            construirChips(modalidades)
            // Passa o mapa id→nome para o adapter formatar a badge
            adapter.modalidadesMap = modalidades.associate { it.id to it.nome }
        }
    }

    private fun construirChips(modalidades: List<ModalidadeResponse>) {
        binding.layoutChips.removeAllViews()

        // Chip "Todos"
        adicionarChip("Todos", null)

        // Um chip por modalidade
        modalidades.forEach { mod -> adicionarChip(mod.nome, mod.id) }
    }

    private fun adicionarChip(label: String, modalidadeId: String?) {
        val chip = Chip(requireContext()).apply {
            text        = label
            isCheckable = true
            isChecked   = viewModel.filtroModalidadeId == modalidadeId
            setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    viewModel.filtroModalidadeId = modalidadeId
                    viewModel.aplicarFiltro()
                    // Garante que só 1 chip fica ativo
                    construirChips(viewModel.modalidades.value ?: emptyList())
                }
            }
        }
        binding.layoutChips.addView(chip)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}