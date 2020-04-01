package com.intrasonics.mobile.numericanalyser.base

import android.app.Application
import com.intrasonics.mobile.numericanalyser.di.AppComponent
import com.intrasonics.mobile.numericanalyser.di.DaggerAppComponent

class NumericAnalyserApplication : Application() {
    lateinit var numericAnalyserComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        numericAnalyserComponent = DaggerAppComponent.builder().build()
    }
}
