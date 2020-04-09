package com.intrasonics.mobile.numericanalyser.feature.juliaset

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.intrasonics.mobile.numericanalyser.R
import com.intrasonics.mobile.numericanalyser.base.NumericAnalyserApplication
import com.intrasonics.mobile.numericanalyser.databinding.ActivityJuliaBinding
import javax.inject.Inject

class JuliaActivity : Activity() {

    @Inject
    lateinit var viewModel: JuliaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Supplies Dagger dependencies - both the @Inject above and transitives ones.
         *
         * A future improvement would be to decouple the lifecycle better - for example, a rotation
         * would cause the view model to be recreated for no reason
         */
        (application as NumericAnalyserApplication).numericAnalyserComponent.inject(this)

        /**
         * Android data-binding setup. Note this needs to happen after Dagger is ready
         */
        val binding = DataBindingUtil.setContentView<ActivityJuliaBinding>(this, R.layout.activity_julia)
        binding.viewModel = viewModel
    }
}
