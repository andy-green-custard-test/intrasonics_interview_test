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

    /*** analyseList ***/
    @Test(expected = IllegalArgumentException::class)
    fun testFullStatisticsError() {
        statisticsProvider.analyseList(listOf())
    }

    @Test
    fun testFullStatisticsNormal() { // Numbers 1-10

        /* Given */
        val data = listOf(
                1.0,
                9.0,
                5.0,
                3.0,
                7.0,
                4.0,
                2.0,
                10.0,
                6.0,
                8.0)

        /* When */
        val result = statisticsProvider.analyseList(data)

        /* Expect */
        result.mean shouldBe 5.5
        result.median shouldBe 5.5 // 5.0 and 6.0 should be the middle two
    }

    /*** mean ***/
    @Test
    fun testMean1() {
        /* Given */
        val data = listOf(1.0)

        /* When */
        val mean = data.mean()

        /* Expect */
        mean shouldBe 1.0
    }

    @Test
    fun testMeanDuplicates() {
        /* Given */
        val data = listOf(2.0, 2.0, 2.0)

        /* When */
        val mean = data.mean()

        /* Expect */
        mean shouldBe 2.0
    }

    @Test
    fun testMeanNormal() {
        /* Given */
        val data = listOf(1.0, 2.0, 3.0)

        /* When */
        val mean = data.mean()

        /* Expect */
        mean shouldBe 2.0
    }

    @Test
    fun testMeanNegative() {
        /* Given */
        val data = listOf(-1.0, -2.0, -3.0)

        /* When */
        val mean = data.mean()

        /* Expect */
        mean shouldBe -2.0
    }

    @Test
    fun testMeanMixedSign() {
        /* Given */
        val data = listOf(-1.0, 60.0, -2.0)

        /* When */
        val mean = data.mean()

        /* Expect */
        mean shouldBe 19.0
    }

    /*** median ***/
    @Test(expected = IllegalArgumentException::class)
    fun testMedianEmpty() {
        /* Given */
        val data = listOf<Double>()

        /* When */
        data.median()
    }

    @Test
    fun testMedianOdd() {
        /* Given */
        val data = listOf(1.0, 21.0, 3.0)

        /* When */
        val median = data.median()

        /* Expect */
        median shouldBe 3.0
    }

    @Test
    fun testMedianEven() {
        /* Given */
        val data = listOf(1.0, 99.0, 101.0, 300.0)

        /* When */
        val median = data.median()

        /* Expect */
        median shouldBe 100.0
    }

    /*** Odd ***/
    @Test
    fun testOdd() {
        /* Given */
        val data = 13

        /* When */
        val odd = data.isOdd()

        /* Expect */
        odd shouldBe true
    }

    @Test
    fun testEven() {
        /* Given */
        val data = 14

        /* When */
        val odd = data.isOdd()

        /* Expect */
        odd shouldBe false
    }

    /*** Parse list ***/
    @Test
    fun testEmpty() {
        /* Given */
        val input = ""

        /* When */
        val result = statisticsProvider.parseList(input)

        /* Expect */
        result shouldBe StatisticsProvider.ParseResult.Empty
    }

    @Test
    fun testWhiteSpace() {
        /* Given */
        val input = """  
              
                 
                                             
                                   
                                  
        """ // Space, newline, tab

        /* When */
        val result = statisticsProvider.parseList(input)

        /* Expect */
        result shouldBe StatisticsProvider.ParseResult.Empty
    }

    @Test
    fun testTight() {
        /* Given */
        val input = "1,2,3"

        /* When */
        val result = statisticsProvider.parseList(input)

        /* Expect */
        result shouldBe StatisticsProvider.ParseResult.Success(listOf(1.0, 2.0, 3.0))
    }

    @Test
    fun testLoose() {
        /* Given */
        val input = "1,2,3"

        /* When */
        val result = statisticsProvider.parseList(input)

        /* Expect */
        result shouldBe StatisticsProvider.ParseResult.Success(listOf(1.0, 2.0, 3.0))
    }

    @Test
    fun testNewline() {
        /* Given */
        val input = """1
2
3"""

        /* When */
        val result = statisticsProvider.parseList(input)

        /* Expect */
        result shouldBe StatisticsProvider.ParseResult.Success(listOf(1.0, 2.0, 3.0))
    }

    @Test
    fun testMixedSeparators() {
        /* Given */
        val input = """1,,,,,2;;;;3"""

        /* When */
        val result = statisticsProvider.parseList(input)

        /* Expect */
        result shouldBe StatisticsProvider.ParseResult.Success(listOf(1.0, 2.0, 3.0))
    }

    @Test
    fun testNonsense() {
        /* Given */
        val input = "I am not a list of numbers"

        /* When */
        val result = statisticsProvider.parseList(input)

        /* Expect */
        result shouldBe StatisticsProvider.ParseResult.Invalid
    }
}
