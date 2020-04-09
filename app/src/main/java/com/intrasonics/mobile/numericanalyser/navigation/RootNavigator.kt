package com.intrasonics.mobile.numericanalyser.navigation

import android.content.Context

interface RootNavigator {
    fun showCalculator(context: Context)
    fun showAbout(context: Context)
    fun showJulia(context: Context)
}
