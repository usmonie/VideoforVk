package akhmedoff.usman.data.utils

import java.util.*

object Utils {

    private val suffixes = TreeMap<Int, String>()

    init {
        suffixes[1_000] = "k"
        suffixes[1_000_000] = "M"
        suffixes[1_000_000_000] = "G"
    }


    fun format(value: Int): String {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Int.MIN_VALUE) return format(Int.MIN_VALUE + 1)
        if (value < 0) return "-" + format(-value)
        if (value < 1000) return value.toString() //deal with easy case

        val e = suffixes.floorEntry(value)
        val divideBy = e.key
        val suffix = e.value

        val truncated = value / (divideBy!! / 10) //the number part of the output times 10
        val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
        val stringBuilder = StringBuilder()
        return if (hasDecimal) stringBuilder.append(truncated / 10.0).append(suffix) else {
            stringBuilder.append(truncated / 10).append(suffix)
        }.toString()
    }
}