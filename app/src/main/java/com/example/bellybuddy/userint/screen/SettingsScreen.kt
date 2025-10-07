package com.example.bellybuddy.userint.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onSelectBottom: (BottomItem) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        },
        bottomBar = {
            BottomToolBar(
                selected = BottomItem.Settings,   // ← green circle on Settings
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
            Text("Settings screen stuff here", style = MaterialTheme.typography.headlineSmall)
        }
    }
}
