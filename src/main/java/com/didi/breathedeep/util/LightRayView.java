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
 * Custom view that renders light ray effects from the top of the screen
 */
class LightRayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.WHITE
        alpha = 80 // Semi-transparent
        style = Paint.Style.STROKE
        strokeWidth = 5f
        isAntiAlias = true
    }
    
    private val rayPaths = mutableListOf<Path>()
    private val rayCount = 12
    
    init {
        // Initialize ray paths
        for (i in 0 until rayCount) {
            rayPaths.add(Path())
        }
    }
    
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateRayPaths()
    }
    
    private fun updateRayPaths() {
        val width = width.toFloat()
        val height = height.toFloat()
        
        // Create ray paths
        for (i in 0 until rayCount) {
            val path = rayPaths[i]
            path.reset()
            
            // Calculate ray start position (evenly distributed across top)
            val startX = (width / (rayCount + 1)) * (i + 1)
            val startY = 0f
            
            // Calculate ray end position (converging toward center bottom)
            val endX = width / 2f + (width / 4f) * cos(Math.toRadians((i * 30).toDouble())).toFloat()
            val endY = height * 0.7f
            
            // Draw ray
            path.moveTo(startX, startY)
            path.lineTo(endX, endY)
        }
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        // Draw each ray with varying opacity
        for (i in 0 until rayCount) {
            // Vary opacity for each ray
            val alpha = (40 + (i % 3) * 20).coerceIn(40, 100)
            paint.alpha = alpha
            paint.strokeWidth = 2f + (i % 4)
            
            canvas.drawPath(rayPaths[i], paint)
        }
    }
}
