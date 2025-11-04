package com.example.bellybuddy.userint.screen

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bellybuddy.ui.theme.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SymptomScreen(
    onSelectBottom: (BottomItem) -> Unit,
    onAddSymptomClick: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // === Date state ===
    var selectedDate by remember { mutableStateOf("Select a date") }

    // Date picker dialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            selectedDate = "${month + 1}/$day/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // === Symptom list + slider levels ===
    var symptoms by remember {
        mutableStateOf(listOf("Bloating", "Diarrhea", "Constipation", "Abdominal Pain", "Irregular Stool", "Loss of Appetite", "Incomplete Evacuation"))
    }
    val symptomLevels = remember { mutableStateMapOf<String, Float>() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Symptom Tracker",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                },
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddSymptomClick,
                containerColor = BellyGreen,
                contentColor = Color.Black
            ) {
                Text("+", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
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
            // === Centered Date Button ===
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
                    Text(selectedDate)
                }
            }

            HorizontalDivider(thickness = 1.dp, color = LightGray)
            Spacer(modifier = Modifier.height(16.dp))

            // === Symptom List ===
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(symptoms) { symptom ->
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

            Button(
                onClick = { /* no backend yet */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BellyGreenDark,
                    contentColor = Color.Black
                )
            ) {
                Text("Save Symptoms", fontWeight = FontWeight.Bold)
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
                fontSize = 18.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            // === Slider with value label ===
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

            // === Rounded black circle showing current value ===
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .background(Color.Black, shape = MaterialTheme.shapes.medium)
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

