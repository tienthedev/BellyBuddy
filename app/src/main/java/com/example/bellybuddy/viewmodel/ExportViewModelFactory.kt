package com.example.bellybuddy.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ExportViewModelFactory(
    private val application: Application,
    private val bowelMovementViewModel: BowelMovementViewModel,
    private val dailyJournalViewModel: DailyJournalViewModel,
    private val foodLogViewModel: FoodLogViewModel,
    private val symptomViewModel: SymptomViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExportViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExportViewModel(
                application,
                bowelMovementViewModel,
                dailyJournalViewModel,
                foodLogViewModel,
                symptomViewModel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
