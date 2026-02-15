package com.example.bellybuddy.userint.screen

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bellybuddy.utils.generatePdf
import com.example.bellybuddy.viewmodel.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onSelectBottom: (BottomItem) -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val coroutineScope = rememberCoroutineScope()

    // Instantiate ViewModels
    val bowelMovementViewModel: BowelMovementViewModel = viewModel()
    val dailyJournalViewModel: DailyJournalViewModel = viewModel()
    val foodLogViewModel: FoodLogViewModel = viewModel()
    val symptomViewModel: SymptomViewModel = viewModel()

    // Create the factory
    val exportViewModelFactory = ExportViewModelFactory(
        application,
        bowelMovementViewModel,
        dailyJournalViewModel,
        foodLogViewModel,
        symptomViewModel
    )

    // Instantiate ExportViewModel with the factory
    val exportViewModel: ExportViewModel = viewModel(factory = exportViewModelFactory)

    // Collect data from the ExportViewModel
    val bowelMovements by exportViewModel.getAllBowelMovements().collectAsState(initial = emptyList())
    val dailyJournals by exportViewModel.getAllDailyJournals().collectAsState(initial = emptyList())
    val foodLogs by exportViewModel.getAllFoodLogs().collectAsState(initial = emptyList())
    val symptoms by exportViewModel.getAllSymptoms().collectAsState(initial = emptyList())

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
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                coroutineScope.launch {
                    generatePdf(context, bowelMovements, dailyJournals, foodLogs, symptoms)
                }
            }) {
                Text("Export PDF")
            }
        }
    }
}
