package com.example.bellybuddy.userint.screen

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GridScreen(
    onSelectBottom: (BottomItem) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("All Features") }) },
        bottomBar = {
            BottomToolBar(
                selected = BottomItem.Grid,        // green circle on Grid
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
            Text("Grid screen stub")
        }
    }
}
