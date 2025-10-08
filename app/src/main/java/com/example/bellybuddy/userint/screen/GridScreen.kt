package com.example.bellybuddy.userint.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GridScreen(
    onSelectBottom: (BottomItem) -> Unit,
    onBowelMovementClick: () -> Unit,
    onJournalClick: () -> Unit,
    onFoodLogClick: () -> Unit,
    onSymptomClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("All Features") }) },
        bottomBar = {
            BottomToolBar(
                selected = BottomItem.Grid,
                onSelect = onSelectBottom
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onJournalClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Daily Journal")
            }

            Button(
                onClick = onFoodLogClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Food Log")
            }

            Button(
                onClick = onSymptomClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Symptom Tracker")
            }

            Button(
                onClick = onBowelMovementClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Bowel Movements")
            }
        }
    }
}