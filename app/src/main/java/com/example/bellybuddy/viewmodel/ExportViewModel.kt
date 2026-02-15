package com.example.bellybuddy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.bellybuddy.data.model.BowelMovement
import com.example.bellybuddy.data.model.DailyJournal
import com.example.bellybuddy.data.model.FoodLog
import com.example.bellybuddy.data.model.Symptom
import kotlinx.coroutines.flow.Flow

class ExportViewModel(
    application: Application,
    private val bowelMovementViewModel: BowelMovementViewModel,
    private val dailyJournalViewModel: DailyJournalViewModel,
    private val foodLogViewModel: FoodLogViewModel,
    private val symptomViewModel: SymptomViewModel
) : AndroidViewModel(application) {

    fun getAllBowelMovements(): Flow<List<BowelMovement>> {
        return bowelMovementViewModel.getAllBowelMovements()
    }

    fun getAllDailyJournals(): Flow<List<DailyJournal>> {
        return dailyJournalViewModel.getAllJournalEntries()
    }

    fun getAllFoodLogs(): Flow<List<FoodLog>> {
        return foodLogViewModel.getAllFoodLogs()
    }

    fun getAllSymptoms(): Flow<List<Symptom>> {
        return symptomViewModel.getAllSymptoms()
    }
}
