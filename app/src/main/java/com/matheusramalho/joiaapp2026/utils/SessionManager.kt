package com.matheusramalho.joiaapp2026.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "jogos_uni_prefs"
        private const val KEY_TOKEN  = "jwt_token"
        private const val KEY_NOME   = "user_nome"
        private const val KEY_EMAIL  = "user_email"
        private const val KEY_ROLE   = "user_role"
        private const val KEY_CURSO  = "user_curso_id"
    }

    fun saveSession(token: String, nome: String, email: String, role: String, cursoId: String) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_NOME, nome)
            .putString(KEY_EMAIL, email)
            .putString(KEY_ROLE, role)
            .putString(KEY_CURSO, cursoId)
            .apply()
    }

    fun getToken(): String?   = prefs.getString(KEY_TOKEN, null)
    fun getNome(): String?    = prefs.getString(KEY_NOME, null)
    fun getEmail(): String?   = prefs.getString(KEY_EMAIL, null)
    fun getRole(): String?    = prefs.getString(KEY_ROLE, null)
    fun getCursoId(): String? = prefs.getString(KEY_CURSO, null)

    fun isLoggedIn(): Boolean = !getToken().isNullOrBlank()
    fun clearSession() = prefs.edit().clear().apply()
}
