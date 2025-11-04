package com.example.bellybuddy.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_journal_entry")
data class DailyJournal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "journal_id")
    val journalId: Int = 0,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "date")
    val date: String,  // "yyyy-MM-dd"

    @ColumnInfo(name = "mood")
    val mood: String = "",  // Optional: "Happy", "Sad", "Neutral", etc.

    @ColumnInfo(name = "notes")
    val notes: String,  // The journal entry text

    @ColumnInfo(name = "time_created")
    val timeCreated: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "time_updated")
    val timeUpdated: Long = System.currentTimeMillis()
)