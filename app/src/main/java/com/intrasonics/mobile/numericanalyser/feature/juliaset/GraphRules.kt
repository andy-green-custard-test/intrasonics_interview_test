package com.intrasonics.mobile.numericanalyser.feature.juliaset

/**
 * Minimum information to define WHAT data to draw (as opposed to rendering behaviour)
 */
data class GraphRules(
    /**
     * c  in f(z) = z*z + c
     */
val real: Float,
    val imaginary: Float,
    /**
     * What absolute value do we treat as evidence we have diverged?
     */
    val veryBig: Float,
    /**
     * How many iterations can we spend waiting for it to hit veryBig before we assume it converges?
     */
    val maximumIterationsToGetBig: Int,

    /**
     *  Axes
     *  */
    val minReal: Float,
    val maxReal: Float,
    val minImaginary: Float,
    val maxImaginary: Float
)
