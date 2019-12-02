package com.jaykallen.racquet3.room

import com.jaykallen.racquet3.model.RacquetModel

// 2019-11-22 JK: Modified so that live data is only used in the view model

class RoomRepository(private val roomDao: RoomDao) {
    private lateinit var allItems: List<RacquetModel>
    private lateinit var allItemsOrdered: List<RacquetModel>
    private lateinit var singleItem: RacquetModel

    fun getAll(): List<RacquetModel> {
        allItems = roomDao.getAll()
        return allItems
    }

    fun getAllOrdered(): List<RacquetModel> {
        allItemsOrdered = roomDao.getAllOrdered()
        return allItemsOrdered
    }

    fun getId(id: Long): RacquetModel {
        singleItem = roomDao.getId(id)
        return singleItem
    }

    fun insert(racquetModel: RacquetModel) {
        roomDao.insert(racquetModel)
    }

    fun update(racquetModel: RacquetModel) {
        roomDao.update(racquetModel)
    }

    fun delete(racquetModel: RacquetModel) {
        roomDao.delete(racquetModel)
    }

    fun deleteAll() {
        roomDao.deleteAll()
    }
}