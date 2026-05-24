package com.matheusramalho.joiaapp2026

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.matheusramalho.joiaapp2026.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Conecta NavController com o BottomNavigationView
        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_home) as NavHostFragment
        navController = navHost.navController

        // setupWithNavController cuida de: highlight do item ativo,
        // back stack limpo ao trocar de aba e seleção ao pressionar o item ativo
        binding.bottomNav.setupWithNavController(navController)
    }
}