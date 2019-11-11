package com.procatdt.navsample

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class CatalogViewModel : ViewModel() {
    private val password = "jetpackrox"     // This would typically be verified from the server

    fun validatePassword(entry: String): Boolean {
        return entry == password
    }


}
