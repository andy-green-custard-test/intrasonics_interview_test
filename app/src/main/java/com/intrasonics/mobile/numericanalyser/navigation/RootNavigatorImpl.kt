package com.intrasonics.mobile.numericanalyser.navigation

import android.content.Context
import android.content.Intent
import com.intrasonics.mobile.numericanalyser.feature.about.AboutActivity
import com.intrasonics.mobile.numericanalyser.feature.calculator.CalculatorActivity
import com.intrasonics.mobile.numericanalyser.feature.juliaset.JuliaActivity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This class follows the navigator pattern
 *
 * The principle here is the calling class should not know about the implementation details of what
 * it wishes to show. For example, it shouldn't really be necessary to refer to extra constants on
 * the destination class
 */

@Singleton
class RootNavigatorImpl @Inject constructor() : RootNavigator {
    override fun showCalculator(context: Context) {
        context.startActivity(Intent(context, CalculatorActivity::class.java))
    }

    override fun showJulia(context: Context) {
        context.startActivity(Intent(context, JuliaActivity::class.java))
    }

    override fun showAbout(context: Context) {
        context.startActivity(Intent(context, AboutActivity::class.java))
    }
}
