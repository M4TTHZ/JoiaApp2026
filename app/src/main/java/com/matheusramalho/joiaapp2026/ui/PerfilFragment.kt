package com.matheusramalho.joiaapp2026.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.matheusramalho.joiaapp2026.AuthActivity
import com.matheusramalho.joiaapp2026.data.model.UserResponse
import com.matheusramalho.joiaapp2026.data.repository.AuthRepository
import com.matheusramalho.joiaapp2026.databinding.FragmentPerfilBinding
import com.matheusramalho.joiaapp2026.ui.perfil.PerfilViewModel
import com.matheusramalho.joiaapp2026.utils.Resource
import com.matheusramalho.joiaapp2026.utils.SessionManager

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PerfilViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(c: Class<T>): T =
                PerfilViewModel(AuthRepository(requireContext())) as T
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        viewModel.loadPerfil()

        binding.btnLogout.setOnClickListener {
            SessionManager(requireContext()).clearSession()
            startActivity(Intent(requireContext(), AuthActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }

    private fun observeData() {
        viewModel.perfil.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    preencherPerfil(resource.data)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, resource.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }

        viewModel.updateState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> Snackbar.make(binding.root, resource.data, Snackbar.LENGTH_SHORT).show()
                is Resource.Error   -> Snackbar.make(binding.root, resource.message, Snackbar.LENGTH_LONG).show()
                else -> Unit
            }
        }
    }

    private fun preencherPerfil(user: UserResponse) {
        val nome = user.nome.ifBlank { "Usuário" }
        binding.tvNome.text    = nome
        binding.tvEmail.text   = user.email
        binding.tvCpf.text     = user.cpf ?: "Não informado"
        binding.tvTelefone.text = user.telefone ?: "Não informado"
        binding.tvRole.text    = when(user.role.uppercase()) {
            "ADMIN"  -> "Administrador"
            "ATLETA" -> "Atleta"
            else     -> user.role
        }
        binding.tvCurso.text   = user.curso?.nome ?: "Sem curso"
        binding.tvAvatar.text  = nome.firstOrNull()?.uppercase() ?: "U"

        val membro = try {
            val iso = user.criadoEm ?: return
            val zdt = java.time.ZonedDateTime.parse(iso)
            "Membro desde ${zdt.format(java.time.format.DateTimeFormatter.ofPattern("MM/yyyy"))}"
        } catch (_: Exception) { "" }
        binding.tvMembro.text = membro
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
