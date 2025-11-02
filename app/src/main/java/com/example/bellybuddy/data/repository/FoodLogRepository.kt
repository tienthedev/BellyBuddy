package com.example.bellybuddy.data.repository

import com.example.bellybuddy.data.dao.FoodLogDao
import com.example.bellybuddy.data.model.FoodLog
import kotlinx.coroutines.flow.Flow

class FoodLogRepository(private val foodLogDao: FoodLogDao) {

    fun getAllFoodLogs(userId: Int): Flow<List<FoodLog>> {
        return foodLogDao.getAllFoodLogs(userId)
    }

    fun getFoodLogsByDate(userId: Int, date: String): Flow<List<FoodLog>> {
        return foodLogDao.getFoodLogsByDate(userId, date)
    }

    fun getFoodLogsByDateRange(
        userId: Int,
        startDate: String,
        endDate: String
    ): Flow<List<FoodLog>> {
        return foodLogDao.getFoodLogsByDateRange(userId, startDate, endDate)
    }

    fun getFoodLogsByMealType(userId: Int, mealType: String): Flow<List<FoodLog>> {
        return foodLogDao.getFoodLogsByMealType(userId, mealType)
    }

    suspend fun getFoodLogById(foodId: Int): FoodLog? {
        return foodLogDao.getFoodLogById(foodId)
    }

    suspend fun insert(foodLog: FoodLog): Long {
        return foodLogDao.insert(foodLog)
    }

    suspend fun update(foodLog: FoodLog) {
        val updatedLog = foodLog.copy(timeUpdated = System.currentTimeMillis())
        foodLogDao.update(updatedLog)
    }

    suspend fun delete(foodLog: FoodLog) {
        foodLogDao.delete(foodLog)
    }

    suspend fun deleteAllForUser(userId: Int) {
        foodLogDao.deleteAllForUser(userId)
    }

    fun searchFoodLogs(userId: Int, query: String): Flow<List<FoodLog>> {
        return foodLogDao.searchFoodLogs(userId, query)
    }

    suspend fun getEntryCountForDate(userId: Int, date: String): Int {
        return foodLogDao.getEntryCountForDate(userId, date)
    }
}