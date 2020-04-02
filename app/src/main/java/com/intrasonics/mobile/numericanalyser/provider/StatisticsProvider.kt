package com.intrasonics.mobile.numericanalyser.provider

/**
 * A class which is designed to take in a data set and produce a report - currently mean & median.
 *
 * Future improvements might include min, max, range, mode etc - if this was real life, I would
 * probably put a JIRA in the backlog and cite it here.
 */
interface StatisticsProvider {
    /**
     * @param rawInput
     * Takes just about anything a user can ram into a text field and attempts to return either
     * a list of numbers or a reasonable representation of why it can't
     *
     * I've taken the view here that the priority is to try and always give (reasonable) results, so
     * made it as defensive as possible. If the results of this were mission-critical, a fail-fast
     * implementation would be better.
     *
     * E.g. 1,, 3, 4 will be parsed - which might be helpful - or might make the user fail to notice
     * they made a typo (forgot 2)
     *
     *
     *
     * Delimiter can be any of:
     * -Whitespace
     * -Comma ,
     * -Semicolon ;
     * -Colon :
     *
     *
     *
     * Limitations:
     *
     * Assumes that dot is used as the decimal separator

     */
    fun parseList(rawInput: String): ParseResult

    sealed class ParseResult {
        /**
         * The field is either empty or white space
         */
        object Empty : ParseResult()

        /**
         * Any other invalid input - e.g. unsuitable characters
         */
        object Invalid : ParseResult()

        /**
         *
         */
        data class Success(val input: List<Double>) : ParseResult()
    }

    /**
     * Produces statistics from a list of numbers
     *
     * @param input must be a list of at least 1 number
     *
     * @throws IllegalArgumentException if an empty list is passed
     *
     * Notes:
     *
     * 1. Whilst the example in the instructions gives a list of integers, the wording does just
     * say "an array of numbers". In the spirit of "best guess", I am going to use Double, since it
     * can express anything a user is realistically going to type into a form. Number is also a
     * good candidate, but it doesn't come with sort out the box (why re-invent the wheel?)
     *
     * 2. I'm going to assume I'm allowed to use a Kotlin list, which is convenient to use, and I can
     * achieve what I need in in O(n) time and O(n) space either way, although obviously
     * the copy required by using an immutable collection would hurt for extremely large inputs
     *
     * But clearly in "Real life", I would have checked these assumptions...
     */
    fun analyseList(input: List<Double>): Report

    data class Report(val input: List<Double>, val mean: Double, val median: Double)
}
