package com.jaykallen.racquet3.managers

// Created by jkallen on 4/5/2017

object Helper {
    fun calcHeadWeight(slope: Double, length: Double, balancePoint: Double): Double {
        var headweight = 0.0
        if (length > 0 && balancePoint > 0) {
            val midpoint = length / 2
            headweight = (balancePoint - midpoint) / slope
            println("Formula:$balancePoint-$midpoint/$slope")
        }
        return headweight
    }

    fun calcBalancePoint(slope: Double, length: Double, headweight: Double): Double {
        var balancepoint = 0.0
        val midpoint = length / 2
        balancepoint = headweight * slope + midpoint
        return balancepoint
    }

    fun verifyEntry(entry: String): Double {
        var entryDouble = 0.0
        try {
            entryDouble = java.lang.Double.parseDouble(NumOnly(entry))
        } catch (e: Exception) {
            return 0.0
        }

        return entryDouble
    }

    private fun NumOnly(input: String): String {
        return input.replace("[^\\d.]".toRegex(), "")
    }

}
