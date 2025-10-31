package com.example.bellybuddy.data

import android.content.Context
import androidx.room.Room
import com.example.bellybuddy.data.database.AppDatabase
import com.example.bellybuddy.data.repository.FoodLogRepository
import com.example.bellybuddy.data.repository.SymptomRepository
import com.example.bellybuddy.data.repository.BowelMovementRepository

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).build()
            INSTANCE = instance
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
}