package com.example.bellybuddy.userint.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyJournalingScreen(
    onSelectBottom: (BottomItem) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Daily Journaling") })
        },
        bottomBar = {
            BottomToolBar(
                selected = BottomItem.Grid,  // Or null if you don't want any selected
                onSelect = onSelectBottom
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Daily journaling stuff ehrehrherhe")
            // Add your daily journaling UI here
        }
    }
}