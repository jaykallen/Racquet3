package com.jaykallen.racquet3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jaykallen.racquet3.model.RacquetModel
import com.jaykallen.racquet3.room.RoomRepository
import com.jaykallen.racquet3.room.RoomyDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RoomRepository
    var allLiveData: LiveData<List<RacquetModel>>
    var allOrderedLiveData: LiveData<List<RacquetModel>>
    var idLiveData: LiveData<RacquetModel>?

    init {
        val userDao = RoomyDatabase.getDatabase(application).roomDao()
        repository = RoomRepository(userDao)
        allLiveData = repository.allLiveData
        allOrderedLiveData = repository.allOrderedLiveData
        idLiveData = repository.idLiveData
    }

    fun getId(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.getId(id)
        }
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
