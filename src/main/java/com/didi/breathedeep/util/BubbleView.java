package com.didi.breathedeep.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

/**
 * Custom view that renders bubble effects for the underwater theme
 */
class BubbleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.WHITE
        alpha = 80 // Semi-transparent
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    
    private val strokePaint = Paint().apply {
        color = Color.WHITE
        alpha = 120 // More opaque for the stroke
        style = Paint.Style.STROKE
        strokeWidth = 1f
        isAntiAlias = true
    }
    
    // Random shine position for each bubble
    private val shineAngle = Random.nextFloat() * 45f + 45f // 45-90 degrees
    private val shineSize = Random.nextFloat() * 0.2f + 0.1f // 10-30% of bubble size
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = Math.min(width, height) / 2f - 1f
        
        // Draw main bubble
        canvas.drawCircle(centerX, centerY, radius, paint)
        canvas.drawCircle(centerX, centerY, radius, strokePaint)
        
        // Draw shine highlight
        val shineX = centerX + radius * 0.7f * Math.cos(Math.toRadians(shineAngle.toDouble())).toFloat()
        val shineY = centerY - radius * 0.7f * Math.sin(Math.toRadians(shineAngle.toDouble())).toFloat()
        val shinePaint = Paint(paint)
        shinePaint.alpha = 180
        canvas.drawCircle(shineX, shineY, radius * shineSize, shinePaint)
    }
}
