package com.intrasonics.mobile.numericanalyser.feature.calculator

import android.content.Context
import android.view.View
import com.intrasonics.mobile.numericanalyser.R
import com.intrasonics.mobile.numericanalyser.provider.StatisticsProvider
import com.intrasonics.mobile.numericanalyser.provider.StatisticsProviderImpl
import io.kotlintest.shouldBe
import javax.inject.Provider
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class CalculatorViewModelTest {

    lateinit var statisticsProvider: StatisticsProvider
    lateinit var context: Context
    lateinit var contextProvider: Provider<Context>
    lateinit var calculatorViewModel: CalculatorViewModel

    @Before
    fun setup() {
        // While it's arguably impure to use a concrete class rather than a mock, it feels like it produces more sensible tests in this particular case
        statisticsProvider = StatisticsProviderImpl()

        context = mock(Context::class.java)
        `when`(context.getString(R.string.activity_calculator_input_validation_invalid)).thenReturn("INPUT ERROR")

        contextProvider = mock(Provider::class.java) as Provider<Context>
        `when`(contextProvider.get()).thenReturn(context)

        calculatorViewModel = CalculatorViewModel(statisticsProvider, contextProvider)
    }

    @Test
    fun testInitialState() {
        /* Expect */
        calculatorViewModel.resultTableVisibility.get() shouldBe View.GONE
        calculatorViewModel.resultMean.get() shouldBe null
        calculatorViewModel.resultMedian.get() shouldBe null

        calculatorViewModel.inputErrorVisibility.get() shouldBe View.GONE
        calculatorViewModel.inputError.get() shouldBe null
    }

    @Test
    fun testHappyPath() {
        /* Given */
        val data = "1.0, 2.0, 3.0"

        /* When */
        calculatorViewModel.rawInput.set(data)

        /* Expect */
        calculatorViewModel.resultTableVisibility.get() shouldBe View.VISIBLE
        calculatorViewModel.resultMean.get() shouldBe "2.0"
        calculatorViewModel.resultMedian.get() shouldBe "2.0"

        calculatorViewModel.inputErrorVisibility.get() shouldBe View.GONE
        calculatorViewModel.inputError.get() shouldBe null
    }

    @Test
    fun testNonsense() {
        /* Given */
        val data = "I have typed something else"

        /* When */
        calculatorViewModel.rawInput.set(data)

        /* Expect */
        calculatorViewModel.resultTableVisibility.get() shouldBe View.GONE
        calculatorViewModel.resultMean.get() shouldBe null
        calculatorViewModel.resultMedian.get() shouldBe null

        calculatorViewModel.inputErrorVisibility.get() shouldBe View.VISIBLE
        calculatorViewModel.inputError.get() shouldBe "INPUT ERROR"
    }

    @Test
    fun testReset() {
        /* When */
        calculatorViewModel.rawInput.set("something interesting")
        calculatorViewModel.onResetClick(mock(View::class.java))

        /* Expect */
        calculatorViewModel.rawInput.get() shouldBe ""
    }

    @After
    fun tearDown() {
        calculatorViewModel.onDestroy()
    }
}
