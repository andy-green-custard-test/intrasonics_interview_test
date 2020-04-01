package com.intrasonics.mobile.numericanalyser

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.intrasonics.mobile.numericanalyser.feature.welcome.WelcomeActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AboutFlowTest {
    @get:Rule
    val activityRule = ActivityTestRule(WelcomeActivity::class.java)

    @Before
    fun navigateToAbout() {
        Espresso.onView(ViewMatchers.withId(R.id.activity_welcome_button_about)).perform(click())
    }

    @Test
    fun expectWelcomePage() {
        val titleBar = Espresso.onView(ViewMatchers.withId(R.id.activity_about_title))
        titleBar.check(ViewAssertions.matches(ViewMatchers.withText("Legal Information")))
    }
}
