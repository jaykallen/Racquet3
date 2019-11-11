package com.jaykallen.racquet3.room

import androidx.room.*
import com.jaykallen.racquet3.model.RacquetModel

/**
 * Created by Jay Kallen on 6/13/18.
 * This interface contains all the CRUD queries used by Room.
 */

@Dao
interface RoomDao {

    @Query("Select * from RacquetModel")
    fun getAll(): List<RacquetModel>

    @Query("Select * from RacquetModel where id=:id")
    fun getById(id: String): RacquetModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(racquetModel: RacquetModel)

    @Update
    fun update(racquetModel: RacquetModel)

    @Delete
    fun delete(racquetModel: RacquetModel)

}