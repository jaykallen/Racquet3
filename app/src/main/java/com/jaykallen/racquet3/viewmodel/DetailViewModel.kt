package com.jaykallen.racquet3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.jaykallen.racquet3.model.RacquetModel
import com.jaykallen.racquet3.room.RoomRepository
import com.jaykallen.racquet3.room.RoomyDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RoomRepository
    var idLiveData = MutableLiveData<RacquetModel>()

    init {
        val dao = RoomyDatabase.getDatabase(application).roomDao()
        repository = RoomRepository(dao)
    }

    fun getId(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val item = repository.getId(id)
            withContext(Dispatchers.Main) {
                idLiveData.value = item
            }
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
