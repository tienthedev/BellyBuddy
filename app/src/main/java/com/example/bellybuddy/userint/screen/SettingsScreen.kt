package com.example.bellybuddy.userint.screen

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    userViewModel: UserViewModel,
    onSelectBottom: (BottomItem) -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val coroutineScope = rememberCoroutineScope()

    var isExporting by remember { mutableStateOf(false) }
    var exportProgress by remember { mutableStateOf(0f) }

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

    // Collect data
    val loggedInUser by userViewModel.loggedInUser.collectAsState()
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
                selected = BottomItem.Settings,
                onSelect = onSelectBottom
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Settings", style = MaterialTheme.typography.headlineSmall)
            
            Spacer(modifier = Modifier.height(32.dp))

            if (isExporting) {
                Text("Generating Beautiful Report... ${(exportProgress * 100).toInt()}%")
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { exportProgress },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        isExporting = true
                        exportProgress = 0f
                        coroutineScope.launch {
                            generatePdf(
                                context,
                                loggedInUser,
                                bowelMovements,
                                dailyJournals,
                                foodLogs,
                                symptoms
                            ) { progress ->
                                exportProgress = progress
                            }
                            isExporting = false
                            Toast.makeText(context, "Report Exported to Downloads", Toast.LENGTH_LONG).show()
                        }
                    }
                ) {
                    Text("Export Health Report (PDF)")
                }
            }
        }
    }
}
