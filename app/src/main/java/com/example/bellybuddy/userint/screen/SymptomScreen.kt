package com.example.bellybuddy.userint.screen

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bellybuddy.data.model.Symptom
import com.example.bellybuddy.ui.theme.*
import com.example.bellybuddy.viewmodel.SymptomViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymptomScreen(
    onSelectBottom: (BottomItem) -> Unit,
    onAddSymptomClick: () -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: SymptomViewModel = viewModel()
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    // Date state
    var selectedDateString by remember { mutableStateOf(dateFormat.format(calendar.time)) }
    var displayDate by remember { mutableStateOf("Today") }

    // Get symptoms for selected date from database
    val savedSymptoms by viewModel.getSymptomsByDate(selectedDateString)
        .collectAsState(initial = emptyList())

    // Date picker dialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            calendar.set(year, month, day)
            selectedDateString = dateFormat.format(calendar.time)
            displayDate = "${month + 1}/$day/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Symptom options and slider levels
    val symptomOptions = listOf(
        "Bloating", "Diarrhea", "Constipation", "Abdominal Pain",
        "Irregular Stool", "Loss of Appetite", "Incomplete Evacuation"
    )
    val symptomLevels = remember { mutableStateMapOf<String, Float>() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Symptom Tracking", color = Color.Black) },
                navigationIcon = {
                    TextButton(
                        onClick = onBack,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = BellyGreenDark
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            BottomToolBar(
                selected = BottomItem.Grid,
                onSelect = onSelectBottom
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(16.dp)
        ) {
            // Date Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                OutlinedButton(
                    onClick = { datePickerDialog.show() },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(1.dp, NeutralGray)
                ) {
                    Text(displayDate)
                }
            }

            HorizontalDivider(thickness = 1.dp, color = LightGray)
            Spacer(modifier = Modifier.height(16.dp))

            // Symptom Input Section
            Text(
                "Log New Symptoms",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(symptomOptions) { symptom ->
                    SymptomSliderCard(
                        symptom = symptom,
                        level = symptomLevels[symptom] ?: 0f,
                        onLevelChange = { newValue ->
                            symptomLevels[symptom] = newValue
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Save Button
            Button(
                onClick = {
                    val currentTime = timeFormat.format(Date())
                    val symptomsToSave = symptomLevels
                        .filter { it.value > 0 }
                        .map { (name, level) ->
                            Symptom(
                                userId = 1,
                                date = selectedDateString,
                                time = currentTime,
                                symptomType = name,
                                intensity = level.toInt()
                            )
                        }

                    if (symptomsToSave.isNotEmpty()) {
                        viewModel.insertMultipleSymptoms(symptomsToSave) { }
                        symptomLevels.clear()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BellyGreenDark,
                    contentColor = Color.White
                )
            ) {
                Text("Save Symptoms", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = LightGray)
            Spacer(modifier = Modifier.height(16.dp))

            // Saved Symptoms Section
            Text(
                "Saved Symptoms for $displayDate",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (savedSymptoms.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No symptoms logged for this date",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(savedSymptoms) { symptom ->
                        SavedSymptomCard(
                            symptom = symptom,
                            onDelete = { viewModel.deleteSymptom(symptom) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SymptomSliderCard(
    symptom: String,
    level: Float,
    onLevelChange: (Float) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = symptom,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            Slider(
                value = level,
                onValueChange = onLevelChange,
                valueRange = 0f..10f,
                steps = 9,
                colors = SliderDefaults.colors(
                    thumbColor = BellyGreenDark,
                    activeTrackColor = BellyGreenDark,
                    inactiveTrackColor = LightGray
                ),
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .background(Color.Black, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = level.toInt().toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun SavedSymptomCard(
    symptom: Symptom,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, LightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = symptom.symptomType,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Intensity: ${symptom.intensity}/10",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete symptom",
                    tint = Color.Red
                )
            }
        }
    }
}
