package com.example.bellybuddy.userint.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bellybuddy.R
import com.example.bellybuddy.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    userViewModel: UserViewModel,
    onNavigateToProfile: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    val loggedInUser by userViewModel.loggedInUser.collectAsState()

    LaunchedEffect(loggedInUser) {
        // Show splash for a moment
        delay(1500)
        if (loggedInUser != null) {
            onNavigateToDashboard()
        } else {
            onNavigateToProfile()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.bellybuddylogo),
            contentDescription = "BellyBuddy Logo",
            modifier = Modifier.size(180.dp)
        )
    }
}
