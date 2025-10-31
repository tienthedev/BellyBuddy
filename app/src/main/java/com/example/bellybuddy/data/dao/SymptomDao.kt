package com.example.bellybuddy.data.dao

import androidx.room.*
import com.example.bellybuddy.data.model.Symptom
import kotlinx.coroutines.flow.Flow

@Dao
interface SymptomDao {

    @Query("""
        SELECT * FROM symptom_entry 
        WHERE user_id = :userId 
        ORDER BY date DESC, time DESC
    """)
    fun getAllSymptoms(userId: Int): Flow<List<Symptom>>

    @Query("""
        SELECT * FROM symptom_entry 
        WHERE user_id = :userId AND date = :date
        ORDER BY time DESC
    """)
    fun getSymptomsByDate(userId: Int, date: String): Flow<List<Symptom>>

    @Query("SELECT * FROM symptom_entry WHERE symptom_id = :symptomId")
    suspend fun getSymptomById(symptomId: Int): Symptom?

    // Get symptoms linked to a specific bowel movement
    @Query("""
        SELECT s.* FROM symptom_entry s
        INNER JOIN bowel_symptom_link bsl ON s.symptom_id = bsl.symptom_id
        WHERE bsl.bowel_id = :bowelId
    """)
    fun getSymptomsForBowelMovement(bowelId: Int): Flow<List<Symptom>>

    @Insert
    suspend fun insert(symptom: Symptom): Long

    @Insert
    suspend fun insertAll(symptoms: List<Symptom>): List<Long>  // Returns list of IDs

    @Update
    suspend fun update(symptom: Symptom)

    @Delete
    suspend fun delete(symptom: Symptom)

    @Query("DELETE FROM symptom_entry WHERE user_id = :userId AND date = :date")
    suspend fun deleteByDate(userId: Int, date: String)

    @Query("DELETE FROM symptom_entry WHERE user_id = :userId")
    suspend fun deleteAllForUser(userId: Int)
}