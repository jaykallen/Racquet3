package com.jaykallen.racquet3

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created by Jay Kallen on 6/13/18.
 * This creates and initializes the Room database to be used with the model table
 */

@Database(entities = arrayOf(DataModel::class), version = 1)
abstract class RoomInstance: RoomDatabase() {

    abstract fun daoAccess(): RoomDao

    companion object {
        var instance: RoomInstance? = null

        fun getInstance(context: Context): RoomInstance? {
            if (instance == null) {
                synchronized(RoomInstance::class) {
                    instance = Room.databaseBuilder(context.applicationContext,
                            RoomInstance::class.java, "room.db").build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }


}