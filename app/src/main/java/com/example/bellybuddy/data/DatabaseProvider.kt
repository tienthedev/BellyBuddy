package com.example.bellybuddy.data

import android.content.Context
import com.example.bellybuddy.data.database.AppDatabase
import com.example.bellybuddy.data.repository.*

object DatabaseProvider {

    @Volatile
    private var database: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return database ?: synchronized(this) {
            val instance = AppDatabase.getDatabase(context.applicationContext)
            database = instance
            instance
        }
    }
    fun getFoodLogRepository(context: Context): FoodLogRepository {
        val db = getDatabase(context)
        return FoodLogRepository(db.foodLogDao())
    }

    fun getSymptomRepository(context: Context): SymptomRepository {
        val db = getDatabase(context)
        return SymptomRepository(db.symptomDao())
    }

    fun getBowelMovementRepository(context: Context): BowelMovementRepository {
        val db = getDatabase(context)
        return BowelMovementRepository(
            db.bowelMovementDao(),
            db.bowelSymptomLinkDao()
        )
    }

    fun getDailyJournalRepository(context: Context): DailyJournalRepository {
        val db = getDatabase(context)
        return DailyJournalRepository(db.dailyJournalEntryDao())
    }
}