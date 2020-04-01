package com.intrasonics.mobile.numericanalyser.di

import com.intrasonics.mobile.numericanalyser.base.BaseActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(target: BaseActivity)
}
