package com.intrasonics.mobile.numericanalyser.provider

import javax.inject.Inject

class StatisticsProviderImpl @Inject constructor() : StatisticsProvider {
    override fun analyseList(input: List<Number>): StatisticsProvider.Report {
        return StatisticsProvider.Report(1, 1) // TODO: (On a real project, I would cite a JIRA number here)
    }
}
