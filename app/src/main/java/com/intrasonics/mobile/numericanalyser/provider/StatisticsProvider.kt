package com.intrasonics.mobile.numericanalyser.provider

interface StatisticsProvider {
    fun analyseList(input: List<Number>): Report

    data class Report(val mean: Number, val median: Number)
}
