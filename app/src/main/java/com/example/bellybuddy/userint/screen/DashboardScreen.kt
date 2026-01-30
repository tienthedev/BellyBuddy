package com.example.bellybuddy.userint.screen

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.lerp
import com.example.bellybuddy.R
import java.util.Calendar
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.painter.Painter
import com.example.bellybuddy.ui.theme.BellyGreen
import com.example.bellybuddy.userint.component.DailyScoreCard
import com.example.bellybuddy.userint.component.WeightCard
import com.example.bellybuddy.userint.component.ReminderCard
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.style.TextAlign
import com.example.bellybuddy.data.model.DailyJournal
import com.example.bellybuddy.viewmodel.DailyJournalViewModel
import com.example.bellybuddy.viewmodel.UserViewModel
import com.example.bellybuddy.viewmodel.UserViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onProfileClick: (() -> Unit)? = null,
    onBottomSelect: (BottomItem) -> Unit,
    onLogout: (() -> Unit)? = null, // optional for later use
    onDailyScoreClick: (() -> Unit)? = null,
    onWeightClick: (() -> Unit)? = null,
    onReminderClick: (() -> Unit)? = null,
    score: Int = 88

) {
    // --- START: ViewModel and Database Integration ---

    // Get the application context to create the ViewModel
    val application = LocalContext.current.applicationContext as Application

    // Instantiate the ViewModel using our factory
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(application)
    )

    val journalViewModel: DailyJournalViewModel = viewModel()

    // Observe the loggedInUser StateFlow. The UI will automatically recompose when this changes.
    val loggedInUser by userViewModel.loggedInUser.collectAsState()

    // --- END: ViewModel and Database Integration ---
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val greeting = when (hour) {
        in 0..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }

    val brand = Color(0xFF9DDB9E) // light green accent
    val journalOpen = rememberSaveable { mutableStateOf(false) }

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
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomToolBar(
                selected = BottomItem.Home,
                onSelect = onBottomSelect
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(3.dp))
                Text(
                    text = "$greeting, Robie",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = "Today's Status",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Row(Modifier.fillMaxWidth()) {
                    DailyScoreCard(
                        score = score, // This can be replaced later if you store score in the DB
                        showLabel = true,
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                            .padding(end = 8.dp),
                        onClick = { onDailyScoreClick?.invoke() }
                    )
                    WeightCard(
                        title = "Weight",
                        // Use the user's weight from the database.
                        // The `?:` operator provides a default value if weight is null.
                        value = "${loggedInUser?.weight ?: "--"} lbs",
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                            .padding(start = 8.dp),
                        onClick = { onWeightClick?.invoke() }
                    )
                }
                Spacer(Modifier.height(16.dp))

                ReminderCard(
                    title = "Reminder",
                    message = "Did you take your supplements?",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    onClick = { onReminderClick?.invoke() }
                )
                Spacer(Modifier.height(16.dp))

                TodayStatusCards(
                    foodItems = listOf("Oatmeal", "Grilled Chicken", "Salad"), // Replace with actual data
                    symptoms = listOf("Bloating", "Headache"), // Replace with actual data
                    bowelMovements = listOf("Morning - Normal"), // Replace with actual data
                    modifier = Modifier.fillMaxWidth()
                )
            }

            SideDockButton(
                onClick = { journalOpen.value = true },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = 8.dp)
            )

            DailyJournalSheet(
                open = journalOpen.value,
                onClose = { journalOpen.value = false },
                onSave = { text ->
                    // Save to database
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val today = dateFormat.format(Date())

                    val entry = DailyJournal(
                        userId = 1,  // TODO: Get from actual logged-in user
                        date = today,
                        mood = "",
                        notes = text
                    )
                    journalViewModel.insertJournalEntry(entry)
                    journalOpen.value = false
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            )
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

    Column(
        modifier = modifier
    ) {
        // Swipeable cards
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) { page ->
            when (page) {
                0 -> StatusCard(
                    title = "Food",
                    iconRes = R.drawable.ic_food,
                    items = foodItems,
                    emptyMessage = "No food logged today",
                    backgroundColor = BellyGreen.copy(alpha = 0.15f),
                    borderColor = BellyGreen
                )
                1 -> StatusCard(
                    title = "Symptoms",
                    iconRes = R.drawable.ic_symptoms,
                    items = symptoms,
                    emptyMessage = "No symptoms recorded",
                    backgroundColor = Color(0xFFFFE0B2).copy(alpha = 0.3f),
                    borderColor = Color(0xFFFFA726)
                )
                2 -> StatusCard(
                    title = "Bowel Movement",
                    iconRes = R.drawable.ic_toilet,
                    items = bowelMovements,
                    emptyMessage = "No bowel movements logged",
                    backgroundColor = Color(0xFFE1BEE7).copy(alpha = 0.3f),
                    borderColor = Color(0xFFAB47BC)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Page indicators
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(if (pagerState.currentPage == index) 24.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index)
                                BellyGreen
                            else
                                BellyGreen.copy(alpha = 0.3f)
                        )
                )
                if (index < 2) {
                    Spacer(Modifier.width(8.dp))
                }
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
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    borderColor: Color? = null
) {
    val respectedColor = borderColor ?: lerp(backgroundColor, Color.Black, 0.25f)

    OutlinedCard(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(2.dp, respectedColor),
        colors = CardDefaults.outlinedCardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                )
                if (iconRes != null) {
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = title,
                        modifier = Modifier.size(40.dp),   // smaller so it fits nicely next to text
                        tint = Color.Unspecified
                    )
                }
            }
            Spacer(Modifier.height(12.dp))

            if (items.isEmpty()) {
                Text(
                    text = emptyMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Start
                )
            } else {
                Column(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalArrangement = Arrangement.Top
                ) {
                    items.take(3).forEach { item ->
                        Text(
                            text = "• $item",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                    if (items.size > 3) {
                        Text(
                            text = "+${items.size - 3} more",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SideDockButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = BellyGreen,
        shape = RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp),
        shadowElevation = 6.dp,
        modifier = modifier
            .size(width = 48.dp, height = 88.dp)
            .clickable { onClick() }
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = R.drawable.journal),
                contentDescription = "Daily Journal",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun DailyJournalSheet(
    open: Boolean,
    onClose: () -> Unit,
    onSave: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(open) { onClose() }

    // Scrim behind the sheet
    AnimatedVisibility(
        visible = open,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
                .clickable { onClose() } // tap outside to close
        )
    }

    AnimatedVisibility(
        visible = open,
        enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
        exit  = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp),
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth(1f) // sheet width (85% of screen); tweak as you like
        ) {
            var text by remember { mutableStateOf("") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    "Daily Journal",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    placeholder = { Text("What did you eat or do today?") },
                    minLines = 6
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onClose,
                        modifier = Modifier.weight(1f)
                    ) { Text("Cancel") }

                    Button(
                        onClick = { onSave(text) },
                        modifier = Modifier.weight(1f)
                    ) { Text("Save") }
                }
            }
        }
    }
}

enum class BottomItem { Settings, Grid, Home, Calendar, Bell}
@Composable
fun BottomToolBar(
    selected: BottomItem,
    onSelect: (BottomItem) -> Unit,
    modifier: Modifier = Modifier
) {
    // This Surface floats above the nav bar area and has big rounded corners
    Surface(
        color = Color(0xFF121212),            // black-ish
        tonalElevation = 8.dp,
        shadowElevation = 12.dp,
        shape = RoundedCornerShape(28.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .navigationBarsPadding()         // keep above gesture bar
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ToolbarIcon(
                item = BottomItem.Settings,
                selected = selected == BottomItem.Settings,
                onClick = { onSelect(BottomItem.Settings) },
                painter = painterResource(R.drawable.settings) // your vector
            )
            ToolbarIcon(
                item = BottomItem.Grid,
                selected = selected == BottomItem.Grid,
                onClick = { onSelect(BottomItem.Grid) },
                painter = painterResource(R.drawable.grid) // your vector
            )
            ToolbarIcon(
                item = BottomItem.Home,
                selected = selected == BottomItem.Home,
                onClick = { onSelect(BottomItem.Home) },
                painter = painterResource(R.drawable.home) // your vector
            )
            ToolbarIcon(
                item = BottomItem.Calendar,
                selected = selected == BottomItem.Calendar,
                onClick = { onSelect(BottomItem.Calendar) },
                painter = painterResource(R.drawable.calendar) // your vector
            )
            ToolbarIcon(
                item = BottomItem.Bell,
                selected = selected == BottomItem.Bell,
                onClick = { onSelect(BottomItem.Bell) },
                painter = painterResource(R.drawable.bell) // your vector
            )
        }
    }
}

// --- Single icon with selected highlight + optional red dot for Bell ---
@Composable
private fun ToolbarIcon(
    item: BottomItem,
    selected: Boolean,
    onClick: () -> Unit,
    painter: Painter,
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(44.dp)
            .clickable(onClick = onClick)
    ) {
        // Selected: green circular background
        if (selected) {
            Surface(
                color = BellyGreen,
                shape = CircleShape,
                modifier = Modifier.size(36.dp)
            ) {}
        }

        Icon(
            painter = painter,
            contentDescription = item.name,
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
    }
}
