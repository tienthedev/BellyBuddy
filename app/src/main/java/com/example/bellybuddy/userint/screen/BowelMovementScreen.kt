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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bellybuddy.R
import com.example.bellybuddy.data.model.BowelMovement
import com.example.bellybuddy.ui.theme.BellyGreenDark
import com.example.bellybuddy.ui.theme.BellyGreenLight
import com.example.bellybuddy.ui.theme.LightGray
import com.example.bellybuddy.ui.theme.NeutralGray
import com.example.bellybuddy.viewmodel.BowelMovementViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BowelMovementScreen(
    onBack: () -> Unit,
    onSelectBottom: (BottomItem) -> Unit,
    currentUserId: Int,
    viewModel: BowelMovementViewModel = viewModel()
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    val dateFormatDisplay = remember { SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()) }
    val timeFormatDisplay = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }
    val dateFormatDB = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val timeFormatDB = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }

    var selectedDate by remember { mutableStateOf(dateFormatDisplay.format(calendar.time)) }
    var selectedTime by remember { mutableStateOf(timeFormatDisplay.format(calendar.time)) }
    var selectedDateDB by remember { mutableStateOf(dateFormatDB.format(calendar.time)) }

    val savedBowelMovements by viewModel.getBowelMovementsByDate(selectedDateDB)
        .collectAsState(initial = emptyList())

    var selectedBristolType by remember { mutableStateOf<Int?>(null) }
    var selectedColor by remember { mutableStateOf<StoolColor?>(null) }
    var urgencyLevel by remember { mutableStateOf(0f) }
    var painLevel by remember { mutableStateOf(0f) }
    var hasBlood by remember { mutableStateOf(false) }
    var hasMucus by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf("") }

    val bristolTypes = remember {
        listOf(
            BristolType(1, R.drawable.bristol_type_1, "Hard lumps"),
            BristolType(2, R.drawable.bristol_type_2, "Lumpy"),
            BristolType(3, R.drawable.bristol_type_3, "Cracked"),
            BristolType(4, R.drawable.bristol_type_4, "Smooth"),
            BristolType(5, R.drawable.bristol_type_5, "Soft blobs"),
            BristolType(6, R.drawable.bristol_type_6, "Mushy"),
            BristolType(7, R.drawable.bristol_type_7, "Liquid")
        )
    }

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
                        text = "Log Bowel Movement",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            BottomToolBar(selected = BottomItem.Grid, onSelect = onSelectBottom)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .weight(0.62f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                SectionCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Date & Time",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { datePickerDialog.show() },
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Text(selectedDate)
                            }

                            OutlinedButton(
                                onClick = { timePickerDialog.show() },
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Text(selectedTime)
                            }
                        }
                    }
                }

                SectionCard {
                    Text(
                        text = "Consistency (Bristol Stool Scale)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            bristolTypes.take(4).forEach { type ->
                                BristolTypeCard(
                                    type = type,
                                    isSelected = selectedBristolType == type.number,
                                    onClick = { selectedBristolType = type.number },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            bristolTypes.takeLast(3).forEach { type ->
                                BristolTypeCard(
                                    type = type,
                                    isSelected = selectedBristolType == type.number,
                                    onClick = { selectedBristolType = type.number },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                SectionCard {
                    Text(
                        text = "Color",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        StoolColor.entries.forEach { stoolColor ->
                            ColorCircleOption(
                                stoolColor = stoolColor,
                                isSelected = selectedColor == stoolColor,
                                onClick = { selectedColor = stoolColor },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                SectionCard {
                    Text(
                        text = "Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LevelSliderRow(
                        title = "Pain Level",
                        value = painLevel,
                        onValueChange = { painLevel = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LevelSliderRow(
                        title = "Urgency Level",
                        value = urgencyLevel,
                        onValueChange = { urgencyLevel = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        LabeledCheckbox(
                            label = "Blood",
                            checked = hasBlood,
                            onCheckedChange = { hasBlood = it },
                            modifier = Modifier.weight(1f)
                        )

                        LabeledCheckbox(
                            label = "Mucus",
                            checked = hasMucus,
                            onCheckedChange = { hasMucus = it },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                SectionCard {
                    Text(
                        text = "Notes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 110.dp),
                        placeholder = {
                            Text("Add any additional details...")
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BellyGreenDark,
                            unfocusedBorderColor = NeutralGray,
                            cursorColor = BellyGreenDark
                        ),
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (selectedBristolType != null && selectedColor != null) {
                                val timeDB = try {
                                    val displayTime = timeFormatDisplay.parse(selectedTime)
                                    timeFormatDB.format(displayTime ?: Date())
                                } catch (e: Exception) {
                                    timeFormatDB.format(Date())
                                }

                                viewModel.insertBowelMovement(
                                    BowelMovement(
                                        userId = currentUserId,
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
                                )

                                selectedBristolType = null
                                selectedColor = null
                                painLevel = 0f
                                urgencyLevel = 0f
                                hasBlood = false
                                hasMucus = false
                                notes = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 54.dp),
                        enabled = selectedBristolType != null && selectedColor != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BellyGreenDark,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "Save Entry",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(0.38f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Today's Bowel Movements",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                if (savedBowelMovements.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No bowel movements logged today",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
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
private fun SectionCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, Color(0xFFE9E9E9))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
private fun BristolTypeCard(
    type: BristolType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.heightIn(min = 112.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) BellyGreenLight.copy(alpha = 0.35f) else Color.White
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.5.dp,
            color = if (isSelected) BellyGreenDark else Color(0xFFD7D7D7)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = type.iconRes),
                contentDescription = "Bristol Type ${type.number}",
                modifier = Modifier.size(42.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Type ${type.number}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = type.description,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                maxLines = 2
            )
        }
    }
}

@Composable
private fun ColorCircleOption(
    stoolColor: StoolColor,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = stoolColor.color,
            border = BorderStroke(
                width = if (isSelected) 3.dp else 1.5.dp,
                color = if (isSelected) BellyGreenDark else Color(0xFF8E8E8E)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {}

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stoolColor.displayName,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 2
        )
    }
}

@Composable
private fun LevelSliderRow(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.onSurface
            ) {
                Text(
                    text = value.toInt().toString(),
                    color = MaterialTheme.colorScheme.surface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            value = value,
            onValueChange = onValueChange,
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
}

@Composable
private fun LabeledCheckbox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, Color(0xFFE4E4E4))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = BellyGreenDark,
                    uncheckedColor = NeutralGray
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
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
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(1.dp, Color(0xFFE9E9E9))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${bowelMovement.consistency} - ${bowelMovement.color}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Time: ${formatTime(bowelMovement.time)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Text(
                    text = "Pain: ${bowelMovement.painLevel}/10 | Urgency: ${bowelMovement.urgencyLevel}/10",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                if (bowelMovement.blood || bowelMovement.mucus) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = buildString {
                            if (bowelMovement.blood) append("🔴 Blood ")
                            if (bowelMovement.mucus) append("💧 Mucus")
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                if (bowelMovement.notes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = bowelMovement.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
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