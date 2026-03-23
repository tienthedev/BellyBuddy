package com.example.bellybuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.bellybuddy.navigation.AppNavHost
import com.example.bellybuddy.ui.theme.BellyBuddyTheme
import com.example.bellybuddy.viewmodel.UserViewModel
import com.example.bellybuddy.viewmodel.UserViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Create UserViewModel scoped to the Activity so it's shared across all screens
            val userViewModel: UserViewModel = viewModel(
                factory = UserViewModelFactory(application)
            )
            
            BellyBuddyTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController, userViewModel = userViewModel)
            }
        }
    }
}
