package com.jaykallen.racquet3.room

import androidx.lifecycle.LiveData
import com.jaykallen.racquet3.model.RacquetModel

class RoomRepository(private val roomDao: RoomDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val all: LiveData<ArrayList<RacquetModel>> = roomDao.getAll()

    suspend fun insert(racquetModel: RacquetModel) {
        roomDao.insert(racquetModel)
    }



}