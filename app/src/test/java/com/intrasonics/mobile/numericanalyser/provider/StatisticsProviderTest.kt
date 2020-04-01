package com.intrasonics.mobile.numericanalyser.provider

import io.kotlintest.shouldBe
import javax.inject.Inject
import org.junit.Before
import org.junit.Test

class StatisticsProviderTest {
    @Inject
    lateinit var statisticsProvider: StatisticsProvider

    @Before
    fun before() {
        statisticsProvider = StatisticsProviderImpl()
    }

    @Test
    fun initialFailingTest() {
        statisticsProvider.analyseList(listOf()) shouldBe null // TODO (I wouldn't normally commit a TODO without a JIRA)
    }
}
