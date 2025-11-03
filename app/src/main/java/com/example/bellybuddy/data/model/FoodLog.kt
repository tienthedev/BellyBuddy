package com.example.bellybuddy.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// Includes a foreign key relationship to the User entity
//@Entity(
//    tableName = "food_entry",
//    foreignKeys = [
//        ForeignKey(
//            entity = User::class,
//            parentColumns = ["id"],
//            childColumns = ["user_id"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
//)

// Using this for testing purposes to remove the foreign key constraint
@Entity(tableName = "food_entry")
data class FoodLog(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "food_id")
    val foodId: Int = 0,

    @ColumnInfo(name = "user_id")
    val userId: Int,  // Foreign key to User table

    @ColumnInfo(name = "date")
    val date: String,  // Format: "yyyy-MM-dd" (e.g., "2025-10-29")

    @ColumnInfo(name = "time")
    val time: String,  // Format: "HH:mm:ss" (e.g., "14:30:00")

    @ColumnInfo(name = "food_name")
    val foodName: String,

    @ColumnInfo(name = "meal_type")
    val mealType: String,

    @ColumnInfo(name = "portion_size")
    val portionSize: String,

    @ColumnInfo(name = "notes")
    val notes: String = "",  // Optional - can be ingredients or description

    @ColumnInfo(name = "time_created")
    val timeCreated: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "time_updated")
    val timeUpdated: Long = System.currentTimeMillis()
)