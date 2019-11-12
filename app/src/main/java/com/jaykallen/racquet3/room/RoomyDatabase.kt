package com.jaykallen.racquet3.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jaykallen.racquet3.model.RacquetModel


@Database(entities = arrayOf(RacquetModel::class), version = 1, exportSchema = false)
abstract class RoomyDatabase: RoomDatabase() {

    abstract fun accessDao(): RoomDao

    companion object {
        @Volatile
        private var INSTANCE: RoomyDatabase? = null

        fun getInstance(context: Context): RoomyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            } else {
                synchronized(this) {
                    val instance = Room.databaseBuilder(context.applicationContext, RoomyDatabase::class.java, "room").build()
                    INSTANCE = instance
                    return instance
                }
            }
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }


}