package com.intrasonics.mobile.numericanalyser.feature.juliaset

import android.arch.lifecycle.MutableLiveData
import android.databinding.Observable
import android.databinding.ObservableField
import com.intrasonics.mobile.numericanalyser.base.BaseViewModel
import com.intrasonics.mobile.numericanalyser.navigation.RootNavigator
import javax.inject.Inject

class JuliaViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var navigator: RootNavigator

    val real = ObservableField<String>("0.0")
    val imaginary = ObservableField<String>("0.0")

    val realScaleMin = ObservableField<String>("-2.0")
    val realScaleMax = ObservableField<String>("2.0")
    val imaginaryScaleMin = ObservableField<String>("-2.0")
    val imaginaryScaleMax = ObservableField<String>("2.0")

    val action = MutableLiveData<Action>()

    enum class Action {
        REDRAW
    }

    private var callback: Observable.OnPropertyChangedCallback

    init {
        callback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
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

    fun onDestroy() {
        real.removeOnPropertyChangedCallback(callback)
        imaginary.removeOnPropertyChangedCallback(callback)
        realScaleMin.removeOnPropertyChangedCallback(callback)
        realScaleMax.removeOnPropertyChangedCallback(callback)
        imaginaryScaleMin.removeOnPropertyChangedCallback(callback)
        imaginaryScaleMax.removeOnPropertyChangedCallback(callback)
    }
}
