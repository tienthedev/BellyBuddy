package com.example.bellybuddy.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bellybuddy.data.dao.UserDao
import com.example.bellybuddy.data.dao.FoodLogDao
import com.example.bellybuddy.data.model.User
import com.example.bellybuddy.data.model.FoodLog

@Database(
    entities = [
        User::class,        // Creates "users" table
        FoodLog::class      // Creates "food_entry" table
    ],
    version = 5,  // Increment version when db schema changes
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun foodLogDao(): FoodLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Singleton pattern
        // Ensures only one instance of the database is created
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bellybuddy_database"
                )
                    .fallbackToDestructiveMigration()  // For development
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}