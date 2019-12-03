package com.jaykallen.racquet3.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jaykallen.racquet3.model.RacquetModel


@Database(entities = [RacquetModel::class], version = 3, exportSchema = false)
abstract class RoomyDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao

    companion object {
        @Volatile
        private var INSTANCE: RoomyDatabase? = null

        fun getDatabase(context: Context): RoomyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomyDatabase::class.java,
                    "room_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    fun destroyInstance() {
        INSTANCE = null
    }
}