package com.intrasonics.mobile.numericanalyser.feature.calculator

import android.content.Context
import android.databinding.Observable
import android.databinding.ObservableField
import android.view.View
import com.intrasonics.mobile.numericanalyser.R
import com.intrasonics.mobile.numericanalyser.base.BaseViewModel
import com.intrasonics.mobile.numericanalyser.provider.StatisticsProvider
import javax.inject.Inject
import javax.inject.Provider

class CalculatorViewModel @Inject constructor(
    var statisticsProvider: StatisticsProvider,
    var contextProvider: Provider<Context>
) : BaseViewModel() {

    /** Fields used by the layout **/
    val rawInput = ObservableField<String>() // Two-way bound

    val inputError = ObservableField<String>()
    val inputErrorVisibility = ObservableField<Int>()

    val resultTableVisibility = ObservableField<Int>()
    val resultMean = ObservableField<String>()
    val resultMedian = ObservableField<String>()

    private val inputChangedCallback: Observable.OnPropertyChangedCallback

    fun onResetClick(view: View) {
        rawInput.set("")
    }

    init {
        // I would consider using RxJava here - but it seems extreme to add the library for just one use
        inputChangedCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                updateUI()
            }
        }
        rawInput.addOnPropertyChangedCallback(inputChangedCallback)
        updateUI()
    }

    fun updateUI() {
        val numericData = rawInput.get()?.let {
            statisticsProvider.parseList(it)
        } ?: StatisticsProvider.ParseResult.Empty

        val context = contextProvider.get()

        when (numericData) {
            is StatisticsProvider.ParseResult.Empty -> { // Assume it's obvious to the user that the field is empty so show nothing
                showError(null)
                showResult(null, null)
            }
            is StatisticsProvider.ParseResult.Invalid -> {
                showError(context.getString(R.string.activity_calculator_input_validation_invalid))
                showResult(null, null)
            }
            is StatisticsProvider.ParseResult.Success -> {
                val report = numericData.let {
                    statisticsProvider.analyseList(it.input)
                }
                showError(null)
                showResult(report.mean.toString(), report.median.toString())
            }
        }
    }

    private fun showError(messageOrNull: String?) {
        inputError.set(messageOrNull)
        inputErrorVisibility.set(if (messageOrNull != null) View.VISIBLE else View.GONE)
    }

    private fun showResult(mean: String?, median: String?) {
        resultMean.set(mean)
        resultMedian.set(median)
        resultTableVisibility.set(if (mean != null || median != null) View.VISIBLE else View.GONE)
    }

    /**
     * Tries to avoid ever leaving a memory / context leak
     */
    fun onDestroy() {
        rawInput.removeOnPropertyChangedCallback(inputChangedCallback)
    }
}
