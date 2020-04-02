package com.intrasonics.mobile.numericanalyser.di

import com.intrasonics.mobile.numericanalyser.feature.calculator.CalculatorActivity
import com.intrasonics.mobile.numericanalyser.feature.welcome.WelcomeActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class])
interface AppComponent {
    /**
     * All activities which use Dagger need to be explicitly listed here in order for dependency
     * resolution to work successfully
     */
    fun inject(target: WelcomeActivity)
    fun inject(target: CalculatorActivity)
}
