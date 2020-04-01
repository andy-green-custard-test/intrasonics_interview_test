package com.intrasonics.mobile.numericanalyser.di

import com.intrasonics.mobile.numericanalyser.navigation.RootNavigator
import com.intrasonics.mobile.numericanalyser.navigation.RootNavigatorImpl
import com.intrasonics.mobile.numericanalyser.provider.StatisticsProvider
import com.intrasonics.mobile.numericanalyser.provider.StatisticsProviderImpl
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {
    @Binds
    abstract fun provideRootNavigator(rootNavigatorImpl: RootNavigatorImpl): RootNavigator

    @Binds
    abstract fun provideStatisticsProvider(statisticsProvider: StatisticsProviderImpl): StatisticsProvider
}
