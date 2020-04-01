package com.intrasonics.mobile.numericanalyser.feature.about

import android.app.Activity
import android.os.Bundle
import com.intrasonics.mobile.numericanalyser.R

/**
 * Since this page is completely dumb, no ViewModel has been created, unlike other activities
 */
class AboutActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }
}
