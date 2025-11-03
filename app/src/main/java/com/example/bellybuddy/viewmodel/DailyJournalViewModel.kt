package com.example.bellybuddy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bellybuddy.data.DatabaseProvider
import com.example.bellybuddy.data.model.DailyJournal
import com.example.bellybuddy.data.repository.DailyJournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DailyJournalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DailyJournalRepository =
        DatabaseProvider.getDailyJournalRepository(application)

    private val _currentUserId = MutableStateFlow(1)
    val currentUserId: StateFlow<Int> = _currentUserId

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getAllJournalEntries(): Flow<List<DailyJournal>> {
        return repository.getAllJournalEntries(_currentUserId.value)
    }

    fun getJournalEntryByDate(date: String): Flow<DailyJournal?> {
        return repository.getJournalEntryByDate(_currentUserId.value, date)
    }

    fun getJournalEntriesByDateRange(startDate: String, endDate: String): Flow<List<DailyJournal>> {
        return repository.getJournalEntriesByDateRange(_currentUserId.value, startDate, endDate)
    }

    fun insertJournalEntry(
        entry: DailyJournal,
        onComplete: ((Long) -> Unit)? = null
    ) = viewModelScope.launch {
        _isLoading.value = true
        val entryWithUserId = entry.copy(userId = _currentUserId.value)
        val id = repository.insert(entryWithUserId)
        onComplete?.invoke(id)
        _isLoading.value = false
    }

    fun updateJournalEntry(entry: DailyJournal) = viewModelScope.launch {
        _isLoading.value = true
        repository.update(entry)
        _isLoading.value = false
    }

    fun deleteJournalEntry(entry: DailyJournal) = viewModelScope.launch {
        _isLoading.value = true
        repository.delete(entry)
        _isLoading.value = false
    }

    fun searchJournalEntries(query: String): Flow<List<DailyJournal>> {
        return repository.searchJournalEntries(_currentUserId.value, query)
    }

    fun setCurrentUser(userId: Int) {
        _currentUserId.value = userId
    }
}