package com.jaykallen.racquet3.room

import androidx.room.*
import com.jaykallen.racquet3.model.RacquetModel

@Dao
interface RoomDao {

    @Query("Select * from racquet_table")
    fun getAll(): List<RacquetModel>

    @Query("Select * from racquet_table order by name")
    fun getAllOrdered(): List<RacquetModel>

    @Query("Select * from racquet_table where id=:id")
    fun getId(id: Long): RacquetModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(racquetModel: RacquetModel)

    @Update
    fun update(racquetModel: RacquetModel)

    @Delete
    fun delete(racquetModel: RacquetModel)

    @Query("delete from racquet_table")
    fun deleteAll()

}