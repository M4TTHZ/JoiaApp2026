package com.matheusramalho.joiaapp2026.ui.Auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.matheusramalho.joiaapp2026.HomeActivity
import com.matheusramalho.joiaapp2026.R
import com.matheusramalho.joiaapp2026.databinding.FragmentLoginBinding
import com.matheusramalho.joiaapp2026.utils.Resource


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeLoginState()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            viewModel.login(
                email = binding.etEmail.text.toString(),
                senha = binding.etSenha.text.toString()
            )
        }

        binding.tvCadastro.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun observeLoginState() {
        viewModel.loginState.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> setLoading(true)

                is Resource.Success -> {
                    setLoading(false)
                    // Limpa a back-stack e abre o app principal
                    val intent = Intent(requireContext(), HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }

                is Resource.Error -> {
                    setLoading(false)
                    Snackbar.make(binding.root, resource.message, Snackbar.LENGTH_LONG)
                        .setAction("OK") {}
                        .show()
                }
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled     = !loading
        binding.etEmail.isEnabled      = !loading
        binding.etSenha.isEnabled      = !loading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // evita memory leak
    }
}