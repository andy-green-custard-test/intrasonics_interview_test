package com.intrasonics.mobile.numericanalyser.provider

class StatisticsProviderImpl : StatisticsProvider {
    override fun analyseList(input: List<Number>): StatisticsProvider.Report {
        return StatisticsProvider.Report(1, 1) // TODO: (On a real project, I would cite a JIRA number here)
    }
}
