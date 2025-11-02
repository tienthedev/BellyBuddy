package com.example.bellybuddy.data.repository

import com.example.bellybuddy.data.dao.SymptomDao
import com.example.bellybuddy.data.model.Symptom
import kotlinx.coroutines.flow.Flow

class SymptomRepository(private val symptomDao: SymptomDao) {

    fun getAllSymptoms(userId: Int): Flow<List<Symptom>> {
        return symptomDao.getAllSymptoms(userId)
    }

    fun getSymptomsByDate(userId: Int, date: String): Flow<List<Symptom>> {
        return symptomDao.getSymptomsByDate(userId, date)
    }

    fun getSymptomsForBowelMovement(bowelId: Int): Flow<List<Symptom>> {
        return symptomDao.getSymptomsForBowelMovement(bowelId)
    }

    suspend fun getSymptomById(symptomId: Int): Symptom? {
        return symptomDao.getSymptomById(symptomId)
    }

    suspend fun insert(symptom: Symptom): Long {
        return symptomDao.insert(symptom)
    }

    suspend fun insertAll(symptoms: List<Symptom>): List<Long> {
        return symptomDao.insertAll(symptoms)
    }

    suspend fun update(symptom: Symptom) {
        symptomDao.update(symptom)
    }

    suspend fun delete(symptom: Symptom) {
        symptomDao.delete(symptom)
    }

    suspend fun deleteByDate(userId: Int, date: String) {
        symptomDao.deleteByDate(userId, date)
    }

    suspend fun deleteAllForUser(userId: Int) {
        symptomDao.deleteAllForUser(userId)
    }
}