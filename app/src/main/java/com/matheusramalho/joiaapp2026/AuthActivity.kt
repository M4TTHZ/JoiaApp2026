package com.matheusramalho.joiaapp2026

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.matheusramalho.joiaapp2026.utils.SessionManager


class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Se já tem token salvo → vai direto pro app
        if (SessionManager(this).isLoggedIn()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_auth)
    }
}