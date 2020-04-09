package com.intrasonics.mobile.numericanalyser.feature.juliaset

import android.arch.lifecycle.MutableLiveData
import android.databinding.Observable
import android.databinding.ObservableField
import android.view.View
import com.intrasonics.mobile.numericanalyser.base.BaseViewModel
import com.intrasonics.mobile.numericanalyser.navigation.RootNavigator
import javax.inject.Inject

/**
 * On first pass, instead of calculating whether each pixel is "in", it instead calculates whether
 * each larger square is in. This constant defines the largest square we can draw without it being
 * ridiculous
 *
 * Should be a power of 2. Should be (on average) significantly smaller than the expected viewport
 * size
 */
const val STEP_SIZE_MAXIMUM_PX = 32

const val PAN_MULTIPLIER = 0.25f // 25% of the screen width/height per click
const val ZOOM_MULTIPLIER = 1.25f // 25% bigger

/**
 * ViewModel to provide input to / control JuliaGraphView
 */
class JuliaViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var navigator: RootNavigator

    val real = ObservableField<String>("0.0")
    val imaginary = ObservableField<String>("0.0")

    val realScaleMin = ObservableField<String>("-2.0")
    val realScaleMax = ObservableField<String>("2.0")
    val imaginaryScaleMin = ObservableField<String>("-2.0")
    val imaginaryScaleMax = ObservableField<String>("2.0")

    /**
     * I thought about trying to replicate the way Google Maps caches tiles.
     *
     * But then I thought it might be easier to use progressive loading - and have more predictable results
     */
    var stepSizePx = STEP_SIZE_MAXIMUM_PX

    val action = MutableLiveData<Action>()

    sealed class Action {
        data class REDRAW(val stepSize: Int, val graphRules: GraphRules) : Action()
    }

    private var callback: Observable.OnPropertyChangedCallback

    init {
        callback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                onGraphChanged()
            }
        }

        bind()
    }

    private fun bind() {
        real.addOnPropertyChangedCallback(callback)
        imaginary.addOnPropertyChangedCallback(callback)
        realScaleMin.addOnPropertyChangedCallback(callback)
        realScaleMax.addOnPropertyChangedCallback(callback)
        imaginaryScaleMin.addOnPropertyChangedCallback(callback)
        imaginaryScaleMax.addOnPropertyChangedCallback(callback)
    }

    /**
     * Turn the form data into a convenient form for passing around
     */
    private fun graphRules(): GraphRules { // I'd actually do null handling properly if I was doing this at a sensible speed:quality ratio
        return GraphRules(real.getFloatOrZero(),
                imaginary.getFloatOrZero(),
                3f,
                5,
                realScaleMin.getFloatOrZero(),
                realScaleMax.getFloatOrZero(),
                imaginaryScaleMin.getFloatOrZero(),
                imaginaryScaleMax.getFloatOrZero())
    }

    /**
     * Each time we successfully render, attempt to do so at twice the resolution
     *
     * As soon as the user changes anything, jump to the worst quality
     */
    fun onCompleteRender() {
        /*Note: it's entirely possible that during the render, the step size has been reset to maximum - which is technically a glitch,
            since we'll effectively skip the very worst step size*/
        if (stepSizePx > 1) {
            stepSizePx /= 2
            action.value = Action.REDRAW(stepSizePx, graphRules())
        }
    }

    // If viewport changes (even just the keyboard moving and causing the layout to update), then we need to start again - since the bitmap cache is no longer a suitable size
    fun onResizeGraph() {
        onGraphChanged()
    }

    private fun onGraphChanged() {
        stepSizePx = STEP_SIZE_MAXIMUM_PX // Jump to worst quality as soon as a user makes any change. This is to improve responsiveness
        action.value = Action.REDRAW(stepSizePx, graphRules())
    }

    /*** Navigation ***/

    /* Pan left defined as "make the pretty thing move left" - therefore ADD to the start and end values */
    fun onPanLeftClick(view: View) {
        val rangeX = realScaleMax.getFloatOrZero() - realScaleMin.getFloatOrZero()
        realScaleMin.setFloat(realScaleMin.getFloatOrZero() + rangeX * PAN_MULTIPLIER)
        realScaleMax.setFloat(realScaleMax.getFloatOrZero() + rangeX * PAN_MULTIPLIER)
    }
    fun onPanRightClick(view: View) {
        val rangeX = realScaleMax.getFloatOrZero() - realScaleMin.getFloatOrZero()
        realScaleMin.setFloat(realScaleMin.getFloatOrZero() - rangeX * PAN_MULTIPLIER)
        realScaleMax.setFloat(realScaleMax.getFloatOrZero() - rangeX * PAN_MULTIPLIER)
    }
    fun onPanUpClick(view: View) {
        val rangeY = imaginaryScaleMax.getFloatOrZero() + imaginaryScaleMin.getFloatOrZero()
        imaginaryScaleMin.setFloat(imaginaryScaleMin.getFloatOrZero() + rangeY * PAN_MULTIPLIER)
        imaginaryScaleMax.setFloat(imaginaryScaleMax.getFloatOrZero() + rangeY * PAN_MULTIPLIER)
    }
    fun onPanDownClick(view: View) {
        val rangeY = imaginaryScaleMax.getFloatOrZero() - imaginaryScaleMin.getFloatOrZero()
        imaginaryScaleMin.setFloat(imaginaryScaleMin.getFloatOrZero() - rangeY * PAN_MULTIPLIER)
        imaginaryScaleMax.setFloat(imaginaryScaleMax.getFloatOrZero() - rangeY * PAN_MULTIPLIER)
    }

    /**
     * Zooms with respect to the origin of the graph - it's not exactly hard to do what you'd want it to do - it's just 2 more transforms - but then a pinch zoom would be better anyway :-)
     */
    fun onZoomInClick(view: View) {
        realScaleMin.setFloat(realScaleMin.getFloatOrZero() * ZOOM_MULTIPLIER)
        realScaleMax.setFloat(realScaleMax.getFloatOrZero() * ZOOM_MULTIPLIER)
        imaginaryScaleMin.setFloat(imaginaryScaleMin.getFloatOrZero() * ZOOM_MULTIPLIER)
        imaginaryScaleMax.setFloat(imaginaryScaleMax.getFloatOrZero() * ZOOM_MULTIPLIER)
    }
    fun onZoomOutClick(view: View) {
        realScaleMin.setFloat(realScaleMin.getFloatOrZero() / ZOOM_MULTIPLIER)
        realScaleMax.setFloat(realScaleMax.getFloatOrZero() / ZOOM_MULTIPLIER)
        imaginaryScaleMin.setFloat(imaginaryScaleMin.getFloatOrZero() / ZOOM_MULTIPLIER)
        imaginaryScaleMax.setFloat(imaginaryScaleMax.getFloatOrZero() / ZOOM_MULTIPLIER)
    }

    /*** Teardown ***/
    // Because this is outside of the managed Android lifecycle, we have to do our own teardown
        fun onDestroy() {
        real.removeOnPropertyChangedCallback(callback)
        imaginary.removeOnPropertyChangedCallback(callback)
        realScaleMin.removeOnPropertyChangedCallback(callback)
        realScaleMax.removeOnPropertyChangedCallback(callback)
        imaginaryScaleMin.removeOnPropertyChangedCallback(callback)
        imaginaryScaleMax.removeOnPropertyChangedCallback(callback)
    }
}

/*** Convenience methods to go from primitives to Android data-binding constructs ***/
private fun ObservableField<String>.setFloat(value: Float) {
    this.set(value.toString())
}

private fun ObservableField<String>.getFloatOrZero(): Float {
    return this.get().toFloatOrZero()
}

/**
 * Sure, 0 is almost certainly the wrong value. But this approach allows the graph just disappear when the user puts in invalid, rather than crash out immediately.
 */
private fun String?.toFloatOrZero(): Float {
    return this?.toFloatOrNull() ?: 0f
}
