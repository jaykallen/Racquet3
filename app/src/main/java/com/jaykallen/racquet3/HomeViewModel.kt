package com.jaykallen.racquet3

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {
    var name: String = "Action Bind"
    private var thread: Job = Job()
    private var counter = 0
    private var running = true
    var duration: Long = 5       // Refresh data every 20 seconds
    var storyLiveData: MutableLiveData<String> = MutableLiveData()

    fun loadDataRefresher() {
        if (running) {
            counter++
            println("Update #${counter} next in $duration seconds")
            thread = CoroutineScope(Dispatchers.Default).launch {
                withContext(Dispatchers.Main) {
                    storyLiveData.value = getServerData(counter)
                }
                delay(duration * 1000)
                loadDataRefresher()
            }
        }
    }

    private fun getServerData(counter: Int): String {
        return when (counter) {
            0 -> "Once upon a time"
            1 -> "There was a handsome prince"
            2 -> "He decided to become an Android developer"
            3 -> "He said I will study hard and learn everything"
            4 -> "But the Android Gods at Google said"
            5 -> "I will change the way you program every year"
            6 -> "So you're always out of date, Muhahahaha"
            else -> "The prince cried $counter times"
        }
    }

    fun killDataRefresher() {
        println("Cancel data refresh# ${counter}")
        running = false
        thread.cancel()
    }

}