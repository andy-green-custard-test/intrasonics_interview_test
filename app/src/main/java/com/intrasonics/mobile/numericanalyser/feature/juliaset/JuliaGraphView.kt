package com.intrasonics.mobile.numericanalyser.feature.juliaset

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.random.Random

class JuliaGraphView : View {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val convergentPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
    }
    val divergentPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
    }

    val precomputeExector = Executors.newSingleThreadExecutor()
    private var presentFuture: Future<*>? = null // Future for any current rendering operation
    var bitmap: Bitmap? = null // Canvas for pre-rendering

    val mainThreadHandler = Handler(Looper.getMainLooper())

    lateinit var onDrawCallback: () -> Unit?
    lateinit var onResizeCallback: () -> Unit?

    override fun onDraw(canvas: Canvas?) {
        if (presentFuture != null)
            return // abort, since bitmap is being mutated  - note: this.invalidate() is guaranteed to be called when it finishes

        canvas?.apply {
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            onDrawCallback()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444) // Ideally would define this a 1 bit/pixel
        // TODO: fix obvious thread safety problem
        onResizeCallback()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    fun isIn(r: Int, i: Int): Boolean {
        return Random.nextBoolean()
    }

    /**
     * In principle, I would use data-binding here too - to avoid passing long parameters around - but this is quicker
     */
    fun updateParamsAndRedraw(stepSize: Int) {

        presentFuture?.cancel(false)

        presentFuture = precomputeExector.submit {
            try {
                val canvas = Canvas(bitmap)
                canvas.drawRect(0f, 0f, bitmap!!.width.toFloat(), bitmap!!.height.toFloat(), divergentPaint) // Clear canvas

                for (r in 0..bitmap!!.width step stepSize) {
                    for (i in 0..bitmap!!.height step stepSize) {
                        if (isIn(r, i)) {
                            canvas.drawRect(r.toFloat(), // Chunk into squares of size stepSize to reduce the amount of computation required
                                    i.toFloat(),
                                    (r + stepSize).toFloat(),
                                    (i + stepSize).toFloat(),
                                    convergentPaint)
                        }
                    }
                }

                mainThreadHandler.post {
                    presentFuture = null
                    this.invalidate()
                }
            } catch (interruptedException: InterruptedException) {
                println("Was cancelled")
                // Too late, a new render has been scheduled, drop render
            }
        }
    }

    override fun onDetachedFromWindow() {
        precomputeExector.shutdown()
        super.onDetachedFromWindow()
    }
}
