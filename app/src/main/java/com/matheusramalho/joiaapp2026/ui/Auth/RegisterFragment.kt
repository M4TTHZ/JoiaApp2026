package com.matheusramalho.joiaapp2026.ui.Auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.matheusramalho.joiaapp2026.HomeActivity
import com.matheusramalho.joiaapp2026.data.model.CursoResponse
import com.matheusramalho.joiaapp2026.databinding.FragmentRegisterBinding
import com.matheusramalho.joiaapp2026.utils.Resource

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(requireContext())
    }

    private var cursosList: List<CursoResponse> = emptyList()
    private var cursoSelecionadoIndex: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadCursos()
        setupListeners()
        observeCursos()
        observeRegister()
    }

    private fun setupListeners() {
        binding.btnCadastrar.setOnClickListener {
            val cursoId = if (cursoSelecionadoIndex >= 0) cursosList[cursoSelecionadoIndex].id else ""
            viewModel.register(
                nome     = binding.etNome.text.toString(),
                email    = binding.etEmail.text.toString(),
                senha    = binding.etSenha.text.toString(),
                confirma = binding.etConfirma.text.toString(),
                cpf      = binding.etCpf.text.toString(),
                telefone = binding.etTelefone.text.toString(),
                cursoId  = cursoId
            )
        }
        binding.tvLogin.setOnClickListener { findNavController().popBackStack() }
    }

    private fun observeCursos() {
        viewModel.cursosState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> binding.tilCurso.isEnabled = false
                is Resource.Success -> {
                    cursosList = resource.data
                    val nomes = cursosList.map { "${it.nome} (${it.sigla})" }
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, nomes)
                    binding.actvCurso.setAdapter(adapter)
                    binding.tilCurso.isEnabled = true
                    binding.actvCurso.setOnItemClickListener { _, _, position, _ ->
                        cursoSelecionadoIndex = position
                    }
                }
                is Resource.Error -> {
                    binding.tilCurso.isEnabled = true
                    Snackbar.make(binding.root, resource.message, Snackbar.LENGTH_LONG)
                        .setAction("Tentar novamente") { viewModel.loadCursos() }.show()
                }
            }
        }
    }

    private fun observeRegister() {
        viewModel.registerState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> setLoading(true)
                is Resource.Success -> {
                    setLoading(false)
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                is Resource.Error -> {
                    setLoading(false)
                    Snackbar.make(binding.root, resource.message, Snackbar.LENGTH_LONG).setAction("OK") {}.show()
                }
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnCadastrar.isEnabled = !loading
        binding.etNome.isEnabled       = !loading
        binding.etEmail.isEnabled      = !loading
        binding.etSenha.isEnabled      = !loading
        binding.etConfirma.isEnabled   = !loading
        binding.etCpf.isEnabled        = !loading
        binding.etTelefone.isEnabled   = !loading
        binding.tilCurso.isEnabled     = !loading
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
