package com.example.bellybuddy.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bellybuddy.data.dao.*
import com.example.bellybuddy.data.model.*

@Database(
    entities = [
        User::class,
        FoodLog::class,
        Symptom::class,              // symptom_entry table
        BowelMovement::class,         // bowel_movement_entry table
        BowelSymptomLink::class       // bowel_symptom_link junction table
    ],
    version = 7,  // Increment version
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun foodLogDao(): FoodLogDao
    abstract fun symptomDao(): SymptomDao
    abstract fun bowelMovementDao(): BowelMovementDao
    abstract fun bowelSymptomLinkDao(): BowelSymptomLinkDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bellybuddy_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}