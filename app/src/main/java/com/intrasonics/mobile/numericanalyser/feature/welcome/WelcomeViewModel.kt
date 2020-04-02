package com.intrasonics.mobile.numericanalyser.feature.welcome

import android.view.View
import com.intrasonics.mobile.numericanalyser.base.BaseViewModel
import com.intrasonics.mobile.numericanalyser.navigation.RootNavigator
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_welcome.view.*

/**
 * Top-level navigation page
 */
class WelcomeViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var navigator: RootNavigator

    fun onCalculatorButtonClick(view: View) {
        navigator.showCalculator(view.context)
    }

    fun onAboutButtonClick(view: View) {
        navigator.showAbout(view.context)
    }
}
