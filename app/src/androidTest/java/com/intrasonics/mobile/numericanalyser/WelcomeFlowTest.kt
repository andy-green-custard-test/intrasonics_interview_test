package com.intrasonics.mobile.numericanalyser

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.intrasonics.mobile.numericanalyser.feature.welcome.WelcomeActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WelcomeFlowTest {
    @get:Rule val activityRule = ActivityTestRule(WelcomeActivity::class.java)

    @Test
    fun expectWelcomePage() {
        val titleBar = onView(withId(R.id.activity_welcome_title))
        titleBar.check(matches(withText("Please select an option:")))
    }
}
