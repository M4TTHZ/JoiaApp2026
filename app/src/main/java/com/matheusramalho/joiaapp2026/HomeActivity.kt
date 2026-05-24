package com.matheusramalho.joiaapp2026

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.matheusramalho.joiaapp2026.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
