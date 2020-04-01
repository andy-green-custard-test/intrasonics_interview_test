package com.intrasonics.mobile.numericanalyser.feature.welcome

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.intrasonics.mobile.numericanalyser.R
import com.intrasonics.mobile.numericanalyser.base.NumericAnalyserApplication
import com.intrasonics.mobile.numericanalyser.databinding.ActivityWelcomeBinding
import javax.inject.Inject

class WelcomeActivity : Activity() {

    @Inject
    lateinit var viewModel: WelcomeViewModel

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
        val binding = DataBindingUtil.setContentView<ActivityWelcomeBinding>(this, R.layout.activity_welcome)
        binding.viewModel = viewModel
    }
}
