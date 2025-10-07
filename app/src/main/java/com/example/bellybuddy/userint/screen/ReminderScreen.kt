package com.example.bellybuddy.userint.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(
    onSelectBottom: (BottomItem) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Reminder") })
        },
        bottomBar = {
            BottomToolBar(
                selected = BottomItem.Bell,   // ← green circle on Settings
                onSelect = onSelectBottom
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Reminder screen stuff here", style = MaterialTheme.typography.headlineSmall)
        }
    }
}
