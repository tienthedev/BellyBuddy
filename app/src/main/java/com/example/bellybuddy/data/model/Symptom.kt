package com.example.bellybuddy.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "symptom_entry")
data class Symptom(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "symptom_id")
    val symptomId: Int = 0,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "date")
    val date: String,  // "yyyy-MM-dd"

    @ColumnInfo(name = "time")
    val time: String,  // "HH:mm:ss"

    @ColumnInfo(name = "symptom_type")
    val symptomType: String,  // "Bloating", "Diarrhea", etc.

    @ColumnInfo(name = "intensity")
    val intensity: Int  // 0-10 (removed default, removed notes)
)