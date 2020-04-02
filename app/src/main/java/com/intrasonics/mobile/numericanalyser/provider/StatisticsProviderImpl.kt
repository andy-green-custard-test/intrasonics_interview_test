package com.intrasonics.mobile.numericanalyser.provider

import android.support.annotation.VisibleForTesting
import java.lang.IllegalArgumentException
import java.lang.Math.abs
import javax.inject.Inject

class StatisticsProviderImpl @Inject constructor() : StatisticsProvider {
    override fun parseList(rawInput: String): StatisticsProvider.ParseResult {
        val regex = """[\s,;:\r\n\-]+"""
        val rawList = rawInput.trim().split(Regex(regex))
        val numbers = rawList.map { it.toDoubleOrNull() }

        return when {
            rawInput.isBlank() -> {
                StatisticsProvider.ParseResult.Empty
            }
            numbers.contains(null) -> {
                StatisticsProvider.ParseResult.Invalid // Contains something that couldn't be parsed
            }
            else -> {
                StatisticsProvider.ParseResult.Success(numbers.filterNotNull())
            }
        }
    }

    override fun analyseList(input: List<Double>): StatisticsProvider.Report {
        val mean = input.mean()
        val median = input.median()
        return StatisticsProvider.Report(input, mean, median)
    }
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun List<Double>.median(): Double {
    val n = this.size
    return when {
        n == 0 -> {
            throw IllegalArgumentException("Must provide at least one number")
        }
        n.isOdd() -> {
            this.sorted()[n / 2]
        }
        else -> { // Even
            /* When there are an even number of elements, I following the convention where you
             *take the mean of the middle two */
            val sorted = this.sorted()
            val low = sorted[(n / 2) - 1]
            val high = sorted[(n / 2)]
            return (low + high) / 2.0
        }
    }
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun List<Double>.mean(): Double {
    return this.sum() / this.size
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun Int.isOdd(): Boolean {
    return abs(this) % 2 == 1
}
