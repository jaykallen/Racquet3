package com.jaykallen.racquet3.room

import androidx.lifecycle.LiveData
import com.jaykallen.racquet3.model.RacquetModel

// 2019-11-18 JK: I'm a bit confused as to how the repository works.  Does this return LiveData directly??
// Or should I setup a listener from the ViewModel and then another listener from the Fragment??

class RoomRepository(private val roomDao: RoomDao) {
    val allLiveData: LiveData<List<RacquetModel>> = roomDao.getAll()
    val allOrderedLiveData: LiveData<List<RacquetModel>> = roomDao.getAllOrdered()

//    val idLiveData(var id: long): LiveData<List<RacquetModel>> = roomDao.getId(id)
//    I don't know how to do a single lookup from the database
//    fun getId(id: Long): LiveData<RacquetModel> {
//        idLiveData = roomDao!!.getId(id)
//        return idLiveData
//    }

    suspend fun insert(racquetModel: RacquetModel) {
        roomDao.insert(racquetModel)
    }

    suspend fun update(racquetModel: RacquetModel) {
        roomDao.update(racquetModel)
    }

    suspend fun delete(racquetModel: RacquetModel) {
        roomDao.delete(racquetModel)
    }
}