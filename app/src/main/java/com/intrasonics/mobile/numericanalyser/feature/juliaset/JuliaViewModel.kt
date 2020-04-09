package com.intrasonics.mobile.numericanalyser.feature.juliaset

import android.arch.lifecycle.MutableLiveData
import android.databinding.Observable
import android.databinding.ObservableField
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
const val STEP_SIZE_MAXIMUM_PX = 128

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

    enum class Action {
        REDRAW
    }

    private var callback: Observable.OnPropertyChangedCallback

    init {
        callback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                stepSizePx = STEP_SIZE_MAXIMUM_PX // Jump to worst quality as soon as a user makes any change. This is to improve responsiveness
                action.value = Action.REDRAW
            }
        }

        real.addOnPropertyChangedCallback(callback)
        imaginary.addOnPropertyChangedCallback(callback)
        realScaleMin.addOnPropertyChangedCallback(callback)
        realScaleMax.addOnPropertyChangedCallback(callback)
        imaginaryScaleMin.addOnPropertyChangedCallback(callback)
        imaginaryScaleMax.addOnPropertyChangedCallback(callback)
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
            action.value = Action.REDRAW
        }
    }

    // If viewport changes (even just the keyboard moving and causing the layout to update), then we need to start again - since the bitmap cache is no longer a suitable size
    fun onResizeGraph() {
        callback.onPropertyChanged(null, 0)
    }

    fun onDestroy() {
        real.removeOnPropertyChangedCallback(callback)
        imaginary.removeOnPropertyChangedCallback(callback)
        realScaleMin.removeOnPropertyChangedCallback(callback)
        realScaleMax.removeOnPropertyChangedCallback(callback)
        imaginaryScaleMin.removeOnPropertyChangedCallback(callback)
        imaginaryScaleMax.removeOnPropertyChangedCallback(callback)
    }
}
