package com.example.bellybuddy.data.dao

import androidx.room.*
import com.example.bellybuddy.data.model.DailyJournal
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyJournalDao {

    @Query("""
        SELECT * FROM daily_journal_entry 
        WHERE user_id = :userId 
        ORDER BY date DESC, time_created DESC
    """)
    fun getAllJournalEntries(userId: Int): Flow<List<DailyJournal>>

    @Query("""
        SELECT * FROM daily_journal_entry 
        WHERE user_id = :userId AND date = :date
        LIMIT 1
    """)
    fun getJournalEntryByDate(userId: Int, date: String): Flow<DailyJournal?>

    @Query("""
        SELECT * FROM daily_journal_entry 
        WHERE user_id = :userId 
        AND date BETWEEN :startDate AND :endDate
        ORDER BY date DESC
    """)
    fun getJournalEntriesByDateRange(
        userId: Int,
        startDate: String,
        endDate: String
    ): Flow<List<DailyJournal>>

    @Query("SELECT * FROM daily_journal_entry WHERE journal_id = :journalId")
    suspend fun getJournalEntryById(journalId: Int): DailyJournal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DailyJournal): Long

    @Update
    suspend fun update(entry: DailyJournal)

    @Delete
    suspend fun delete(entry: DailyJournal)

    @Query("DELETE FROM daily_journal_entry WHERE user_id = :userId")
    suspend fun deleteAllForUser(userId: Int)

    @Query("""
        SELECT * FROM daily_journal_entry 
        WHERE user_id = :userId 
        AND notes LIKE '%' || :searchQuery || '%'
        ORDER BY date DESC
    """)
    fun searchJournalEntries(userId: Int, searchQuery: String): Flow<List<DailyJournal>>
}