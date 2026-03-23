package com.example.bellybuddy

import android.app.Application
import com.example.bellybuddy.data.database.AppDatabase

class BellyBuddyApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}
