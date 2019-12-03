package com.jaykallen.racquet3.managers

// Created by jkallen on 4/5/2017

object Helper {

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
