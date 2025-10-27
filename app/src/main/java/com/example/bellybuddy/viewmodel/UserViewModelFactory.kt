package com.example.bellybuddy.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bellybuddy.BellyBuddyApplication
import com.example.bellybuddy.data.repository.UserRepository // <-- Import UserRepository

class UserViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            // Get the DAO from the Application class
            val userDao = (application as BellyBuddyApplication).database.userDao()
            // Create the Repository instance
            val repository = UserRepository(userDao)

            @Suppress("UNCHECKED_CAST")
            // Provide the repository to the ViewModel
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
