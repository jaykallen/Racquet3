package com.procatdt.navsample

import android.app.Application
import androidx.lifecycle.*
import androidx.room.RoomDatabase
import com.jaykallen.racquet3.model.RacquetModel
import com.jaykallen.racquet3.room.RoomRepository
import com.jaykallen.racquet3.room.RoomyDatabase
import kotlinx.coroutines.*

class CatalogViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RoomRepository
    var allLiveData: LiveData<List<RacquetModel>>
    var allOrderedLiveData: LiveData<List<RacquetModel>>

    init {
        val userDao = RoomyDatabase.getDatabase(application).roomDao()
        repository = RoomRepository(userDao)
        allLiveData = repository.allLiveData
        allOrderedLiveData = repository.allOrderedLiveData
    }

    fun insert(racquetModel: RacquetModel) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(racquetModel)
        }
    }

    fun update(racquetModel: RacquetModel) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.update(racquetModel)
        }
    }

    fun delete(racquetModel: RacquetModel) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.delete(racquetModel)
        }
    }

}
