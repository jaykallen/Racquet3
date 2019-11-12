package com.jaykallen.racquet3.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jaykallen.racquet3.model.RacquetModel

/**
 * Created by Jay Kallen on 6/13/18.
 * This interface contains all the CRUD queries used by Room.
 */

@Dao
interface RoomDao {

    @Query("Select * from racquet_table")
    fun getAll(): LiveData<ArrayList<RacquetModel>>

    @Query("Select * from racquet_table where id=:id")
    fun getId(id: String): LiveData<RacquetModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(racquetModel: RacquetModel)

    @Update
    fun update(racquetModel: RacquetModel)

    @Delete
    fun delete(racquetModel: RacquetModel)

}