package com.example.bellybuddy.userint.screen

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bellybuddy.R
import com.example.bellybuddy.data.model.BowelMovement
import com.example.bellybuddy.ui.theme.*
import com.example.bellybuddy.viewmodel.BowelMovementViewModel
import java.text.SimpleDateFormat
import java.util.*

data class BristolType(
    val number: Int,
    val iconRes: Int,
    val description: String
)

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
        // FIX: heightIn instead of height(80.dp) so it flexes on smaller screens
        modifier = modifier.heightIn(min = 72.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            // FIX: theme surface instead of hardcoded Color.White
            containerColor = if (isSelected) BellyGreenLight else MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
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
            Image(
                painter = painterResource(id = type.iconRes),
                contentDescription = "Bristol Type ${type.number}",
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
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
        ) {}

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stoolColor.displayName,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            // FIX: theme color instead of hardcoded Color.Black
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BowelMovementScreen(
    onBack: () -> Unit,
    onSelectBottom: (BottomItem) -> Unit,
    viewModel: BowelMovementViewModel = viewModel()
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val dateFormatDisplay = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val timeFormatDisplay = SimpleDateFormat("h:mm a", Locale.getDefault())
    val dateFormatDB = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val timeFormatDB = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    var selectedDate by remember { mutableStateOf(dateFormatDisplay.format(calendar.time)) }
    var selectedTime by remember { mutableStateOf(timeFormatDisplay.format(calendar.time)) }
    var selectedDateDB by remember { mutableStateOf(dateFormatDB.format(calendar.time)) }

    val savedBowelMovements by viewModel.getBowelMovementsByDate(selectedDateDB)
        .collectAsState(initial = emptyList())

    var frequency by remember { mutableStateOf(1) }
    var selectedBristolType by remember { mutableStateOf<Int?>(null) }
    var selectedColor by remember { mutableStateOf<StoolColor?>(null) }
    var urgencyLevel by remember { mutableStateOf(0f) }
    var painLevel by remember { mutableStateOf(0f) }
    var hasBlood by remember { mutableStateOf(false) }
    var hasMucus by remember { mutableStateOf(false) }
    var selectedSymptoms by remember { mutableStateOf(setOf<String>()) }
    var notes by remember { mutableStateOf("") }

    val bristolTypes = listOf(
        BristolType(1, R.drawable.bristol_type_1, "Hard Lumps"),
        BristolType(2, R.drawable.bristol_type_2, "Lumpy Sausage"),
        BristolType(3, R.drawable.bristol_type_3, "Cracked Sausage"),
        BristolType(4, R.drawable.bristol_type_4, "Smooth Sausage"),
        BristolType(5, R.drawable.bristol_type_5, "Soft Blobs"),
        BristolType(6, R.drawable.bristol_type_6, "Mushy"),
        BristolType(7, R.drawable.bristol_type_7, "Liquid")
    )

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            calendar.set(year, month, day)
            selectedDate = dateFormatDisplay.format(calendar.time)
            selectedDateDB = dateFormatDB.format(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            val cal = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }
            selectedTime = timeFormatDisplay.format(cal.time)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Log Bowel Movement",
                        // FIX: theme color instead of hardcoded Color.Black
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    TextButton(
                        onClick = onBack,
                        colors = ButtonDefaults.textButtonColors(contentColor = BellyGreenDark)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    // FIX: theme surface instead of hardcoded Color.White
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            BottomToolBar(selected = BottomItem.Grid, onSelect = onSelectBottom)
        },
        // FIX: theme surface instead of hardcoded Color.White
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
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
                        // FIX: theme color
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { datePickerDialog.show() },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            border = BorderStroke(1.dp, NeutralGray)
                        ) { Text(selectedDate, fontSize = 14.sp) }

                        OutlinedButton(
                            onClick = { timePickerDialog.show() },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            border = BorderStroke(1.dp, NeutralGray)
                        ) { Text(selectedTime, fontSize = 14.sp) }
                    }
                }

                HorizontalDivider(thickness = 1.dp, color = LightGray)
                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Text(
                        text = "Consistency (Bristol Stool Scale)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
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
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                HorizontalDivider(thickness = 1.dp, color = LightGray)
                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Text(
                        text = "Color",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
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

                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pain Level",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.onSurface, shape = MaterialTheme.shapes.medium)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = painLevel.toInt().toString(),
                                color = MaterialTheme.colorScheme.surface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Slider(
                        value = painLevel,
                        onValueChange = { painLevel = it },
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

                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Urgency Level",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.onSurface, shape = MaterialTheme.shapes.medium)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = urgencyLevel.toInt().toString(),
                                color = MaterialTheme.colorScheme.surface,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Slider(
                        value = urgencyLevel,
                        onValueChange = { urgencyLevel = it },
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

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Checkbox(
                            checked = hasBlood,
                            onCheckedChange = { hasBlood = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = BellyGreenDark,
                                uncheckedColor = NeutralGray
                            )
                        )
                        Text(text = "Blood", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Checkbox(
                            checked = hasMucus,
                            onCheckedChange = { hasMucus = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = BellyGreenDark,
                                uncheckedColor = NeutralGray
                            )
                        )
                        Text(text = "Mucus", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }

                HorizontalDivider(thickness = 1.dp, color = LightGray)
                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Text(
                        text = "Notes (Optional)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        // FIX: heightIn instead of height(100.dp)
                        modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
                        placeholder = { Text("Add any additional details...", color = NeutralGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BellyGreenDark,
                            unfocusedBorderColor = NeutralGray,
                            cursorColor = BellyGreenDark
                        ),
                        maxLines = 4
                    )
                }

                Button(
                    onClick = {
                        if (selectedBristolType != null && selectedColor != null) {
                            val timeDB = try {
                                val displayTime = timeFormatDisplay.parse(selectedTime)
                                timeFormatDB.format(displayTime ?: Date())
                            } catch (e: Exception) {
                                timeFormatDB.format(Date())
                            }
                            val bowelMovement = BowelMovement(
                                userId = 1,
                                date = selectedDateDB,
                                time = timeDB,
                                consistency = "Type $selectedBristolType",
                                color = selectedColor!!.displayName,
                                painLevel = painLevel.toInt(),
                                urgencyLevel = urgencyLevel.toInt(),
                                blood = hasBlood,
                                mucus = hasMucus,
                                notes = notes
                            )
                            viewModel.insertBowelMovement(bowelMovement)
                            selectedBristolType = null
                            selectedColor = null
                            painLevel = 0f
                            urgencyLevel = 0f
                            hasBlood = false
                            hasMucus = false
                            notes = ""
                        }
                    },
                    // FIX: heightIn instead of height(56.dp)
                    modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                    enabled = selectedBristolType != null && selectedColor != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BellyGreenDark,
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = "Save Entry", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            HorizontalDivider(thickness = 2.dp, color = LightGray)

            Column(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    "Today's Bowel Movements",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                if (savedBowelMovements.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "No bowel movements logged today",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(savedBowelMovements) { bm ->
                            SavedBowelMovementCard(
                                bowelMovement = bm,
                                onDelete = { viewModel.deleteBowelMovement(bm) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SavedBowelMovementCard(
    bowelMovement: BowelMovement,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        // FIX: theme surface
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, LightGray)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${bowelMovement.consistency} - ${bowelMovement.color}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    // FIX: theme color
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Time: ${formatTime(bowelMovement.time)}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = "Pain: ${bowelMovement.painLevel}/10 | Urgency: ${bowelMovement.urgencyLevel}/10",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                if (bowelMovement.blood || bowelMovement.mucus) {
                    Text(
                        text = buildString {
                            if (bowelMovement.blood) append("🔴 Blood ")
                            if (bowelMovement.mucus) append("💧 Mucus")
                        },
                        fontSize = 14.sp,
                        color = Color.Red
                    )
                }
                if (bowelMovement.notes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = bowelMovement.notes,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
            }
        }
    }
}

private fun formatTime(time: String): String {
    return try {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val date = inputFormat.parse(time)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        time
    }
}
