package com.example.bellybuddy.userint.screen

import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(
    onSelectBottom: (BottomItem) -> Unit,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current

    var notificationsEnabled by remember { mutableStateOf(true) }

    var supplementsOn by remember { mutableStateOf(true) }
    var supplementsTime by remember { mutableStateOf("9:00 AM") }

    var waterOn by remember { mutableStateOf(true) }
    var waterTime by remember { mutableStateOf("12:00 PM") }

    var journalOn by remember { mutableStateOf(true) }
    var journalTime by remember { mutableStateOf("8:30 PM") }

    var weighInOn by remember { mutableStateOf(false) }
    var weighInTime by remember { mutableStateOf("7:30 AM") }

    fun showTimePicker(
        initialLabel: String,
        onTimeSelected: (String) -> Unit
    ) {
        val cal = Calendar.getInstance()
        val dialog = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val amPm = if (hourOfDay >= 12) "PM" else "AM"
                val hour12 = when (val h = hourOfDay % 12) {
                    0 -> 12
                    else -> h
                }
                val min = minute.toString().padStart(2, '0')
                onTimeSelected("$hour12:$min $amPm")
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            false
        )
        dialog.setTitle(initialLabel)
        dialog.show()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Reminders",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        bottomBar = {
            BottomToolBar(
                selected = BottomItem.Bell,
                onSelect = onSelectBottom
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Notifications", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(2.dp))
                        Text(
                            if (notificationsEnabled) "Enabled" else "Disabled",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                }
            }

            Text(
                "Quick Reminders",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            ReminderRow(
                title = "Supplements",
                subtitle = "Daily",
                enabled = supplementsOn,
                timeLabel = supplementsTime,
                notificationsEnabled = notificationsEnabled,
                onToggle = { supplementsOn = it },
                onPickTime = { showTimePicker("Supplements time") { supplementsTime = it } }
            )

            ReminderRow(
                title = "Water check-in",
                subtitle = "Daily",
                enabled = waterOn,
                timeLabel = waterTime,
                notificationsEnabled = notificationsEnabled,
                onToggle = { waterOn = it },
                onPickTime = { showTimePicker("Water reminder time") { waterTime = it } }
            )

            ReminderRow(
                title = "Daily journal",
                subtitle = "Daily",
                enabled = journalOn,
                timeLabel = journalTime,
                notificationsEnabled = notificationsEnabled,
                onToggle = { journalOn = it },
                onPickTime = { showTimePicker("Journal reminder time") { journalTime = it } }
            )

            ReminderRow(
                title = "Weigh-in",
                subtitle = "Mon / Wed / Fri",
                enabled = weighInOn,
                timeLabel = weighInTime,
                notificationsEnabled = notificationsEnabled,
                onToggle = { weighInOn = it },
                onPickTime = { showTimePicker("Weigh-in time") { weighInTime = it } }
            )

            Spacer(Modifier.height(4.dp))

            // Preview section
            Text(
                "Today’s Reminders",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            val todays = buildList {
                if (notificationsEnabled && supplementsOn) add("Supplements • $supplementsTime")
                if (notificationsEnabled && waterOn) add("Water check-in • $waterTime")
                if (notificationsEnabled && journalOn) add("Daily journal • $journalTime")
                if (notificationsEnabled && weighInOn) add("Weigh-in • $weighInTime")
            }

            if (todays.isEmpty()) {
                Text(
                    "No reminders scheduled",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        todays.forEach { line ->
                            Text("• $line")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReminderRow(
    title: String,
    subtitle: String,
    enabled: Boolean,
    timeLabel: String,
    notificationsEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    onPickTime: () -> Unit
) {
    val isActuallyEnabled = notificationsEnabled && enabled

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(2.dp))
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onPickTime,
                    enabled = notificationsEnabled && enabled,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Pick time"
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(timeLabel)
                }
            }

            Switch(
                checked = enabled,
                onCheckedChange = onToggle,
                enabled = notificationsEnabled
            )
        }
    }
}
