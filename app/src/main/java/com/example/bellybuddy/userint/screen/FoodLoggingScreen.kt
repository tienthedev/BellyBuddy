package com.example.bellybuddy.userint.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bellybuddy.data.model.FoodLog
import com.example.bellybuddy.ui.theme.BellyGreenDark
import com.example.bellybuddy.viewmodel.FoodLogViewModel
import java.text.SimpleDateFormat
import java.util.*

enum class MealType(val displayName: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACK("Snack")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodLoggingScreen(
    onSelectBottom: (BottomItem) -> Unit,
    onBack: () -> Unit = {},
    viewModel: FoodLogViewModel = viewModel()
) {
    var showAddSheet by remember { mutableStateOf(false) }
    var editingEntry by remember { mutableStateOf<FoodLog?>(null) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }

    // Format date for database query (yyyy-MM-dd)
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val selectedDateString = dateFormat.format(selectedDate.time)

    // Collect food logs from database for selected date
    val foodLogs by viewModel.getFoodLogsByDate(selectedDateString)
        .collectAsState(initial = emptyList())

    val brand = Color(0xFF9DDB9E)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Food Log",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
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
                    containerColor = MaterialTheme.colorScheme.surface
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
                onClick = { showAddSheet = true },
                containerColor = brand,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Food Entry",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            DateSelector(
                selectedDate = selectedDate,
                onDateChange = { selectedDate = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = DividerDefaults.Thickness,
                color = DividerDefaults.color
            )

            if (foodLogs.isEmpty()) {
                EmptyStateMessage(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val groupedEntries = foodLogs.groupBy {
                        MealType.valueOf(it.mealType)
                    }

                    MealType.entries.forEach { mealType ->
                        val entriesForMeal = groupedEntries[mealType] ?: emptyList()
                        if (entriesForMeal.isNotEmpty()) {
                            item {
                                Text(
                                    text = mealType.displayName,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            items(entriesForMeal) { entry ->
                                FoodEntryCard(
                                    entry = entry,
                                    onEdit = { editingEntry = entry },
                                    onDelete = { viewModel.deleteFoodLog(entry) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Add Food Entry Sheet
        if (showAddSheet) {
            AddFoodEntrySheet(
                onDismiss = { showAddSheet = false },
                onSave = { foodName, mealType, portionSize, notes ->
                    // Format current time for database
                    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    val currentTime = timeFormat.format(Date())

                    val newEntry = FoodLog(
                        userId = 1,  // TODO: Get from actual logged-in user
                        date = selectedDateString,
                        time = currentTime,
                        foodName = foodName,
                        mealType = mealType.name,
                        portionSize = portionSize,
                        notes = notes,
                        timeCreated = System.currentTimeMillis(),
                        timeUpdated = System.currentTimeMillis()
                    )
                    viewModel.insertFoodLog(newEntry)
                    showAddSheet = false
                }
            )
        }

        // Edit Food Entry Sheet
        editingEntry?.let { entry ->
            AddFoodEntrySheet(
                existingEntry = entry,
                onDismiss = { editingEntry = null },
                onSave = { foodName, mealType, portionSize, notes ->
                    val updatedEntry = entry.copy(
                        foodName = foodName,
                        mealType = mealType.name,
                        portionSize = portionSize,
                        notes = notes,
                        timeUpdated = System.currentTimeMillis()
                    )
                    viewModel.updateFoodLog(updatedEntry)
                    editingEntry = null
                }
            )
        }
    }
}

@Composable
private fun DateSelector(
    selectedDate: Calendar,
    onDateChange: (Calendar) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = dateFormat.format(selectedDate.time),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = "View your meals",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = {
                val newDate = selectedDate.clone() as Calendar
                newDate.add(Calendar.DAY_OF_MONTH, -1)
                onDateChange(newDate)
            }) {
                Text("← Prev")
            }
            TextButton(onClick = {
                val newDate = selectedDate.clone() as Calendar
                newDate.add(Calendar.DAY_OF_MONTH, 1)
                onDateChange(newDate)
            }) {
                Text("Next →")
            }
        }
    }
}

@Composable
private fun FoodEntryCard(
    entry: FoodLog,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entry.foodName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Portion: ${entry.portionSize}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                // Display time from database
                Text(
                    text = formatTime(entry.time),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            if (entry.notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = entry.notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = onEdit) {
                    Text("Edit")
                }
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

// Helper function to format time from "HH:mm:ss" to "h:mm a"
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

@Composable
private fun EmptyStateMessage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No food logged yet",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap the + button to add your first meal",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    }
}