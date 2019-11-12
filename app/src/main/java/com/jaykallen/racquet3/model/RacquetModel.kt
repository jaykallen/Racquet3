package com.jaykallen.racquet3.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Created by jay kallen on 3/29/2017.  This will work without a @PrimaryKey definition.  However, you
// won't be able to update a record without the primary key.
// Note: Every time you change the database structure, you will need to recreate the emulator or else
// you'll get a "need Realm Migration".

@Entity(tableName = "racquet_table")
data class RacquetModel (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0,
    @ColumnInfo(name = "name") var name: String = "Default",
    @ColumnInfo(name = "units") var units: String? = "Inches",
    @ColumnInfo(name = "headSize") var headSize: Double = 100.0,
    @ColumnInfo(name = "length") var length: Double = 27.0,
    @ColumnInfo(name = "balancePoint") var balancePoint: Double = 13.5,
    @ColumnInfo(name = "headWeight") var headWeight: Double = 0.0,
    @ColumnInfo(name = "headLight") var headLight: Boolean? = true,
    @ColumnInfo(name = "weight") var weight: Double = 300.0,
    @ColumnInfo(name = "stringName") var stringName: String? = null,
    @ColumnInfo(name = "stringPattern") var stringPattern: String = "",
    @ColumnInfo(name = "stringTension") var stringTension: Double = 50.0,
    @ColumnInfo(name = "notes") var notes: String = ""
) {
    constructor() : this(0, "Default", "Inches", 100.0, 27.0, 13.5, 0.0, true, 300.0, "", "16x19", 50.0, "")
}