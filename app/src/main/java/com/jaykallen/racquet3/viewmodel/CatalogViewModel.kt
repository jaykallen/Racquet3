package com.jaykallen.racquet3.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.jaykallen.racquet3.model.RacquetModel
import com.jaykallen.racquet3.room.RoomRepository
import com.jaykallen.racquet3.room.RoomyDatabase
import kotlinx.coroutines.*

class CatalogViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RoomRepository
    var allLiveData = MutableLiveData<List<RacquetModel>>()
    var allOrderedLiveData = MutableLiveData<List<RacquetModel>>()

    init {
        val dao = RoomyDatabase.getDatabase(application).roomDao()
        repository = RoomRepository(dao)
    }

    fun getAll() {
        CoroutineScope(Dispatchers.IO).launch {
            val itemList = repository.getAll()
            withContext(Dispatchers.Main) {
                allLiveData.value = itemList
            }
        }
    }

    fun getAllOrdered() {
        CoroutineScope(Dispatchers.IO).launch {
            val itemList = repository.getAllOrdered()
            withContext(Dispatchers.Main) {
                allOrderedLiveData.value = itemList
            }
        }
    }
}
