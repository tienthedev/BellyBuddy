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

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    val userList: StateFlow<List<User>> = userRepository.allUsers
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly, // Start observing immediately
            initialValue = emptyList()
        )

    val loggedInUser: StateFlow<User?> = userRepository.loggedInUser
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly, // Ensure it's ready as soon as possible
            initialValue = null
        )

    fun registerUser(
        name: String,
        email: String,
        password: String,
        age: Int? = null,
        weight: Double? = null,
        height: Double? = null,
        onResult: (Boolean, String) -> Unit
    ) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            onResult(false, "All fields are required.")
            return
        }

        viewModelScope.launch {
            // Ensure no one else is logged in before creating/updating this user
            userRepository.logOut()
            
            val existingUser = userRepository.findUserByEmail(email)
            if (existingUser != null) {
                val updatedUser = existingUser.copy(
                    name = name,
                    age = age,
                    weight = weight,
                    height = height,
                    isLoggedIn = true
                )
                userRepository.update(updatedUser)
                onResult(true, "Profile updated!")
            } else {
                val newUser = User(
                    id = 0,
                    name = name,
                    email = email,
                    passwordHash = hashPassword(password),
                    weight = weight,
                    height = height,
                    age = age,
                    isLoggedIn = true
                )
                userRepository.insert(newUser)
                onResult(true, "Registration successful!")
            }
        }
    }

    private fun hashPassword(password: String): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }

    fun loginUser(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.findUserByEmail(email)
            if (user == null) {
                onResult(false, "No user found with this email.")
            } else if (user.passwordHash != hashPassword(password)) {
                onResult(false, "Incorrect password.")
            } else {
                userRepository.loginUser(user)
                onResult(true, "Login successful!")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logOut()
        }
    }
    
    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.update(user)
        }
    }
}
