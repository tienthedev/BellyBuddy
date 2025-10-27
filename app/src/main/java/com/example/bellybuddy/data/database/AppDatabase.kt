package com.example.bellybuddy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bellybuddy.data.dao.UserDao
import com.example.bellybuddy.data.model.User

// Change version from 1 to 2
@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
