package com.didi.breathedeep.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.didi.breathedeep.R
import com.didi.breathedeep.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Delayed navigation to onboarding or home screen
        Handler(Looper.getMainLooper()).postDelayed({
            // Check if first launch
            val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
            val isFirstLaunch = prefs.getBoolean("is_first_launch", true)
            
            if (isFirstLaunch) {
                // First launch, show onboarding
                startActivity(Intent(this, OnboardingActivity::class.java))
                prefs.edit().putBoolean("is_first_launch", false).apply()
            } else {
                // Not first launch, go directly to home
                startActivity(Intent(this, HomeActivity::class.java))
            }
            
            finish()
        }, SPLASH_DELAY)
    }
    
    companion object {
        private const val SPLASH_DELAY = 1500L // 1.5 seconds
    }
}
