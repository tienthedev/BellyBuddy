package com.example.bellybuddy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bellybuddy.data.DatabaseProvider
import com.example.bellybuddy.data.model.BowelMovement
import com.example.bellybuddy.data.model.BowelMovementWithSymptoms
import com.example.bellybuddy.data.repository.BowelMovementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BowelMovementViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BowelMovementRepository =
        DatabaseProvider.getBowelMovementRepository(application)

    private val _currentUserId = MutableStateFlow(1)
    val currentUserId: StateFlow<Int> = _currentUserId

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getAllBowelMovements(): Flow<List<BowelMovement>> {
        return repository.getAllBowelMovements(_currentUserId.value)
    }

    fun getBowelMovementsByDate(date: String): Flow<List<BowelMovement>> {
        return repository.getBowelMovementsByDate(_currentUserId.value, date)
    }

    fun getAllBowelMovementsWithSymptoms(): Flow<List<BowelMovementWithSymptoms>> {
        return repository.getAllBowelMovementsWithSymptoms(_currentUserId.value)
    }

    fun insertBowelMovement(
        bowelMovement: BowelMovement,
        onComplete: ((Long) -> Unit)? = null
    ) = viewModelScope.launch {
        _isLoading.value = true
        val bmWithUserId = bowelMovement.copy(userId = _currentUserId.value)
        val id = repository.insert(bmWithUserId)
        onComplete?.invoke(id)
        _isLoading.value = false
    }

    fun insertBowelMovementWithSymptoms(
        bowelMovement: BowelMovement,
        symptomIds: List<Int>,
        onComplete: ((Long) -> Unit)? = null
    ) = viewModelScope.launch {
        _isLoading.value = true
        val bmWithUserId = bowelMovement.copy(userId = _currentUserId.value)
        val id = repository.insertWithSymptoms(bmWithUserId, symptomIds)
        onComplete?.invoke(id)
        _isLoading.value = false
    }

    fun updateBowelMovement(bowelMovement: BowelMovement) = viewModelScope.launch {
        _isLoading.value = true
        repository.update(bowelMovement)
        _isLoading.value = false
    }

    fun deleteBowelMovement(bowelMovement: BowelMovement) = viewModelScope.launch {
        _isLoading.value = true
        repository.delete(bowelMovement)
        _isLoading.value = false
    }

    fun setCurrentUser(userId: Int) {
        _currentUserId.value = userId
    }
}