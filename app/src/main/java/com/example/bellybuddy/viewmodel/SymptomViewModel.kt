package com.example.bellybuddy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bellybuddy.data.DatabaseProvider
import com.example.bellybuddy.data.model.Symptom
import com.example.bellybuddy.data.repository.SymptomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SymptomViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SymptomRepository =
        DatabaseProvider.getSymptomRepository(application)

    private val _currentUserId = MutableStateFlow(1)
    val currentUserId: StateFlow<Int> = _currentUserId

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getAllSymptoms(): Flow<List<Symptom>> {
        return repository.getAllSymptoms(_currentUserId.value)
    }

    fun getSymptomsByDate(date: String): Flow<List<Symptom>> {
        return repository.getSymptomsByDate(_currentUserId.value, date)
    }

    fun getSymptomsForBowelMovement(bowelId: Int): Flow<List<Symptom>> {
        return repository.getSymptomsForBowelMovement(bowelId)
    }

    fun insertSymptom(symptom: Symptom, onComplete: (Long) -> Unit) = viewModelScope.launch {
        _isLoading.value = true
        val symptomWithUserId = symptom.copy(userId = _currentUserId.value)
        val id = repository.insert(symptomWithUserId)
        onComplete(id)
        _isLoading.value = false
    }

    fun insertMultipleSymptoms(symptoms: List<Symptom>, onComplete: (List<Long>) -> Unit) = viewModelScope.launch {
        _isLoading.value = true
        val symptomsWithUserId = symptoms.map { it.copy(userId = _currentUserId.value) }
        val ids = repository.insertAll(symptomsWithUserId)
        onComplete(ids)
        _isLoading.value = false
    }

    fun updateSymptom(symptom: Symptom) = viewModelScope.launch {
        _isLoading.value = true
        repository.update(symptom)
        _isLoading.value = false
    }

    fun deleteSymptom(symptom: Symptom) = viewModelScope.launch {
        _isLoading.value = true
        repository.delete(symptom)
        _isLoading.value = false
    }

    fun deleteSymptomsByDate(date: String) = viewModelScope.launch {
        _isLoading.value = true
        repository.deleteByDate(_currentUserId.value, date)
        _isLoading.value = false
    }

    fun setCurrentUser(userId: Int) {
        _currentUserId.value = userId
    }
}