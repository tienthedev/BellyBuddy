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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import com.example.bellybuddy.ui.theme.*
import com.example.bellybuddy.R
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.foundation.BorderStroke
import com.example.bellybuddy.userint.component.DailyScoreCard
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bellybuddy.viewmodel.DailyJournalViewModel
import com.example.bellybuddy.viewmodel.FoodLogViewModel
import com.example.bellybuddy.viewmodel.SymptomViewModel
import com.example.bellybuddy.viewmodel.BowelMovementViewModel
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onSelectBottom: (BottomItem) -> Unit,
    journalViewModel: DailyJournalViewModel = viewModel(),
    foodViewModel: FoodLogViewModel = viewModel(),
    symptomViewModel: SymptomViewModel = viewModel(),
    bowelViewModel: BowelMovementViewModel = viewModel()
) {
    var selectedDay by remember {
        mutableStateOf(
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
    }
    val currentMonth = remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH)) }
    val currentYear = remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }

    // Format the selected date to match database format (yyyy-MM-dd)
    val selectedDateString = remember(selectedDay, currentMonth.value, currentYear.value) {
        val calendar = Calendar.getInstance()
        calendar.set(currentYear.value, currentMonth.value, selectedDay)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.format(calendar.time)
    }

    // Get journal entry for the selected date
    val journalEntry by journalViewModel.getJournalEntryByDate(selectedDateString)
        .collectAsState(initial = null)

    // Get food logs for the selected date
    val foodLogs by foodViewModel.getFoodLogsByDate(selectedDateString)
        .collectAsState(initial = emptyList())

    // Get symptoms for the selected date
    val symptoms by symptomViewModel.getSymptomsByDate(selectedDateString)
        .collectAsState(initial = emptyList())

    // Get bowel movements for the selected date
    val bowelMovements by bowelViewModel.getBowelMovementsByDate(selectedDateString)
        .collectAsState(initial = emptyList())

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
                    healthData = if (isFutureDate) null else generateSampleHealthData(currentMonth.value, currentYear.value)[selectedDay],
                    journalEntry = journalEntry,
                    foodLogs = foodLogs,
                    symptoms = symptoms,
                    bowelMovements = bowelMovements
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

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.5.dp, BellyGreen.copy(alpha = 0.8f)),
        colors = CardDefaults.outlinedCardColors(
            containerColor = BellyGreenLight.copy(alpha = 0.2f),
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedIconButton(
                onClick = onPreviousMonth,
                modifier = Modifier.size(44.dp),
                border = BorderStroke(1.5.dp, BellyGreen.copy(alpha = 0.8f)),
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    containerColor = BellyGreenLight.copy(alpha = 0.25f),
                    contentColor = BellyGreenDark
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = "Previous month",
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "${monthNames[currentMonth]} $currentYear",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedIconButton(
                onClick = onNextMonth,
                modifier = Modifier.size(44.dp),
                border = BorderStroke(1.5.dp, BellyGreen.copy(alpha = 0.8f)),
                colors = IconButtonDefaults.outlinedIconButtonColors(
                    containerColor = BellyGreenLight.copy(alpha = 0.25f),
                    contentColor = BellyGreenDark
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = "Next month",
                    modifier = Modifier.size(24.dp)
                )
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
    val shape = RoundedCornerShape(12.dp)
    val borderColor = when {
        isSelected -> BellyGreenDark
        isToday    -> BellyGreen.copy(alpha = 0.6f)
        else       -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
    }
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .border(borderWidth, borderColor, shape)
            .background(
                color = when {
                    isSelected -> BellyGreen
                    isToday    -> BellyGreen.copy(alpha = 0.15f)
                    else       -> Color.Transparent
                },
                shape = shape
            )
            .clickable { onDaySelected() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = day.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                ),
                color = when {
                    isSelected -> Color.White
                    isToday    -> BellyGreenDark
                    else       -> MaterialTheme.colorScheme.onSurface
                }
            )
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
    healthData: HealthDayData?,
    journalEntry: com.example.bellybuddy.data.model.DailyJournal?,
    foodLogs: List<com.example.bellybuddy.data.model.FoodLog>,
    symptoms: List<com.example.bellybuddy.data.model.Symptom>,
    bowelMovements: List<com.example.bellybuddy.data.model.BowelMovement>
) {
    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    var selectedTab by remember { mutableStateOf(DayInfoTab.Food) }

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

                    // Circle tab buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DayInfoTab.values().forEach { tab ->
                            CircleTabButton(
                                label = tab.label,
                                isSelected = selectedTab == tab,
                                onClick = { selectedTab = tab }
                            )
                            if (tab != DayInfoTab.values().last()) {
                                Spacer(Modifier.width(12.dp))
                            }
                        }
                    }

                    // Content based on selected tab
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = BellyGreenLight.copy(alpha = 0.1f)
                        )
                    ) {
                        Box(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            when (selectedTab) {
                                DayInfoTab.Food -> {
                                    // Display real food data from database
                                    if (foodLogs.isEmpty()) {
                                        Text(
                                            text = "No food recorded for this day.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    } else {
                                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                            // Group food logs by meal type
                                            val groupedFoods = foodLogs.groupBy { it.mealType }

                                            // Display meals in order
                                            listOf("BREAKFAST", "LUNCH", "DINNER", "SNACK").forEach { mealType ->
                                                groupedFoods[mealType]?.let { foods ->
                                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                                        Text(
                                                            text = mealType.lowercase().replaceFirstChar { it.uppercase() },
                                                            style = MaterialTheme.typography.titleSmall.copy(
                                                                fontWeight = FontWeight.Bold
                                                            ),
                                                            color = BellyGreenDark
                                                        )
                                                        foods.forEach { food ->
                                                            Row(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                horizontalArrangement = Arrangement.SpaceBetween
                                                            ) {
                                                                Text(
                                                                    text = "• ${food.foodName}",
                                                                    style = MaterialTheme.typography.bodyMedium,
                                                                    modifier = Modifier.weight(1f)
                                                                )
                                                                Text(
                                                                    text = food.portionSize,
                                                                    style = MaterialTheme.typography.bodySmall,
                                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                DayInfoTab.Symptoms -> {
                                    // Display real symptoms data from database
                                    if (symptoms.isEmpty()) {
                                        Text(
                                            text = "No symptoms recorded for this day.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    } else {
                                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                            symptoms.forEach { symptom ->
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = "• ${symptom.symptomType}",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        modifier = Modifier.weight(1f)
                                                    )
                                                    Surface(
                                                        shape = RoundedCornerShape(8.dp),
                                                        color = when {
                                                            symptom.intensity >= 8 -> ErrorRed.copy(alpha = 0.2f)
                                                            symptom.intensity >= 5 -> WarningYellow.copy(alpha = 0.2f)
                                                            else -> SuccessGreen.copy(alpha = 0.2f)
                                                        }
                                                    ) {
                                                        Text(
                                                            text = "${symptom.intensity}/10",
                                                            style = MaterialTheme.typography.bodySmall.copy(
                                                                fontWeight = FontWeight.Bold
                                                            ),
                                                            color = when {
                                                                symptom.intensity >= 8 -> ErrorRed
                                                                symptom.intensity >= 5 -> WarningYellow.copy(alpha = 0.8f)
                                                                else -> SuccessGreen
                                                            },
                                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                DayInfoTab.Bowel -> {
                                    // Display real bowel movement data from database
                                    if (bowelMovements.isEmpty()) {
                                        Text(
                                            text = "No bowel movements recorded for this day.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    } else {
                                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                            bowelMovements.forEach { bm ->
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(
                                                            color = BellyGreenLight.copy(alpha = 0.1f),
                                                            shape = RoundedCornerShape(8.dp)
                                                        )
                                                        .padding(12.dp),
                                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    // Time and main info
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            text = formatBowelTime(bm.time),
                                                            style = MaterialTheme.typography.titleSmall.copy(
                                                                fontWeight = FontWeight.Bold
                                                            ),
                                                            color = BellyGreenDark
                                                        )
                                                        Text(
                                                            text = "${bm.consistency}",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                                        )
                                                    }

                                                    // Color
                                                    Text(
                                                        text = "Color: ${bm.color}",
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )

                                                    // Pain and Urgency levels
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                                    ) {
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Text(
                                                                text = "Pain: ",
                                                                style = MaterialTheme.typography.bodySmall,
                                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                                            )
                                                            Surface(
                                                                shape = RoundedCornerShape(4.dp),
                                                                color = when {
                                                                    bm.painLevel >= 7 -> ErrorRed.copy(alpha = 0.2f)
                                                                    bm.painLevel >= 4 -> WarningYellow.copy(alpha = 0.2f)
                                                                    else -> SuccessGreen.copy(alpha = 0.2f)
                                                                }
                                                            ) {
                                                                Text(
                                                                    text = "${bm.painLevel}/10",
                                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                                        fontWeight = FontWeight.Bold
                                                                    ),
                                                                    color = when {
                                                                        bm.painLevel >= 7 -> ErrorRed
                                                                        bm.painLevel >= 4 -> WarningYellow.copy(alpha = 0.8f)
                                                                        else -> SuccessGreen
                                                                    },
                                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                                )
                                                            }
                                                        }

                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Text(
                                                                text = "Urgency: ",
                                                                style = MaterialTheme.typography.bodySmall,
                                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                                            )
                                                            Surface(
                                                                shape = RoundedCornerShape(4.dp),
                                                                color = when {
                                                                    bm.urgencyLevel >= 7 -> ErrorRed.copy(alpha = 0.2f)
                                                                    bm.urgencyLevel >= 4 -> WarningYellow.copy(alpha = 0.2f)
                                                                    else -> SuccessGreen.copy(alpha = 0.2f)
                                                                }
                                                            ) {
                                                                Text(
                                                                    text = "${bm.urgencyLevel}/10",
                                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                                        fontWeight = FontWeight.Bold
                                                                    ),
                                                                    color = when {
                                                                        bm.urgencyLevel >= 7 -> ErrorRed
                                                                        bm.urgencyLevel >= 4 -> WarningYellow.copy(alpha = 0.8f)
                                                                        else -> SuccessGreen
                                                                    },
                                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                                )
                                                            }
                                                        }
                                                    }

                                                    // Blood and Mucus indicators
                                                    if (bm.blood || bm.mucus) {
                                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                            if (bm.blood) {
                                                                Surface(
                                                                    shape = RoundedCornerShape(4.dp),
                                                                    color = ErrorRed.copy(alpha = 0.15f)
                                                                ) {
                                                                    Text(
                                                                        text = "🔴 Blood",
                                                                        style = MaterialTheme.typography.bodySmall,
                                                                        color = ErrorRed,
                                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                                    )
                                                                }
                                                            }
                                                            if (bm.mucus) {
                                                                Surface(
                                                                    shape = RoundedCornerShape(4.dp),
                                                                    color = WarningYellow.copy(alpha = 0.15f)
                                                                ) {
                                                                    Text(
                                                                        text = "💧 Mucus",
                                                                        style = MaterialTheme.typography.bodySmall,
                                                                        color = WarningYellow.copy(alpha = 0.9f),
                                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }

                                                    // Notes if present
                                                    if (bm.notes.isNotEmpty()) {
                                                        Text(
                                                            text = "Note: ${bm.notes}",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                DayInfoTab.Journal -> {
                                    // Display real journal data from database
                                    if (journalEntry != null) {
                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Text(
                                                text = journalEntry.notes,
                                                style = MaterialTheme.typography.bodyMedium,
                                                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                                            )
                                        }
                                    } else {
                                        Text(
                                            text = "No journal notes recorded for this day.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            }
                        }
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
private fun CircleTabButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) BellyGreen else BellyGreenLight.copy(alpha = 0.3f)
                ),
            contentAlignment = Alignment.Center
        ) {
            val iconRes = getIconResourceForTab(label)
            if (iconRes != null) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = label,
                    modifier = Modifier.size(32.dp),
                    tint = Color.Unspecified  // This preserves the original PNG colors
                )
            } else {
                Text(
                    text = label.firstOrNull()?.uppercase() ?: "",
                    style = MaterialTheme.typography.headlineMedium,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) BellyGreen else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

private fun getIconResourceForTab(label: String): Int? {
    return when (label) {
        "Food" -> R.drawable.ic_food
        "Symptoms" -> R.drawable.ic_symptoms
        "Bowel" -> R.drawable.ic_toilet
        "Journal" -> R.drawable.ic_journal
        else -> null
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

enum class DayInfoTab(val label: String) {
    Food("Food"),
    Symptoms("Symptoms"),
    Bowel("Bowel"),
    Journal("Journal")
}

data class HealthDayData(
    val score: Int,
    val weight: Double,
    val food: List<String>,
    val symptoms: List<String>,
    val bowel: List<String>,
    val journal: List<String>
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
            symptoms = emptyList(),
            bowel = emptyList(),
            journal = emptyList()
        )
    }
    return data
}

// Helper function to format bowel movement time
private fun formatBowelTime(time: String): String {
    return try {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val date = inputFormat.parse(time)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        time
    }
}