package com.intrasonics.mobile.numericanalyser.feature.calculator

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.intrasonics.mobile.numericanalyser.R
import com.intrasonics.mobile.numericanalyser.base.NumericAnalyserApplication
import com.intrasonics.mobile.numericanalyser.databinding.ActivityCalculatorBinding
import javax.inject.Inject

const val KEY_RAW_INPUT = "KEY_RAW_INPUT"

class CalculatorActivity : Activity() {
    @Inject
    lateinit var viewModel: CalculatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as NumericAnalyserApplication).numericAnalyserComponent.inject(this) // Dagger setup. See WelcomeActivity for more info

        /**
         * Restore any user input if appropriate (e.g. if OS kills activity while paused or if
         * device is rotated)
         */
        viewModel.rawInput.set(savedInstanceState?.getString(KEY_RAW_INPUT))

        val binding = DataBindingUtil.setContentView<ActivityCalculatorBinding>(this, R.layout.activity_calculator) // Android Data-Binding - see WelcomeActivity for more info
        binding.viewModel = viewModel
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(KEY_RAW_INPUT, viewModel.rawInput.get())
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()
    }
}
