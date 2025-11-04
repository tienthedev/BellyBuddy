package com.example.bellybuddy.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bellybuddy.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table ORDER BY name ASC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM user_table WHERE isLoggedIn = 1 LIMIT 1")
    fun getLoggedInUser(): Flow<User?>

    // Using OnConflictStrategy.IGNORE will prevent a crash if you try to insert a
    // user with an email that already exists (due to the unique index).
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    @Query("UPDATE user_table SET isLoggedIn = 0")
    suspend fun logOutAllUsers()
}
