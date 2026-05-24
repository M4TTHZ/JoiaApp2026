package com.matheusramalho.joiaapp2026.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.matheusramalho.joiaapp2026.utils.SessionManager

class PerfilFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tv = TextView(requireContext()).apply {
            val session = SessionManager(requireContext())
            text = "Perfil de ${session.getNome() ?: "Usuário"}\n${session.getRole() ?: ""}"
            textSize = 16f
            setPadding(48, 48, 48, 48)
        }
        return tv
    }
}