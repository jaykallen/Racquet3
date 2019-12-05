package com.jaykallen.racquet3.managers

// Created by jkallen on 4/4/2017

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object SharedPrefsManager {
    private lateinit var preferences: SharedPreferences
    private lateinit var sharedprefs: SharedPreferences.Editor

    fun setUnits(context: Context, units: String) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedprefs = preferences.edit()
        sharedprefs.putString("Units", units).apply()
    }

    fun getUnits(context: Context): String {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString("Units", "Inches")?:"Inches"  // 2nd value is default value.
    }
}
