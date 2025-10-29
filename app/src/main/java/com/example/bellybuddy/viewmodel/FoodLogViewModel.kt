package com.example.bellybuddy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bellybuddy.data.DatabaseProvider
import com.example.bellybuddy.data.model.FoodLog
import com.example.bellybuddy.data.repository.FoodLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FoodLogViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FoodLogRepository =
        DatabaseProvider.getFoodLogRepository(application)

    private val _currentUserId = MutableStateFlow(1)
    val currentUserId: StateFlow<Int> = _currentUserId

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getAllFoodLogs(): Flow<List<FoodLog>> {
        return repository.getAllFoodLogs(_currentUserId.value)
    }

    fun getFoodLogsByDate(date: String): Flow<List<FoodLog>> {
        return repository.getFoodLogsByDate(_currentUserId.value, date)
    }

    fun getFoodLogsByDateRange(startDate: String, endDate: String): Flow<List<FoodLog>> {
        return repository.getFoodLogsByDateRange(_currentUserId.value, startDate, endDate)
    }

    fun searchFoodLogs(query: String): Flow<List<FoodLog>> {
        return repository.searchFoodLogs(_currentUserId.value, query)
    }

    fun insertFoodLog(foodLog: FoodLog) = viewModelScope.launch {
        _isLoading.value = true
        val logWithUserId = foodLog.copy(userId = _currentUserId.value)
        repository.insert(logWithUserId)
        _isLoading.value = false
    }

    fun updateFoodLog(foodLog: FoodLog) = viewModelScope.launch {
        _isLoading.value = true
        repository.update(foodLog)
        _isLoading.value = false
    }

    fun deleteFoodLog(foodLog: FoodLog) = viewModelScope.launch {
        _isLoading.value = true
        repository.delete(foodLog)
        _isLoading.value = false
    }

    fun setCurrentUser(userId: Int) {
        _currentUserId.value = userId
    }
}