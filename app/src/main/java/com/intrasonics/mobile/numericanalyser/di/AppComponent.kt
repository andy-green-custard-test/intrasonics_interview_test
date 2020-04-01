package com.intrasonics.mobile.numericanalyser.di

import com.intrasonics.mobile.numericanalyser.feature.welcome.WelcomeActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class])
interface AppComponent {
    fun inject(target: WelcomeActivity)
}
