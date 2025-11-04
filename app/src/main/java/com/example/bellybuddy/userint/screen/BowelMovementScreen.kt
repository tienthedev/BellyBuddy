package com.example.bellybuddy.userint.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bellybuddy.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

// Data class for Bristol Stool Scale types
data class BristolType(
    val number: Int,
    val emoji: String, // Later will replace with custom icons, emojis are placeholders
    val description: String
)

// Enum for stool colors
enum class StoolColor(val displayName: String, val color: Color) {
    BROWN("Brown", Color(0xFF8B4513)),
    LIGHT_BROWN("Light Brown", Color(0xFFCD853F)),
    YELLOW("Yellow", Color(0xFFDAA520)),
    GREEN("Green", Color(0xFF6B8E23)),
    RED("Red", Color(0xFFDC143C)),
    BLACK("Black", Color(0xFF2C2C2C))
}

@Composable
fun BristolTypeButton(
    type: BristolType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) BellyGreenLight else Color.White,
            contentColor = Color.Black
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) BellyGreenDark else NeutralGray
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = type.emoji,
                fontSize = 28.sp
            )
            Text(
                text = "Type ${type.number}",
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun ColorCircleButton(
    stoolColor: StoolColor,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .size(60.dp)
                .aspectRatio(1f),
            shape = MaterialTheme.shapes.extraLarge,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = stoolColor.color,
                contentColor = Color.Transparent
            ),
            border = BorderStroke(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) BellyGreenDark else NeutralGray
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            // Empty content - just showing the color
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stoolColor.displayName,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = Color.Black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BowelMovementScreen(
    onBack: () -> Unit,
    onSelectBottom: (BottomItem) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Auto-fill with current date and time
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

    var selectedDate by remember { mutableStateOf(dateFormat.format(calendar.time)) }
    var selectedTime by remember { mutableStateOf(timeFormat.format(calendar.time)) }

    // Frequency counter
    var frequency by remember { mutableStateOf(1) }

    // Bristol Stool Scale selection
    var selectedBristolType by remember { mutableStateOf<Int?>(null) }

    // Stool color selection
    var selectedColor by remember { mutableStateOf<StoolColor?>(null) }

    // Urgency/Pain level
    var urgencyPainLevel by remember { mutableStateOf(0f) }

    // Blood and Mucus presence
    var hasBlood by remember { mutableStateOf(false) }
    var hasMucus by remember { mutableStateOf(false) }

    // Associated Symptoms
    var selectedSymptoms by remember { mutableStateOf(setOf<String>()) }

    // Notes
    var notes by remember { mutableStateOf("") }

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

    // Time picker dialog
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            val cal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }
            selectedTime = timeFormat.format(cal.time)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false // 12-hour format
    )
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Log Bowel Movement",
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
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // === Date & Time Section ===
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Date & Time",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Date Button
                    OutlinedButton(
                        onClick = { datePickerDialog.show() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(1.dp, NeutralGray)
                    ) {
                        Text(selectedDate, fontSize = 14.sp)
                    }

                    // Time Button
                    OutlinedButton(
                        onClick = { timePickerDialog.show() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(1.dp, NeutralGray)
                    ) {
                        Text(selectedTime, fontSize = 14.sp)
                    }
                }
            }

            HorizontalDivider(thickness = 1.dp, color = LightGray)
            Spacer(modifier = Modifier.height(16.dp))

            // === Frequency Counter ===
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Frequency",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Minus Button
                    OutlinedButton(
                        onClick = { if (frequency > 1) frequency-- },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(1.dp, NeutralGray),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("−", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }

                    // Counter Display
                    Text(
                        text = frequency.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    // Plus Button
                    OutlinedButton(
                        onClick = { frequency++ },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(1.dp, NeutralGray),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            HorizontalDivider(thickness = 1.dp, color = LightGray)
            Spacer(modifier = Modifier.height(16.dp))

            // === Bristol Stool Scale (Consistency) ===
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Consistency (Bristol Stool Scale)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Bristol types with emojis as placeholders
                // Replace emojis with custom icons later
                val bristolTypes = listOf(
                    BristolType(1, "🟤", "Hard Lumps"),
                    BristolType(2, "🌰", "Lumpy Sausage"),
                    BristolType(3, "🍠", "Cracked Sausage"),
                    BristolType(4, "🌭", "Smooth Sausage"),
                    BristolType(5, "🥔", "Soft Blobs"),
                    BristolType(6, "💧", "Mushy"),
                    BristolType(7, "💦", "Liquid")
                )

                // Two rows: 4 in first row, 3 in second row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    bristolTypes.take(4).forEach { type ->
                        BristolTypeButton(
                            type = type,
                            isSelected = selectedBristolType == type.number,
                            onClick = { selectedBristolType = type.number },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    bristolTypes.takeLast(3).forEach { type ->
                        BristolTypeButton(
                            type = type,
                            isSelected = selectedBristolType == type.number,
                            onClick = { selectedBristolType = type.number },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Empty spacer to align left
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            HorizontalDivider(thickness = 1.dp, color = LightGray)
            Spacer(modifier = Modifier.height(16.dp))

            // === Color Picker ===
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Color",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Single row of color circles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StoolColor.entries.forEach { stoolColor ->
                        ColorCircleButton(
                            stoolColor = stoolColor,
                            isSelected = selectedColor == stoolColor,
                            onClick = { selectedColor = stoolColor }
                        )
                    }
                }
            }

            HorizontalDivider(thickness = 1.dp, color = LightGray)
            Spacer(modifier = Modifier.height(16.dp))

            // === Urgency/Pain Slider ===
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Urgency / Pain",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )

                    // Value display in black circle
                    Box(
                        modifier = Modifier
                            .background(Color.Black, shape = MaterialTheme.shapes.medium)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = urgencyPainLevel.toInt().toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Slider(
                    value = urgencyPainLevel,
                    onValueChange = { urgencyPainLevel = it },
                    valueRange = 0f..10f,
                    steps = 9,
                    colors = SliderDefaults.colors(
                        thumbColor = BellyGreenDark,
                        activeTrackColor = BellyGreenDark,
                        inactiveTrackColor = LightGray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            HorizontalDivider(thickness = 1.dp, color = LightGray)
            Spacer(modifier = Modifier.height(16.dp))

            // === Blood / Mucus Checkboxes ===
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Blood Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(
                        checked = hasBlood,
                        onCheckedChange = { hasBlood = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = BellyGreenDark,
                            uncheckedColor = NeutralGray
                        )
                    )
                    Text(
                        text = "Blood",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }

                // Mucus Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Checkbox(
                        checked = hasMucus,
                        onCheckedChange = { hasMucus = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = BellyGreenDark,
                            uncheckedColor = NeutralGray
                        )
                    )
                    Text(
                        text = "Mucus",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }

            HorizontalDivider(thickness = 1.dp, color = LightGray)
            Spacer(modifier = Modifier.height(16.dp))

            // === Associated Symptoms ===
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Associated Symptoms",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                // Could connect to backend for dynamic list later
                // and with SymptomScreen
                val symptoms = listOf(
                    "Fatigue",
                    "Nausea",
                    "Cramping",
                    "Bloating",
                    "Loss of Appetite",
                    "Headache"
                )

                // Display symptoms in a grid-like layout (2 columns)
                symptoms.chunked(2).forEach { rowSymptoms ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowSymptoms.forEach { symptom ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Checkbox(
                                    checked = selectedSymptoms.contains(symptom),
                                    onCheckedChange = { isChecked ->
                                        selectedSymptoms = if (isChecked) {
                                            selectedSymptoms + symptom
                                        } else {
                                            selectedSymptoms - symptom
                                        }
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = BellyGreenDark,
                                        uncheckedColor = NeutralGray
                                    )
                                )
                                Text(
                                    text = symptom,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }
                        }
                        // Add empty space if odd number of symptoms in row
                        if (rowSymptoms.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            HorizontalDivider(thickness = 1.dp, color = LightGray)
            Spacer(modifier = Modifier.height(16.dp))

            // === Notes Field ===
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Notes (Optional)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = { Text("Add any additional details...", color = NeutralGray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BellyGreenDark,
                        unfocusedBorderColor = NeutralGray,
                        cursorColor = BellyGreenDark
                    ),
                    maxLines = 5
                )
            }

            HorizontalDivider(thickness = 1.dp, color = LightGray)
            Spacer(modifier = Modifier.height(24.dp))

            // === Save Button ===
            Button(
                onClick = {
                    // TODO: Save the bowel movement entry
                    // This will save all the data: date, time, frequency, bristol type,
                    // color, urgency/pain, blood, mucus, symptoms, notes
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BellyGreenDark,
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Save Entry",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}