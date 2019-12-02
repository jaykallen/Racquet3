package com.jaykallen.racquet3.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jaykallen.racquet3.StartApp
import com.jaykallen.racquet3.model.RacquetModel
import com.jaykallen.racquet3.room.RoomRepository
import com.jaykallen.racquet3.room.RoomyDatabase
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {
    private val repository: RoomRepository
    private var thread: Job = Job()
    private var counter = 0
    private var running = true
    var duration: Long = 3       // Refresh data every XX seconds
    var storyLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        val dao = RoomyDatabase.getDatabase(StartApp.applicationContext()).roomDao()
        repository = RoomRepository(dao)
    }

    fun loadDataRefresher() {
        if (running) {
            println("Update #${counter} next in $duration seconds")
            thread = CoroutineScope(Dispatchers.Default).launch {
                withContext(Dispatchers.Main) {
                    storyLiveData.value = getServerData(counter)
                }
                delay(duration * 1000)
                counter++
                loadDataRefresher()
            }
        }
    }

    private fun getServerData(counter: Int): String {
        return when (counter % 11) {
            0 -> "Manufacturer claims of the balance point"
            1 -> "are often incorrect due to quality control"
            2 -> "and change after ordinary maintenance"
            3 -> "such as new grips, strings, etc."
            4 -> "To determine the balance point"
            5 -> "lie a racquet on a table or counter top with"
            6 -> "the handle portion hanging off the edge."
            7 -> "Slowly slide the racquet until it reaches"
            8 -> "a balancing point where it is about"
            9 -> "to fall off. Mark this point and measure"
            10 -> "from the bottom of the handle."
            else -> "Message has been displayed $counter times"
        }
    }

    fun killDataRefresher() {
        println("Cancel data refresh# ${counter}")
        running = false
        thread.cancel()
    }

    fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAll()
        }
    }
}