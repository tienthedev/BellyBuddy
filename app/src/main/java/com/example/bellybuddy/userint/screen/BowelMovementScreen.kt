package com.example.bellybuddy.userint.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BowelMovementScreen(
    onSelectBottom: (BottomItem) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Bowel Movement") })
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
            Text("Track your bowel movement here")
            // Add your bowel movement tracking UI here
        }
    }
}