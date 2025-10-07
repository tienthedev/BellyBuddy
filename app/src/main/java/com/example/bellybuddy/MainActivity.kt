package com.example.bellybuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.bellybuddy.navigation.AppNavHost
import com.example.bellybuddy.ui.theme.BellyBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BellyBuddyTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}
