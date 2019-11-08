package com.jaykallen.racquet3

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Jay Kallen on 6/13/18.
 * This is a sample data class which can be adapted to any model.
 */

@Entity(tableName = "DataModel")
data class DataModel (
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        @ColumnInfo(name = "name") var name: String,
        @ColumnInfo(name = "descrip") var descrip: String,
        @ColumnInfo(name = "phone") var phone: String,
        @ColumnInfo(name = "address") var address: String
        ) {

    constructor() : this(0, "", "", "", "")

}