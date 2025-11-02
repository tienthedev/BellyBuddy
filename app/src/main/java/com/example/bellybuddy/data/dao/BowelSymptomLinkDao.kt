package com.example.bellybuddy.data.dao

import androidx.room.*
import com.example.bellybuddy.data.model.BowelSymptomLink

@Dao
interface BowelSymptomLinkDao {

    // Link a symptom to a bowel movement
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(link: BowelSymptomLink)

    // Link multiple symptoms to a bowel movement
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(links: List<BowelSymptomLink>)

    // Remove a specific link
    @Delete
    suspend fun delete(link: BowelSymptomLink)

    // Remove all links for a bowel movement
    @Query("DELETE FROM bowel_symptom_link WHERE bowel_id = :bowelId")
    suspend fun deleteLinksForBowelMovement(bowelId: Int)

    // Remove all links for a symptom
    @Query("DELETE FROM bowel_symptom_link WHERE symptom_id = :symptomId")
    suspend fun deleteLinksForSymptom(symptomId: Int)

    // Get all symptom IDs linked to a bowel movement
    @Query("SELECT symptom_id FROM bowel_symptom_link WHERE bowel_id = :bowelId")
    suspend fun getSymptomIdsForBowelMovement(bowelId: Int): List<Int>

    // Get all bowel movement IDs linked to a symptom
    @Query("SELECT bowel_id FROM bowel_symptom_link WHERE symptom_id = :symptomId")
    suspend fun getBowelMovementIdsForSymptom(symptomId: Int): List<Int>
}