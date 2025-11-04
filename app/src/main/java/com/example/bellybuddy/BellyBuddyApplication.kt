package com.example.bellybuddy

import android.app.Application
import androidx.room.Room
import com.example.bellybuddy.data.database.AppDatabase

class BellyBuddyApplication : Application() {


    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "belly_buddy_database"
        )

            .fallbackToDestructiveMigration()
            .build()
    }
}
