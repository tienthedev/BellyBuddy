package com.example.bellybuddy.data.dao

import androidx.room.*
import com.example.bellybuddy.data.model.BowelMovement
import com.example.bellybuddy.data.model.BowelMovementWithSymptoms
import kotlinx.coroutines.flow.Flow

@Dao
interface BowelMovementDao {

    @Query("""
        SELECT * FROM bowel_movement_entry 
        WHERE user_id = :userId 
        ORDER BY date DESC, time DESC
    """)
    fun getAllBowelMovements(userId: Int): Flow<List<BowelMovement>>

    @Query("""
        SELECT * FROM bowel_movement_entry 
        WHERE user_id = :userId AND date = :date
        ORDER BY time DESC
    """)
    fun getBowelMovementsByDate(userId: Int, date: String): Flow<List<BowelMovement>>

    @Query("SELECT * FROM bowel_movement_entry WHERE bowel_id = :bowelId")
    suspend fun getBowelMovementById(bowelId: Int): BowelMovement?

    // Get bowel movement with all linked symptoms
    @Transaction
    @Query("SELECT * FROM bowel_movement_entry WHERE bowel_id = :bowelId")
    suspend fun getBowelMovementWithSymptoms(bowelId: Int): BowelMovementWithSymptoms?

    // Get all bowel movements with symptoms for a user
    @Transaction
    @Query("""
        SELECT * FROM bowel_movement_entry 
        WHERE user_id = :userId 
        ORDER BY date DESC, time DESC
    """)
    fun getAllBowelMovementsWithSymptoms(userId: Int): Flow<List<BowelMovementWithSymptoms>>

    @Insert
    suspend fun insert(bowelMovement: BowelMovement): Long

    @Update
    suspend fun update(bowelMovement: BowelMovement)

    @Delete
    suspend fun delete(bowelMovement: BowelMovement)

    @Query("DELETE FROM bowel_movement_entry WHERE user_id = :userId")
    suspend fun deleteAllForUser(userId: Int)
}