package com.intrasonics.mobile.numericanalyser.base

import android.app.Activity
import android.os.Bundle

abstract class BaseActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as NumericAnalyserApplication).numericAnalyserComponent.inject(this)
    }
}
