package com.jaykallen.racquet3

import androidx.room.*

/**
 * Created by Jay Kallen on 6/13/18.
 * This interface contains all the CRUD queries used by Room.
 */

@Dao
interface RoomDao {

    @Query("Select * from DataModel")
    fun getAll(): List<DataModel>

    @Query("Select * from DataModel where id=:id")
    fun getById(id: String): DataModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(dataModel: DataModel)

    @Update
    fun update(dataModel: DataModel)

    @Delete
    fun delete(dataModel: DataModel)

}