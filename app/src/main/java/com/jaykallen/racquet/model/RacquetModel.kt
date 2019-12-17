package com.jaykallen.racquet.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Created by jay kallen on 3/29/2017.  This will work without a @PrimaryKey definition.  However, you
// won't be able to update a record without the primary key.
// Note: Every time you change the database structure, you will need to recreate the emulator or else
// you'll get a "need Realm Migration".

@Entity(tableName = "racquet_table")
data class RacquetModel (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0, // Insert methods treat 0 as not-set while inserting the item.
    @ColumnInfo(name = "name") var name: String = "Default",
    @ColumnInfo(name = "units") var units: String = "Inches",                   // This could be an enum with metric and inches
    @ColumnInfo(name = "headSize") var headSize: Double = 100.0,
    @ColumnInfo(name = "length") var length: Double = 27.0,                     // Total length of racquet
    @ColumnInfo(name = "weight") var weight: Double = 12.0,                     // Overall weight
    @ColumnInfo(name = "balance") var balance: String = "Head Light",           // This could be an enum
    @ColumnInfo(name = "balancePoint") var balancePoint: Double = 13.5,         // Balance point in measure
    @ColumnInfo(name = "headWeight") var headWeight: Double = 0.0,              // Points of Head Lightness
    @ColumnInfo(name = "grip") var grip: String = "4 1/4",                      // Grip Size
    @ColumnInfo(name = "stringName") var stringName: String? = null,
    @ColumnInfo(name = "stringPattern") var stringPattern: String = "",
    @ColumnInfo(name = "stringTension") var stringTension: Double = 50.0,
    @ColumnInfo(name = "notes") var notes: String = ""
) {
    constructor() : this(0, "", "Inches", 100.0, 27.0, 12.0, "Even", 13.5, 0.0, "4 1/4", "Luxilon", "16x19", 50.0, "")
}