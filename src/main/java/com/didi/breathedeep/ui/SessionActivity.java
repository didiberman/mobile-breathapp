package com.didi.breathedeep.ui

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnRepeat
import androidx.core.view.ViewCompat
import com.didi.breathedeep.R
import com.didi.breathedeep.data.SessionManager
import com.didi.breathedeep.databinding.ActivitySessionBinding
import com.didi.breathedeep.util.AudioManager
import com.didi.breathedeep.util.BubbleView
import com.didi.breathedeep.util.LightRayView
import com.didi.breathedeep.util.RippleView
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class SessionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySessionBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var audioManager: AudioManager
    
    private var durationMinutes: Int = 5
    private var isPaused: Boolean = false
    private var timeRemaining: Long = 0
    
    private var countDownTimer: CountDownTimer? = null
    private var breathingAnimator: ValueAnimator? = null
    private var bubbleAnimators: MutableList<Animator> = mutableListOf()
    private var lightRayAnimator: AnimatorSet? = null
    private var rippleAnimator: ValueAnimator? = null
    
    // Views for enhanced animations
    private lateinit var bubbleViews: List<BubbleView>
    private lateinit var lightRayView: LightRayView
    private lateinit var rippleView: RippleView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySessionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        sessionManager = SessionManager(this)
        audioManager = AudioManager(this)
        
        // Get session duration from intent
        durationMinutes = intent.getIntExtra("DURATION_MINUTES", 5)
        timeRemaining = TimeUnit.MINUTES.toMillis(durationMinutes.toLong())
        
        setupUI()
        setupAnimationViews()
        setupListeners()
        startSession()
    }
    
    private fun setupUI() {
        updateTimerDisplay(timeRemaining)
    }
    
    private fun setupAnimationViews() {
        // Create and add bubble views
        val bubbleContainer = binding.bubbleContainer
        bubbleViews = List(15) { 
            BubbleView(this).apply {
                id = ViewCompat.generateViewId()
                alpha = Random.nextFloat() * 0.5f + 0.2f // 0.2 to 0.7 alpha
                val size = Random.nextInt(10, 40)
                layoutParams = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(size, size)
                bubbleContainer.addView(this)
            }
        }
        
        // Create and add light ray view
        lightRayView = LightRayView(this).apply {
            id = ViewCompat.generateViewId()
            layoutParams = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_PARENT,
                androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            binding.lightRayContainer.addView(this)
        }
        
        // Create and add ripple view
        rippleView = RippleView(this).apply {
            id = ViewCompat.generateViewId()
            layoutParams = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_PARENT,
                androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_PARENT
            )
            binding.rippleContainer.addView(this)
        }
    }
    
    private fun setupListeners() {
        binding.btnPauseResume.setOnClickListener {
            if (isPaused) {
                resumeSession()
            } else {
                pauseSession()
            }
        }
        
        binding.btnBack.setOnClickListener {
            showEndSessionDialog()
        }
    }
    
    private fun startSession() {
        // Start audio playback
        val audioResId = when (durationMinutes) {
            5 -> R.raw.meditation_5min
            10 -> R.raw.meditation_10min
            15 -> R.raw.meditation_15min
            30 -> R.raw.meditation_30min
            else -> R.raw.meditation_5min
        }
        audioManager.playAudio(audioResId)
        
        // Start all animations
        startBreathingAnimation()
        startBubbleAnimations()
        startLightRayAnimation()
        startRippleAnimation()
        
        // Start countdown timer
        startCountdownTimer(timeRemaining)
        
        // Record session start
        sessionManager.startSession(durationMinutes)
    }
    
    private fun pauseSession() {
        isPaused = true
        binding.btnPauseResume.text = getString(R.string.resume)
        binding.btnPauseResume.icon = getDrawable(R.drawable.ic_play)
        
        // Pause timer
        countDownTimer?.cancel()
        
        // Pause animations
        breathingAnimator?.pause()
        bubbleAnimators.forEach { it.pause() }
        lightRayAnimator?.pause()
        rippleAnimator?.pause()
        
        // Pause audio
        audioManager.pauseAudio()
    }
    
    private fun resumeSession() {
        isPaused = false
        binding.btnPauseResume.text = getString(R.string.pause)
        binding.btnPauseResume.icon = getDrawable(R.drawable.ic_pause)
        
        // Resume timer
        startCountdownTimer(timeRemaining)
        
        // Resume animations
        breathingAnimator?.resume()
        bubbleAnimators.forEach { it.resume() }
        lightRayAnimator?.resume()
        rippleAnimator?.resume()
        
        // Resume audio
        audioManager.resumeAudio()
    }
    
    private fun startCountdownTimer(millisUntilFinished: Long) {
        countDownTimer?.cancel()
        
        countDownTimer = object : CountDownTimer(millisUntilFinished, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                updateTimerDisplay(millisUntilFinished)
            }
            
            override fun onFinish() {
                completeSession()
            }
        }.start()
    }
    
    private fun updateTimerDisplay(millisUntilFinished: Long) {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - 
                      TimeUnit.MINUTES.toSeconds(minutes)
        
        binding.tvTimeRemaining.text = String.format("%02d:%02d", minutes, seconds)
    }
    
    private fun startBreathingAnimation() {
        // Create breathing circle animation
        breathingAnimator = ValueAnimator.ofFloat(0.8f, 1.2f).apply {
            duration = 10000 // 10 seconds per cycle
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            
            addUpdateListener { animator ->
                val value = animator.animatedValue as Float
                binding.viewBreathingCircle.scaleX = value
                binding.viewBreathingCircle.scaleY = value
            }
        }
        
        breathingAnimator?.start()
    }
    
    private fun startBubbleAnimations() {
        bubbleAnimators.clear()
        
        // Create and start bubble animations
        bubbleViews.forEach { bubbleView ->
            // Position bubbles at random horizontal positions at the bottom
            val startX = Random.nextFloat() * binding.bubbleContainer.width
            val startY = binding.bubbleContainer.height + bubbleView.height
            
            bubbleView.x = startX
            bubbleView.y = startY
            
            // Create vertical animation (bubbles rising)
            val yAnimator = ObjectAnimator.ofFloat(
                bubbleView, 
                "translationY", 
                startY, 
                -bubbleView.height.toFloat()
            ).apply {
                duration = Random.nextLong(15000, 30000) // 15-30 seconds to rise
                interpolator = LinearInterpolator()
                repeatCount = ValueAnimator.INFINITE
                
                // Reset horizontal position on repeat
                doOnRepeat {
                    bubbleView.x = Random.nextFloat() * binding.bubbleContainer.width
                }
            }
            
            // Create slight horizontal wobble
            val xAnimator = ObjectAnimator.ofFloat(
                bubbleView,
                "translationX",
                startX - 20f,
                startX + 20f
            ).apply {
                duration = Random.nextLong(2000, 5000) // 2-5 seconds per wobble
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
                interpolator = AccelerateDecelerateInterpolator()
            }
            
            // Create size pulsing animation
            val scaleAnimator = ObjectAnimator.ofFloat(
                bubbleView,
                "scaleX",
                0.8f,
                1.2f
            ).apply {
                duration = Random.nextLong(2000, 4000) // 2-4 seconds per pulse
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
                interpolator = AccelerateDecelerateInterpolator()
            }
            
            // Link X and Y scale
            val scaleYAnimator = ObjectAnimator.ofFloat(
                bubbleView,
                "scaleY",
                0.8f,
                1.2f
            ).apply {
                duration = scaleAnimator.duration
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
                interpolator = AccelerateDecelerateInterpolator()
            }
            
            // Combine all animations
            AnimatorSet().apply {
                playTogether(yAnimator, xAnimator, scaleAnimator, scaleYAnimator)
                start()
                bubbleAnimators.add(this)
            }
        }
    }
    
    private fun startLightRayAnimation() {
        // Create light ray animation
        val rotateAnimator = ObjectAnimator.ofFloat(
            lightRayView,
            "rotation",
            0f,
            360f
        ).apply {
            duration = 120000 // 2 minutes per full rotation
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
        }
        
        val alphaAnimator = ObjectAnimator.ofFloat(
            lightRayView,
            "alpha",
            0.2f,
            0.5f
        ).apply {
            duration = 10000 // 10 seconds per alpha cycle
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
        }
        
        lightRayAnimator = AnimatorSet().apply {
            playTogether(rotateAnimator, alphaAnimator)
            start()
        }
    }
    
    private fun startRippleAnimation() {
        // Create water ripple animation
        rippleAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 8000 // 8 seconds per ripple cycle
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            
            addUpdateListener { animator ->
                val value = animator.animatedValue as Float
                rippleView.setPhase(value)
                rippleView.invalidate()
            }
        }
        
        rippleAnimator?.start()
    }
    
    private fun completeSession() {
        // Record session completion
        sessionManager.completeSession()
        
        // Stop audio
        audioManager.stopAudio()
        
        // Navigate to completion screen
        val intent = Intent(this, SessionCompleteActivity::class.java).apply {
            putExtra("DURATION_MINUTES", durationMinutes)
            putExtra("TOTAL_MINUTES", sessionManager.getTotalMinutes())
        }
        startActivity(intent)
        finish()
    }
    
    private fun showEndSessionDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.end_session)
            .setMessage(R.string.confirm_end_session)
            .setPositiveButton(R.string.yes) { _, _ ->
                // Cancel timer
                countDownTimer?.cancel()
                
                // Stop animations
                breathingAnimator?.cancel()
                bubbleAnimators.forEach { it.cancel() }
                lightRayAnimator?.cancel()
                rippleAnimator?.cancel()
                
                // Stop audio
                audioManager.stopAudio()
                
                // Return to home screen
                finish()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }
    
    override fun onPause() {
        super.onPause()
        if (!isPaused) {
            pauseSession()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        breathingAnimator?.cancel()
        bubbleAnimators.forEach { it.cancel() }
        lightRayAnimator?.cancel()
        rippleAnimator?.cancel()
        audioManager.releaseAudio()
    }
}
