package com.example.bellybuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bellybuddy.data.model.User
import com.example.bellybuddy.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.security.MessageDigest

// The ViewModel now depends on the UserRepository
class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    // A flow of all users, useful for admin screens (like UserListScreen).
    val userList: StateFlow<List<User>> = userRepository.allUsers
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Flow starts when UI is visible
            initialValue = emptyList() // Initial value before the first data load
        )

    // A StateFlow to observe the currently logged-in user from anywhere in the app.
    // This is what the Dashboard will use.
    val loggedInUser: StateFlow<User?> = userRepository.loggedInUser
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    /**
     * Launch a coroutine to insert a new user into the database.
     * This is useful for simple additions, but `registerUser` is more complete.
     */
    fun insertUser(name: String, email: String) {
        if (name.isBlank() || email.isBlank()) {
            return
        }
        viewModelScope.launch {
            // Note: This doesn't handle passwords, so it's best used for testing.
            userRepository.insert(User(id = 0, name = name, email = email, passwordHash = "", weight = null, height = null, age = null))
        }
    }

    /**
     * Launch a coroutine to delete a user.
     */
    fun deleteUser(user: User) {
        viewModelScope.launch {
            userRepository.delete(user)
        }
    }

    /**
     * Launch a coroutine to update a user.
     */
    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.update(user)
        }
    }


    // --- NEW LOGIN/REGISTER LOGIC ---

    // A simple hashing function for demonstration. Use bcrypt in a real app.
    private fun hashPassword(password: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }

    /**
     * Registers a new user after checking if the email is already taken.
     * @param onResult A callback to return the result (success/fail) to the UI.
     */
    fun registerUser(name: String, email: String, password: String, onResult: (Boolean, String) -> Unit) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            onResult(false, "All fields are required.")
            return
        }

        viewModelScope.launch {
            // Check if user already exists
            if (userRepository.findUserByEmail(email) != null) {
                onResult(false, "A user with this email already exists.")
            } else {
                val newUser = User(
                    id = 0, // Room will auto-generate
                    name = name,
                    email = email,
                    passwordHash = hashPassword(password),
                    weight = null, // Set initial data as null
                    height = null,
                    age = null
                )
                userRepository.insert(newUser)
                onResult(true, "Registration successful!")
            }
        }
    }

    /**
     * Logs a user in by verifying their email and password.
     * @param onResult A callback to return the result (success/fail) to the UI.
     */
    fun loginUser(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.findUserByEmail(email)
            if (user == null) {
                onResult(false, "No user found with this email.")
            } else if (user.passwordHash != hashPassword(password)) {
                onResult(false, "Incorrect password.")
            } else {
                // Password is correct, log the user in
                userRepository.loginUser(user)
                onResult(true, "Login successful!")
            }
        }
    }

    /**
     * Logs out the currently active user.
     */
    fun logout() {
        viewModelScope.launch {
            userRepository.logOut()
        }
    }
}
