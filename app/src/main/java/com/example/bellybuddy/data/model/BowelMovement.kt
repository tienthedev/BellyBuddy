package com.example.bellybuddy.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bowel_movement_entry")
data class BowelMovement(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bowel_id")
    val bowelId: Int = 0,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "date")
    val date: String,  // "yyyy-MM-dd"

    @ColumnInfo(name = "time")
    val time: String,  // "HH:mm:ss"

    @ColumnInfo(name = "consistency")
    val consistency: String,  // e.g., "Type 4 - Normal"

    @ColumnInfo(name = "color")
    val color: String,  // e.g., "Brown"

    @ColumnInfo(name = "pain_level")
    val painLevel: Int,  // 0-10

    @ColumnInfo(name = "urgency_level")
    val urgencyLevel: Int,  // 0-10

    @ColumnInfo(name = "blood")
    val blood: Boolean = false,

    @ColumnInfo(name = "mucus")
    val mucus: Boolean = false,

    @ColumnInfo(name = "notes")
    val notes: String = ""
)