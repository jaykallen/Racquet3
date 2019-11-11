package com.jaykallen.racquet3.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jaykallen.racquet3.model.RacquetModel


@Database(entities = arrayOf(RacquetModel::class), version = 1)
abstract class RoomInstance: RoomDatabase() {

    abstract fun daoAccess(): RoomDao

    companion object {
        var instance: RoomInstance? = null

        fun getInstance(context: Context): RoomInstance {
            if (instance == null) {
                synchronized(RoomInstance::class) {
                    instance = Room.databaseBuilder(context.applicationContext, RoomInstance::class.java, "room.db").build()
                }
            }
            return instance!!
        }

        fun destroyInstance() {
            instance = null
        }
    }


}