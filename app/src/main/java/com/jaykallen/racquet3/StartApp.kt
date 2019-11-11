package com.jaykallen.racquet3

import android.app.Application
import android.content.Context

class StartApp : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: Application? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}