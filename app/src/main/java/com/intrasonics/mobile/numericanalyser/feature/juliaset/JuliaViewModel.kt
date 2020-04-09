package com.intrasonics.mobile.numericanalyser.feature.juliaset

import com.intrasonics.mobile.numericanalyser.base.BaseViewModel
import com.intrasonics.mobile.numericanalyser.navigation.RootNavigator
import javax.inject.Inject

class JuliaViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var navigator: RootNavigator
}
