package com.example.bellybuddy.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Add an index on the 'email' column to make lookups faster and ensure it's unique.
@Entity(    tableName = "user_table",
    indices = [Index(value = ["email"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Login and Profile Info
    val name: String,
    val email: String,
    val passwordHash: String, // Store a hash of the password, never plain text!

    // User-specific data
    val weight: Double?, // Use nullable types for optional data
    val height: Double?,
    val age: Int?,

    @ColumnInfo(defaultValue = "0") // Set a default value in the database
    val isLoggedIn: Boolean = false
)
