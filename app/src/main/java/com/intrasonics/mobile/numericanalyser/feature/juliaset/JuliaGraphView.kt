package com.intrasonics.mobile.numericanalyser.feature.juliaset

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View

class JuliaGraphView : View {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val convergentPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {

            drawRect(0f, 0f, (System.currentTimeMillis() % 1000).toFloat(), (System.currentTimeMillis() % 1000).toFloat(), convergentPaint)
        }
    }
}
