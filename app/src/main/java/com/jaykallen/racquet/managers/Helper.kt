package com.jaykallen.racquet.managers

// Created by jkallen on 4/5/2017

object Helper {

    fun toDouble(entry: String): Double {
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
