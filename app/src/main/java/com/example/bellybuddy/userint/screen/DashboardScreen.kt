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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bellybuddy.viewmodel.UserViewModel
import com.example.bellybuddy.viewmodel.UserViewModelFactory
import com.example.bellybuddy.viewmodel.DailyJournalViewModel
import com.example.bellybuddy.data.model.DailyJournal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onProfileClick: (() -> Unit)? = null,
    onBottomSelect: (BottomItem) -> Unit,
    onLogout: (() -> Unit)? = null
) {
    // Get the application context to create the ViewModels
    val application = LocalContext.current.applicationContext as Application

    // Instantiate the UserViewModel using our factory
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(application)
    )

    // Instantiate the DailyJournalEntryViewModel
    val journalViewModel: DailyJournalViewModel = viewModel()

    // Observe the loggedInUser StateFlow
    val loggedInUser by userViewModel.loggedInUser.collectAsState()

    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val greeting = when (hour) {
        in 0..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }

    val brand = Color(0xFF9DDB9E)
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
                    text = "$greeting, ${loggedInUser?.name ?: "User"}",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Row(Modifier.fillMaxWidth()) {
                    DailyScoreCard(
                        score = 88,
                        ringColor = brand,
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                            .padding(end = 8.dp)
                    )
                    WeightCard(
                        title = "Weight",
                        value = "${loggedInUser?.weight ?: "--"} lbs",
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                            .padding(start = 8.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                ReminderCard(
                    title = "Reminder",
                    message = "Did you take your supplements?",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }

            // Floating side button
            SideDockButton(
                onClick = { journalOpen.value = true },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = 8.dp)
            )

            // Sliding sheet
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
private fun DailyScoreCard(
    score: Int,
    ringColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 2.dp,
        modifier = modifier
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$score",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text("Daily Score", style = MaterialTheme.typography.titleMedium, color = ringColor)
        }
    }
}

@Composable
private fun WeightCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 2.dp,
        modifier = modifier
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(title, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun ReminderCard(
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 2.dp,
        modifier = modifier
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                title,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                message,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun SideDockButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF9DDB9E),
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
                .clickable { onClose() }
        )
    }

    AnimatedVisibility(
        visible = open,
        enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
        exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp),
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth(1f)
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
                    OutlinedButton(
                        onClick = {
                            text = ""
                            onClose()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            if (text.isNotBlank()) {
                                onSave(text)
                                text = ""
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = text.isNotBlank()
                    ) {
                        Text("Save")
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
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ToolbarIcon(
                item = BottomItem.Settings,
                selected = selected == BottomItem.Settings,
                onClick = { onSelect(BottomItem.Settings) },
                painter = painterResource(R.drawable.settings)
            )
            ToolbarIcon(
                item = BottomItem.Grid,
                selected = selected == BottomItem.Grid,
                onClick = { onSelect(BottomItem.Grid) },
                painter = painterResource(R.drawable.grid)
            )
            ToolbarIcon(
                item = BottomItem.Home,
                selected = selected == BottomItem.Home,
                onClick = { onSelect(BottomItem.Home) },
                painter = painterResource(R.drawable.home)
            )
            ToolbarIcon(
                item = BottomItem.Calendar,
                selected = selected == BottomItem.Calendar,
                onClick = { onSelect(BottomItem.Calendar) },
                painter = painterResource(R.drawable.calendar)
            )
            ToolbarIcon(
                item = BottomItem.Bell,
                selected = selected == BottomItem.Bell,
                onClick = { onSelect(BottomItem.Bell) },
                painter = painterResource(R.drawable.bell)
            )
        }
    }
}

@Composable
private fun ToolbarIcon(
    item: BottomItem,
    selected: Boolean,
    onClick: () -> Unit,
    painter: Painter,
) {
    val highlight = Color(0xFF9DDB9E)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(44.dp)
            .clickable(onClick = onClick)
    ) {
        if (selected) {
            Surface(
                color = highlight,
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