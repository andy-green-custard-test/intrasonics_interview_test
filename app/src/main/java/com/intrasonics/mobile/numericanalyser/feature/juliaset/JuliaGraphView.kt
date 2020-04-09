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

    fun isIn(r: Float, i: Float, graphRules: GraphRules): Boolean {
        val limit = graphRules.veryBig * graphRules.veryBig // Avoid square root at all costs (powers more expensive than multiplying). Also avoid repeated operations

        var r = r // Need mutable versions
        var i = i
        for (iteration in 1..graphRules.maximumIterationsToGetBig) {
            // z = a + bj -> z2 = a2 - b2 + 2ab -> zr = a2 - b2 && zj = 2ab
            val oldR = r // We need to remember the value BEFORE the iteration in order to correctly calculate i
            r = r * r - i * i + graphRules.real // I believe multiplication is faster than use of power 2
            i = 2 * i * oldR

            if (i * i + r * r > limit) { // Multiplication faster than power
                return false // It's diverged
            }
        }
        return true
    }

    /**
     * In principle, I would use data-binding here too - to avoid passing long parameters around - but this is quicker
     *
     * Pre-compute using bitmap on another thread ... clearly the use of a proper mutex, or possibly
     * two alternating mutable bitmap buffers ... or something is required to fix the thread safety
     * issue. Note that rapidly changing the inputs can cause a wierd issue where the graph
     * gradually shrinks to width 1
     *
     * I did try creating a new bitmap each time, the difference in performance was severe
     *
     * I would actually benchmark properly and measure
     */
    fun updateParamsAndRedraw(stepSizePx: Int, graphRules: GraphRules) {

        presentFuture?.cancel(false)

        presentFuture = precomputeExector.submit {
            try {
                val canvas = Canvas(bitmap)
                canvas.drawRect(0f, 0f, bitmap!!.width.toFloat(), bitmap!!.height.toFloat(), divergentPaint) // Clear canvas

                for (x in 0..bitmap!!.width step stepSizePx) {
                    val r = graphRules.minReal + (graphRules.maxReal - graphRules.minReal) * x / bitmap!!.width
                    for (y in 0..bitmap!!.height step stepSizePx) {
                        val i = graphRules.minImaginary + (graphRules.maxImaginary - graphRules.minImaginary) * y / bitmap!!.height
                        if (isIn(r, i, graphRules)) {
                            canvas.drawRect(x.toFloat(), // Chunk into squares of size stepSize to reduce the amount of computation required
                                    y.toFloat(),
                                    (x + stepSizePx).toFloat(),
                                    (y + stepSizePx).toFloat(),
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
