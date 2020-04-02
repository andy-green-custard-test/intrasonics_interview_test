package com.intrasonics.mobile.numericanalyser

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.intrasonics.mobile.numericanalyser.feature.welcome.WelcomeActivity
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalculatorFlowTest {
    @get:Rule
    val activityRule = ActivityTestRule(WelcomeActivity::class.java)

    @Before
    fun navigateToAbout() {
        Espresso.onView(ViewMatchers.withId(R.id.activity_welcome_button_calculator)).perform(ViewActions.click())

        /* Expect */
        val titleBar = Espresso.onView(ViewMatchers.withId(R.id.activity_calculator_title))
        titleBar.check(ViewAssertions.matches(ViewMatchers.withText("Statistics Calculator")))

        val errorView = Espresso.onView(ViewMatchers.withId(R.id.activity_calculator_error))
        errorView.check(matches(not(isDisplayed())))

        val resultTable = Espresso.onView(ViewMatchers.withId(R.id.activity_calculator_result_table))
        resultTable.check(matches(not(isDisplayed())))
    }

    @Test
    fun enterInvalidData() {
        /* When */
        val inputField = Espresso.onView(ViewMatchers.withId(R.id.activity_calculator_input))
        inputField.perform(typeText("nonsense"))

        /* Expect */
        val errorView = Espresso.onView(ViewMatchers.withId(R.id.activity_calculator_error))
        errorView.check(ViewAssertions.matches(ViewMatchers.withText("Unable to understand that input. Please try again.")))

        val resultTable = Espresso.onView(ViewMatchers.withId(R.id.activity_calculator_result_table))
        resultTable.check(matches(not(isDisplayed())))
    }

    @Test
    fun enterValidData() {
        /* When */
        val inputField = Espresso.onView(ViewMatchers.withId(R.id.activity_calculator_input))
        inputField.perform(typeText("1.0 1.0 4.0"))

        /* Expect */
        val errorView = Espresso.onView(ViewMatchers.withId(R.id.activity_calculator_error))
        errorView.check(matches(not(isDisplayed())))

        val meanResult = Espresso.onView(ViewMatchers.withId(R.id.activity_calculator_result_mean_value))
        meanResult.check(ViewAssertions.matches(ViewMatchers.withText("2.0")))

        val medianResult = Espresso.onView(ViewMatchers.withId(R.id.activity_calculator_result_median_value))
        medianResult.check(ViewAssertions.matches(ViewMatchers.withText("1.0")))
    }

    @Test
    fun clearData() {
        /* When */
        val inputField = Espresso.onView(ViewMatchers.withId(R.id.activity_calculator_input))
        inputField.perform(typeText("1.0 1.0 4.0"))

        val clearButton = Espresso.onView(ViewMatchers.withId(R.id.activity_calculator_reset_button))
        clearButton.perform(click())

        /* Expect */
        inputField.check(ViewAssertions.matches(ViewMatchers.withText("")))

        val errorView = Espresso.onView(ViewMatchers.withId(R.id.activity_calculator_error))
        errorView.check(matches(not(isDisplayed())))

        val resultTable = Espresso.onView(ViewMatchers.withId(R.id.activity_calculator_result_table))
        resultTable.check(matches(not(isDisplayed())))
    }
}
