package com.example.bellybuddy.data.repository

import com.example.bellybuddy.data.dao.DailyJournalDao
import com.example.bellybuddy.data.model.DailyJournal
import kotlinx.coroutines.flow.Flow

class DailyJournalRepository(private val DailyJournalDao: DailyJournalDao) {

    fun getAllJournalEntries(userId: Int): Flow<List<DailyJournal>> {
        return DailyJournalDao.getAllJournalEntries(userId)
    }

    fun getJournalEntryByDate(userId: Int, date: String): Flow<DailyJournal?> {
        return DailyJournalDao.getJournalEntryByDate(userId, date)
    }

    fun getJournalEntriesByDateRange(
        userId: Int,
        startDate: String,
        endDate: String
    ): Flow<List<DailyJournal>> {
        return DailyJournalDao.getJournalEntriesByDateRange(userId, startDate, endDate)
    }

    suspend fun getJournalEntryById(journalId: Int): DailyJournal? {
        return DailyJournalDao.getJournalEntryById(journalId)
    }

    suspend fun insert(entry: DailyJournal): Long {
        return DailyJournalDao.insert(entry)
    }

    suspend fun update(entry: DailyJournal) {
        val updatedEntry = entry.copy(timeUpdated = System.currentTimeMillis())
        DailyJournalDao.update(updatedEntry)
    }

    suspend fun delete(entry: DailyJournal) {
        DailyJournalDao.delete(entry)
    }

    suspend fun deleteAllForUser(userId: Int) {
        DailyJournalDao.deleteAllForUser(userId)
    }

    fun searchJournalEntries(userId: Int, query: String): Flow<List<DailyJournal>> {
        return DailyJournalDao.searchJournalEntries(userId, query)
    }
}