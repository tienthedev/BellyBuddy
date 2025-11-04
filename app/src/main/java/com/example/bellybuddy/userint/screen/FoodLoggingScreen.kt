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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bellybuddy.ui.theme.BellyGreenDark
import java.text.SimpleDateFormat
import java.util.*

// Temporary data class for Sprint 2 prototype
data class FoodEntry(
    val id: String = UUID.randomUUID().toString(),
    val foodName: String,
    val mealType: MealType,
    val portionSize: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)

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
    onBack: () -> Unit = {}
) {
    // For Sprint 2: In-memory list to demonstrate UI flow
    var foodEntries by remember { mutableStateOf(getSampleEntries()) }
    var showAddSheet by remember { mutableStateOf(false) }
    var editingEntry by remember { mutableStateOf<FoodEntry?>(null) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    val brand = Color(0xFF9DDB9E)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Food Log",
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
            // Date selector
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

            // Food entries list
            if (foodEntries.isEmpty()) {
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
                    // Group by meal type
                    val groupedEntries = foodEntries.groupBy { it.mealType }

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
                                    onDelete = { foodEntries = foodEntries - entry }
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
                onSave = { newEntry ->
                    foodEntries = foodEntries + newEntry
                    showAddSheet = false
                }
            )
        }

        // Edit Food Entry Sheet
        editingEntry?.let { entry ->
            AddFoodEntrySheet(
                existingEntry = entry,
                onDismiss = { editingEntry = null },
                onSave = { updatedEntry ->
                    foodEntries = foodEntries.map {
                        if (it.id == updatedEntry.id) updatedEntry else it
                    }
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

        // For Sprint 2: Navigate to previous/next day
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
    entry: FoodEntry,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

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

                Text(
                    text = timeFormat.format(Date(entry.timestamp)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            if (entry.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = entry.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }

            // Action buttons
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

// Sample data for Sprint 2 demonstration
private fun getSampleEntries(): List<FoodEntry> {
    return listOf(
        FoodEntry(
            foodName = "Oatmeal with Berries",
            mealType = MealType.BREAKFAST,
            portionSize = "1 bowl",
            description = "Made with almond milk, topped with blueberries and strawberries. No added sugar.",
            timestamp = System.currentTimeMillis() - 3600000 * 5
        ),
        FoodEntry(
            foodName = "Grilled Chicken Salad",
            mealType = MealType.LUNCH,
            portionSize = "1 plate",
            description = "Mixed greens, grilled chicken breast, olive oil dressing. From local cafe.",
            timestamp = System.currentTimeMillis() - 3600000 * 2
        )
    )
}