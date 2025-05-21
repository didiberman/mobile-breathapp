package com.didi.breathedeep.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

/**
 * Custom view that renders water ripple effects
 */
class RippleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.WHITE
        alpha = 80 // Semi-transparent
        style = Paint.Style.STROKE
        strokeWidth = 2f
        isAntiAlias = true
    }
    
    private val ripplePath = Path()
    private var phase = 0f
    private val rippleCount = 5
    private val waveAmplitude = 8f
    
    /**
     * Sets the current phase of the ripple animation (0-1)
     */
    fun setPhase(phase: Float) {
        this.phase = phase
        invalidate()
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        
        // Draw multiple ripple circles with wave effect
        for (i in 0 until rippleCount) {
            val radius = 50f + (i * 60f) + (phase * 100f)
            
            ripplePath.reset()
            
            // Create wavy circle path
            for (angle in 0..360 step 5) {
                val radian = Math.toRadians(angle.toDouble())
                val waveOffset = waveAmplitude * sin(radian * 8 + phase * Math.PI * 2).toFloat()
                val x = centerX + (radius + waveOffset) * cos(radian).toFloat()
                val y = centerY + (radius + waveOffset) * sin(radian).toFloat()
                
                if (angle == 0) {
                    ripplePath.moveTo(x, y)
                } else {
                    ripplePath.lineTo(x, y)
                }
            }
            
            ripplePath.close()
            
            // Adjust opacity based on radius (fade out as they expand)
            val alphaFactor = 1f - (radius / (rippleCount * 100f + 100f))
            paint.alpha = (80 * alphaFactor).toInt()
            
            canvas.drawPath(ripplePath, paint)
        }
    }
}
