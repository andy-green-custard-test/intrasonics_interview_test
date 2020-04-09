package com.intrasonics.mobile.numericanalyser.feature.juliaset

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class JuliaGraphView : View {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var stepSize = Int.MAX_VALUE

    val convergentPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
    }
    lateinit var onDrawCallback: () -> Unit?

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            for (r in 0..canvas.width step stepSize) {
                for (i in 0..canvas.height step stepSize) {
                    if (isIn(r, i)) {
                        drawRect((r - stepSize / 2).toFloat(), // Chunk into squares of size stepSize to reduce the amount of computation required
                                (i - stepSize / 2).toFloat(),
                                (r + stepSize).toFloat(),
                                (i + stepSize).toFloat(),
                                convergentPaint)
                    }
                }
            }
        }

        onDrawCallback()
    }

    fun isIn(r: Int, i: Int): Boolean {
        return Random.nextBoolean()
    }

    /**
     * In principle, I would use data-binding here too - to avoid passing long parameters around - this this is quicker
     */
    fun updateParamsAndRedraw(stepSize: Int) {
        this.stepSize = stepSize
        this.invalidate()
    }
}
