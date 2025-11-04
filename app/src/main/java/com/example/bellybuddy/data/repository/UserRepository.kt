package com.example.bellybuddy.data.repository

import com.example.bellybuddy.data.dao.UserDao
import com.example.bellybuddy.data.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides a clean API for data access to the rest of the application.
 * It abstracts the data source (UserDao) from the ViewModels.
 */
class UserRepository(private val userDao: UserDao) {

    // A flow that emits the list of all users from the database.
    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    // A flow that emits the currently logged-in user, or null if no one is logged in.
    val loggedInUser: Flow<User?> = userDao.getLoggedInUser()

    /**
     * Inserts a new user.
     */
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    /**
     * Updates an existing user.
     */
    suspend fun update(user: User) {
        userDao.update(user)
    }

    /**
     * Deletes a specific user.
     */
    suspend fun delete(user: User) {
        userDao.delete(user)
    }

    /**
     * Deletes all users from the database.
     */
    suspend fun deleteAll() {
        userDao.deleteAll()
    }

    /**
     * Finds a user by their email address for login checks.
     */
    suspend fun findUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    /**
     * Sets a specific user as the logged-in user.
     * This first logs out all other users to ensure data integrity.
     */
    suspend fun loginUser(user: User) {
        // First, log out everyone else to ensure only one user is logged in.
        userDao.logOutAllUsers()
        // Then, update the current user to set them as logged in.
        userDao.update(user.copy(isLoggedIn = true))
    }

    /**
     * Logs out all users.
     */
    suspend fun logOut() {
        userDao.logOutAllUsers()
    }
}
