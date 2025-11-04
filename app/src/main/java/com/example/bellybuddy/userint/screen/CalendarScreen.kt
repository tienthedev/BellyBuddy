package com.example.bellybuddy.userint.screen

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bellybuddy.ui.theme.*
import java.util.*
import com.example.bellybuddy.userint.component.DailyScoreCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onSelectBottom: (BottomItem) -> Unit
) {
    var selectedDay by remember {
        mutableStateOf(
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
    }
    val currentMonth = remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH)) }
    val currentYear = remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Calendar",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            BottomToolBar(
                selected = BottomItem.Calendar,
                onSelect = onSelectBottom
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Month navigation header
                MonthNavigationHeader(
                    currentMonth = currentMonth.value,
                    currentYear = currentYear.value,
                    onPreviousMonth = {
                        if (currentMonth.value == 0) {
                            currentMonth.value = 11
                            currentYear.value -= 1
                        } else {
                            currentMonth.value -= 1
                        }
                    },
                    onNextMonth = {
                        if (currentMonth.value == 11) {
                            currentMonth.value = 0
                            currentYear.value += 1
                        } else {
                            currentMonth.value += 1
                        }
                    }
                )
            }

            item {
                // Calendar grid
                CalendarGrid(
                    month = currentMonth.value,
                    year = currentYear.value,
                    selectedDay = selectedDay,
                    onDaySelected = { selectedDay = it },
                    healthData = generateSampleHealthData(currentMonth.value, currentYear.value)
                )
            }

            item {
                Spacer(Modifier.height(8.dp))
            }

            item {
                val today = Calendar.getInstance()
                val isCurrentMonthAndYear = today.get(Calendar.MONTH) == currentMonth.value &&
                        today.get(Calendar.YEAR) == currentYear.value
                val todayDay = if (isCurrentMonthAndYear) today.get(Calendar.DAY_OF_MONTH) else -1

                val isFutureMonth = currentYear.value > today.get(Calendar.YEAR) ||
                        (currentYear.value == today.get(Calendar.YEAR) && currentMonth.value > today.get(Calendar.MONTH))
                val isFutureDate = isFutureMonth || (isCurrentMonthAndYear && selectedDay > todayDay)

                // Selected date info
                SelectedDateInfo(
                    day = selectedDay,
                    month = currentMonth.value,
                    year = currentYear.value,
                    healthData = if (isFutureDate) null else generateSampleHealthData(currentMonth.value, currentYear.value)[selectedDay]
                )
            }

            item {
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun MonthNavigationHeader(
    currentMonth: Int,
    currentYear: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = BellyGreenLight.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPreviousMonth,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BellyGreen.copy(alpha = 0.2f))
            ) {
                Text("‹", style = MaterialTheme.typography.headlineSmall, color = BellyGreen)
            }

            Text(
                text = "${monthNames[currentMonth]} $currentYear",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            IconButton(
                onClick = onNextMonth,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BellyGreen.copy(alpha = 0.2f))
            ) {
                Text("›", style = MaterialTheme.typography.headlineSmall, color = BellyGreen)
            }
        }
    }
}

@Composable
private fun CalendarGrid(
    month: Int,
    year: Int,
    selectedDay: Int,
    onDaySelected: (Int) -> Unit,
    healthData: Map<Int, HealthDayData>
) {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, 1)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    val today = Calendar.getInstance()
    val isCurrentMonthAndYear = today.get(Calendar.MONTH) == month && today.get(Calendar.YEAR) == year
    val todayDay = if (isCurrentMonthAndYear) today.get(Calendar.DAY_OF_MONTH) else -1

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Day labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Calendar grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.height(300.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Empty cells for days before the first day of the month
                items(firstDayOfWeek) {
                    Box(modifier = Modifier.aspectRatio(1f))
                }

                // Days of the month
                items(daysInMonth) { dayIndex ->
                    val day = dayIndex + 1
                    val isFutureDate = isCurrentMonthAndYear && day > todayDay

                    CalendarDayCell(
                        day = day,
                        isSelected = day == selectedDay,
                        isToday = isCurrentMonthAndYear && day == todayDay,
                        healthData = if (isFutureDate) null else healthData[day],
                        onDaySelected = { onDaySelected(day) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    day: Int,
    isSelected: Boolean,
    isToday: Boolean,
    healthData: HealthDayData?,
    onDaySelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(
                when {
                    isSelected -> BellyGreen
                    isToday -> BellyGreen.copy(alpha = 0.3f)
                    else -> Color.Transparent
                }
            )
            .clickable { onDaySelected() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = day.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                ),
                color = when {
                    isSelected -> Color.White
                    isToday -> BellyGreenDark
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )

            // Health indicator dot
            healthData?.let {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                it.score >= 80 -> SuccessGreen
                                it.score >= 60 -> WarningYellow
                                else -> ErrorRed
                            }
                        )
                )
            }
        }
    }
}

@Composable
private fun SelectedDateInfo(
    day: Int,
    month: Int,
    year: Int,
    healthData: HealthDayData?
) {
    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "${monthNames[month]} $day, $year",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(16.dp))

            healthData?.let { data ->
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        HealthMetricCard(
                            title = "Score",
                            value = data.score.toString(),
                            color = when {
                                data.score >= 80 -> SuccessGreen
                                data.score >= 60 -> WarningYellow
                                else -> ErrorRed
                            },
                            modifier = Modifier.weight(1f)
                        )

                        HealthMetricCard(
                            title = "Weight",
                            value = "%.1f lbs".format(data.weight),
                            color = BellyGreen,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Food Consumed Section
                    Column {
                        Text(
                            text = "Food Consumed",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "No food recorded for this day.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    // Supplements Section
                    Column {
                        Text(
                            text = "Supplements",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "No supplements recorded for this day.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            } ?: run {
                Text(
                    text = "No data recorded for this day",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun HealthMetricCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

data class HealthDayData(
    val score: Int,
    val weight: Double,
    val food: List<String>,
    val supplements: List<String>
)

private fun generateSampleHealthData(month: Int, year: Int): Map<Int, HealthDayData> {
    val data = mutableMapOf<Int, HealthDayData>()
    val today = Calendar.getInstance()
    val currentMonth = today.get(Calendar.MONTH)
    val currentYear = today.get(Calendar.YEAR)
    val currentDay = today.get(Calendar.DAY_OF_MONTH)

    val maxDay = when {
        year > currentYear -> 0
        year < currentYear -> 31
        month > currentMonth -> 0
        month < currentMonth -> 31
        else -> currentDay
    }

    for (day in 1..maxDay) {
        data[day] = HealthDayData(
            score = (60..95).random(),
            weight = (150..160).random() + Math.random(),
            food = emptyList(),
            supplements = emptyList()
        )
    }
    return data
}