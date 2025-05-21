package com.didi.breathedeep.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.didi.breathedeep.R
import com.didi.breathedeep.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        setupListeners()
    }
    
    private fun setupUI() {
        // Setup bottom navigation
        binding.bottomNavigation.selectedItemId = R.id.navigation_home
    }
    
    private fun setupListeners() {
        // Setup session duration cards
        binding.card5min.setOnClickListener {
            startSession(5)
        }
        
        binding.card10min.setOnClickListener {
            startSession(10)
        }
        
        binding.card15min.setOnClickListener {
            startSession(15)
        }
        
        binding.card30min.setOnClickListener {
            startSession(30)
        }
        
        // Setup bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true
                R.id.navigation_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    finish()
                    true
                }
                R.id.navigation_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
    
    private fun startSession(durationMinutes: Int) {
        val intent = Intent(this, SessionActivity::class.java).apply {
            putExtra("DURATION_MINUTES", durationMinutes)
        }
        startActivity(intent)
    }
}
