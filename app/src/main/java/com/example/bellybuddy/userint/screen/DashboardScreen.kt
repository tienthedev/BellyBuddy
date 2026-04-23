package com.example.bellybuddy.userint.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bellybuddy.R
import com.example.bellybuddy.data.model.DailyJournal
import com.example.bellybuddy.ui.theme.BellyGreen
import com.example.bellybuddy.ui.theme.BellyGreenDark
import com.example.bellybuddy.userint.component.DailyScoreCard
import com.example.bellybuddy.userint.component.ReminderCard
import com.example.bellybuddy.userint.component.WeightCard
import com.example.bellybuddy.viewmodel.DailyJournalViewModel
import com.example.bellybuddy.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userViewModel: UserViewModel,
    onProfileClick: (() -> Unit)? = null,
    onBottomSelect: (BottomItem) -> Unit,
    onLogout: (() -> Unit)? = null,
    onDailyScoreClick: (() -> Unit)? = null,
    onWeightClick: (() -> Unit)? = null,
    onReminderClick: (() -> Unit)? = null,
    score: Int = 88
) {
    val journalViewModel: DailyJournalViewModel = viewModel()
    val loggedInUser by userViewModel.loggedInUser.collectAsState()

    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val greeting = when (hour) {
        in 0..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }

    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val today = remember { dateFormat.format(Date()) }
    val todaysEntry by journalViewModel.getJournalEntryByDate(today).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.bellybuddylogo),
                        contentDescription = "BellyBuddy Logo",
                        modifier = Modifier
                            .offset(x = (-8).dp, y = 8.dp)
                            .size(85.dp)
                    )
                },
                actions = {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(50.dp)
                            .clickable { onProfileClick?.invoke() }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.profile_photo),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomToolBar(selected = BottomItem.Home, onSelect = onBottomSelect)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            Spacer(Modifier.height(3.dp))

            Text(
                text = "$greeting, ${loggedInUser?.name ?: "User"}",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = "Today's Status",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // FIX: heightIn instead of height(150.dp) so cards grow with font size
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                DailyScoreCard(
                    score = score,
                    showLabel = true,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = 8.dp),
                    onClick = { onDailyScoreClick?.invoke() }
                )

                WeightCard(
                    title = "Weight",
                    value = if (loggedInUser?.weight != null) "${loggedInUser?.weight} lbs" else "-- lbs",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 8.dp),
                    onClick = { onWeightClick?.invoke() }
                )
            }

            Spacer(Modifier.height(16.dp))

            // FIX: heightIn instead of height(150.dp)
            ReminderCard(
                title = "Reminder",
                message = "Did you take your supplements?",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                onClick = { onReminderClick?.invoke() }
            )

            Spacer(Modifier.height(16.dp))

            // FIX: heightIn instead of height(230.dp)
            DailyJournalCard(
                todayKey = today,
                initialText = todaysEntry?.notes.orEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp),
                onSave = { text ->
                    val trimmed = text.trim()
                    if (trimmed.isEmpty()) return@DailyJournalCard
                    val existing = todaysEntry
                    if (existing != null) {
                        journalViewModel.updateJournalEntry(
                            existing.copy(notes = trimmed, timeUpdated = System.currentTimeMillis())
                        )
                    } else {
                        journalViewModel.insertJournalEntry(
                            DailyJournal(
                                userId = loggedInUser?.id ?: 0,
                                date = today,
                                mood = "",
                                notes = trimmed
                            )
                        )
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            TodayStatusCards(
                foodItems = listOf("Oatmeal", "Grilled Chicken", "Salad"),
                symptoms = listOf("Bloating", "Headache"),
                bowelMovements = listOf("Morning - Normal"),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun DailyJournalCard(
    todayKey: String,
    initialText: String,
    modifier: Modifier = Modifier,
    onSave: (String) -> Unit
) {
    var journalText by rememberSaveable(todayKey) { mutableStateOf(initialText) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Daily Journal",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = journalText,
                onValueChange = { journalText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                placeholder = { Text("Write about your day...") },
                shape = RoundedCornerShape(18.dp),
                singleLine = false,
                maxLines = 8,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
                    val trimmed = journalText.trim()
                    if (trimmed.isNotEmpty()) onSave(trimmed)
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BellyGreen,
                    contentColor = Color.White
                ),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save Journal")
            }
        }
    }
}

@Composable
fun TodayStatusCards(
    foodItems: List<String> = emptyList(),
    symptoms: List<String> = emptyList(),
    bowelMovements: List<String> = emptyList(),
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 3 })

    Column(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(0.dp),
            pageSpacing = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                // FIX: heightIn instead of height(150.dp)
                .heightIn(min = 130.dp)
        ) { page ->
            when (page) {
                0 -> StatusCard(
                    title = "Food",
                    iconRes = R.drawable.ic_food,
                    items = foodItems,
                    emptyMessage = "No food logged today",
                    accentColor = BellyGreen
                )
                1 -> StatusCard(
                    title = "Symptoms",
                    iconRes = R.drawable.ic_symptoms,
                    items = symptoms,
                    emptyMessage = "No symptoms recorded",
                    accentColor = Color(0xFFFFA726)
                )
                2 -> StatusCard(
                    title = "Bowel Movement",
                    iconRes = R.drawable.ic_toilet,
                    items = bowelMovements,
                    emptyMessage = "No bowel movements logged",
                    accentColor = Color(0xFFAB47BC)
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(if (pagerState.currentPage == index) 18.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index) BellyGreen
                            else BellyGreen.copy(alpha = 0.25f)
                        )
                )
                if (index < 2) Spacer(Modifier.width(8.dp))
            }
        }
    }
}

@Composable
private fun StatusCard(
    title: String,
    iconRes: Int? = null,
    items: List<String>,
    emptyMessage: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (iconRes != null) {
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = title,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(Modifier.height(10.dp))

            if (items.isEmpty()) {
                Text(
                    text = emptyMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items.take(3).forEach { item ->
                        Row(verticalAlignment = Alignment.Top) {
                            Text(text = "•", color = accentColor, modifier = Modifier.padding(end = 6.dp))
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    if (items.size > 3) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "+${items.size - 3} more",
                            style = MaterialTheme.typography.bodySmall,
                            color = accentColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

enum class BottomItem { Settings, Grid, Home, Calendar, Bell }

@Composable
fun BottomToolBar(
    selected: BottomItem,
    onSelect: (BottomItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF121212),
        tonalElevation = 8.dp,
        shadowElevation = 12.dp,
        shape = RoundedCornerShape(28.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ToolbarIcon(item = BottomItem.Settings, selected = selected == BottomItem.Settings,
                onClick = { onSelect(BottomItem.Settings) }, painter = painterResource(R.drawable.settings))
            ToolbarIcon(item = BottomItem.Grid, selected = selected == BottomItem.Grid,
                onClick = { onSelect(BottomItem.Grid) }, painter = painterResource(R.drawable.grid))
            ToolbarIcon(item = BottomItem.Home, selected = selected == BottomItem.Home,
                onClick = { onSelect(BottomItem.Home) }, painter = painterResource(R.drawable.home))
            ToolbarIcon(item = BottomItem.Calendar, selected = selected == BottomItem.Calendar,
                onClick = { onSelect(BottomItem.Calendar) }, painter = painterResource(R.drawable.calendar))
            ToolbarIcon(item = BottomItem.Bell, selected = selected == BottomItem.Bell,
                onClick = { onSelect(BottomItem.Bell) }, painter = painterResource(R.drawable.bell))
        }
    }
}

@Composable
private fun ToolbarIcon(
    item: BottomItem,
    selected: Boolean,
    onClick: () -> Unit,
    painter: Painter
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(44.dp)) {
        if (selected) {
            Surface(color = BellyGreen, shape = CircleShape, modifier = Modifier.size(36.dp)) {}
        }
        IconButton(onClick = onClick) {
            Icon(
                painter = painter,
                contentDescription = item.name,
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}
