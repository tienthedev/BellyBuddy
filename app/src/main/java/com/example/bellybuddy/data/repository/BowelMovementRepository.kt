package com.example.bellybuddy.data.repository

import com.example.bellybuddy.data.dao.BowelMovementDao
import com.example.bellybuddy.data.dao.BowelSymptomLinkDao
import com.example.bellybuddy.data.model.BowelMovement
import com.example.bellybuddy.data.model.BowelMovementWithSymptoms
import com.example.bellybuddy.data.model.BowelSymptomLink
import kotlinx.coroutines.flow.Flow

class BowelMovementRepository(
    private val bowelMovementDao: BowelMovementDao,
    private val bowelSymptomLinkDao: BowelSymptomLinkDao
) {

    fun getAllBowelMovements(userId: Int): Flow<List<BowelMovement>> {
        return bowelMovementDao.getAllBowelMovements(userId)
    }

    fun getBowelMovementsByDate(userId: Int, date: String): Flow<List<BowelMovement>> {
        return bowelMovementDao.getBowelMovementsByDate(userId, date)
    }

    fun getAllBowelMovementsWithSymptoms(userId: Int): Flow<List<BowelMovementWithSymptoms>> {
        return bowelMovementDao.getAllBowelMovementsWithSymptoms(userId)
    }

    suspend fun getBowelMovementById(bowelId: Int): BowelMovement? {
        return bowelMovementDao.getBowelMovementById(bowelId)
    }

    suspend fun getBowelMovementWithSymptoms(bowelId: Int): BowelMovementWithSymptoms? {
        return bowelMovementDao.getBowelMovementWithSymptoms(bowelId)
    }

    suspend fun insert(bowelMovement: BowelMovement): Long {
        return bowelMovementDao.insert(bowelMovement)
    }

    // Insert bowel movement and link it to symptoms
    suspend fun insertWithSymptoms(bowelMovement: BowelMovement, symptomIds: List<Int>): Long {
        val bowelId = bowelMovementDao.insert(bowelMovement)

        // Create links between bowel movement and symptoms
        val links = symptomIds.map { symptomId ->
            BowelSymptomLink(bowelId = bowelId.toInt(), symptomId = symptomId)
        }
        bowelSymptomLinkDao.insertAll(links)

        return bowelId
    }

    suspend fun update(bowelMovement: BowelMovement) {
        bowelMovementDao.update(bowelMovement)
    }

    suspend fun delete(bowelMovement: BowelMovement) {
        bowelMovementDao.delete(bowelMovement)
    }

    suspend fun linkSymptomToBowelMovement(bowelId: Int, symptomId: Int) {
        bowelSymptomLinkDao.insert(BowelSymptomLink(bowelId, symptomId))
    }

    suspend fun unlinkSymptomFromBowelMovement(bowelId: Int, symptomId: Int) {
        bowelSymptomLinkDao.delete(BowelSymptomLink(bowelId, symptomId))
    }

    suspend fun deleteAllForUser(userId: Int) {
        bowelMovementDao.deleteAllForUser(userId)
    }
}