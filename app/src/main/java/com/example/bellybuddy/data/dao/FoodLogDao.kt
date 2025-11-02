package com.example.bellybuddy.data.dao

import androidx.room.*
import com.example.bellybuddy.data.model.FoodLog
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodLogDao {

    @Query("""
        SELECT * FROM food_entry 
        WHERE user_id = :userId 
        ORDER BY date DESC, time DESC
    """)
    fun getAllFoodLogs(userId: Int): Flow<List<FoodLog>>

    @Query("""
        SELECT * FROM food_entry 
        WHERE user_id = :userId AND date = :date
        ORDER BY time ASC
    """)
    fun getFoodLogsByDate(userId: Int, date: String): Flow<List<FoodLog>>

    @Query("""
        SELECT * FROM food_entry 
        WHERE user_id = :userId 
        AND date BETWEEN :startDate AND :endDate
        ORDER BY date DESC, time DESC
    """)
    fun getFoodLogsByDateRange(
        userId: Int,
        startDate: String,
        endDate: String
    ): Flow<List<FoodLog>>

    @Query("""
        SELECT * FROM food_entry 
        WHERE user_id = :userId AND meal_type = :mealType
        ORDER BY date DESC, time DESC
    """)
    fun getFoodLogsByMealType(userId: Int, mealType: String): Flow<List<FoodLog>>

    @Query("SELECT * FROM food_entry WHERE food_id = :foodId")
    suspend fun getFoodLogById(foodId: Int): FoodLog?

    @Insert
    suspend fun insert(foodLog: FoodLog): Long

    @Update
    suspend fun update(foodLog: FoodLog)

    @Delete
    suspend fun delete(foodLog: FoodLog)

    @Query("DELETE FROM food_entry WHERE user_id = :userId")
    suspend fun deleteAllForUser(userId: Int)

    @Query("""
        SELECT * FROM food_entry 
        WHERE user_id = :userId 
        AND food_name LIKE '%' || :searchQuery || '%'
        ORDER BY date DESC, time DESC
    """)
    fun searchFoodLogs(userId: Int, searchQuery: String): Flow<List<FoodLog>>

    @Query("""
        SELECT COUNT(*) FROM food_entry 
        WHERE user_id = :userId AND date = :date
    """)
    suspend fun getEntryCountForDate(userId: Int, date: String): Int
}