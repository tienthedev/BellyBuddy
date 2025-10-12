package com.example.bellybuddy.userint.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodEntrySheet(
    existingEntry: FoodEntry? = null,
    onDismiss: () -> Unit,
    onSave: (FoodEntry) -> Unit
) {
    var foodName by remember { mutableStateOf(existingEntry?.foodName ?: "") }
    var selectedMealType by remember { mutableStateOf(existingEntry?.mealType ?: MealType.BREAKFAST) }
    var portionSize by remember { mutableStateOf(existingEntry?.portionSize ?: "") }
    var description by remember { mutableStateOf(existingEntry?.description ?: "") }
    var expanded by remember { mutableStateOf(false) }

    val brand = Color(0xFF9DDB9E)

    BackHandler { onDismiss() }

    Box(modifier = Modifier.fillMaxSize()) {
        // Scrim/backdrop
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { onDismiss() }
            )
        }

        // Bottom sheet
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Surface(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .clickable(enabled = false) {} // Prevent click-through
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    // Header
                    Text(
                        text = if (existingEntry != null) "Edit Food Entry" else "Add Food Entry",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = if (existingEntry != null) "Update your food log" else "Log what you ate",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Food Name
                    OutlinedTextField(
                        value = foodName,
                        onValueChange = { foodName = it },
                        label = { Text("Food Name *") },
                        placeholder = { Text("e.g., Grilled Chicken Salad") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Meal Type Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedMealType.displayName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Meal Type *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(12.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            MealType.entries.forEach { mealType ->
                                DropdownMenuItem(
                                    text = { Text(mealType.displayName) },
                                    onClick = {
                                        selectedMealType = mealType
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Portion Size
                    OutlinedTextField(
                        value = portionSize,
                        onValueChange = { portionSize = it },
                        label = { Text("Portion Size *") },
                        placeholder = { Text("e.g., 1 cup, 2 slices, 1 serving") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description/Notes
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Notes (Optional)") },
                        placeholder = {
                            Text("How was it prepared? Seasonings? Where from?")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        minLines = 5,
                        maxLines = 8,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Helper text
                    Text(
                        text = "Add details like cooking method, seasonings, restaurant name, or any other relevant info",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.padding(start = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Timestamp info
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = brand.copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Logged Time",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = if (existingEntry != null) {
                                        SimpleDateFormat("h:mm a", Locale.getDefault())
                                            .format(Date(existingEntry.timestamp))
                                    } else "Now",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                            Text(
                                text = if (existingEntry != null) "Original time" else "Auto-captured",
                                style = MaterialTheme.typography.bodySmall,
                                color = brand
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                if (foodName.isNotBlank() && portionSize.isNotBlank()) {
                                    val entry = existingEntry?.copy(
                                        foodName = foodName.trim(),
                                        mealType = selectedMealType,
                                        portionSize = portionSize.trim(),
                                        description = description.trim()
                                    )
                                        ?: FoodEntry(
                                            foodName = foodName.trim(),
                                            mealType = selectedMealType,
                                            portionSize = portionSize.trim(),
                                            description = description.trim(),
                                            timestamp = System.currentTimeMillis()
                                        )
                                    onSave(entry)
                                }
                            },
                            enabled = foodName.isNotBlank() && portionSize.isNotBlank(),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = brand,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                if (existingEntry != null) "Update Entry" else "Save Entry",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}