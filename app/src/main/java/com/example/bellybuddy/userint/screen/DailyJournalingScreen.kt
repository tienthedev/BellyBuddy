package com.example.bellybuddy.userint.screen

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bellybuddy.data.model.DailyJournal
import com.example.bellybuddy.ui.theme.*
import com.example.bellybuddy.viewmodel.DailyJournalViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyJournalingScreen(
    onSelectBottom: (BottomItem) -> Unit,
    onBack: () -> Unit,
    viewModel: DailyJournalViewModel = viewModel()
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val displayDateFormat = remember { SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()) }

    var selectedDate by remember { mutableStateOf(calendar.time) }
    var selectedDateString by remember { mutableStateOf(dateFormat.format(selectedDate)) }

    var editingEntry by remember { mutableStateOf<DailyJournal?>(null) }
    var editNotes by remember { mutableStateOf("") }

    var deleteEntry by remember { mutableStateOf<DailyJournal?>(null) }

    val journalEntry by viewModel.getJournalEntryByDate(selectedDateString)
        .collectAsState(initial = null)

    val allJournalEntries by viewModel.getAllJournalEntries()
        .collectAsState(initial = emptyList())

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            calendar.set(year, month, day)
            selectedDate = calendar.time
            selectedDateString = dateFormat.format(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    if (editingEntry != null) {
        AlertDialog(
            onDismissRequest = { editingEntry = null },
            title = {
                Text(
                    text = "Edit Journal Entry",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                OutlinedTextField(
                    value = editNotes,
                    onValueChange = { editNotes = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 5,
                    label = { Text("Notes") },
                    shape = RoundedCornerShape(12.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val original = editingEntry!!
                        viewModel.updateJournalEntry(
                            original.copy(
                                notes = editNotes.trim(),
                                timeUpdated = System.currentTimeMillis()
                            )
                        )
                        editingEntry = null
                    },
                    enabled = editNotes.trim().isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BellyGreenDark,
                        contentColor = Color.White
                    )
                ) { Text("Save Changes") }
            },
            dismissButton = {
                TextButton(onClick = { editingEntry = null }) { Text("Cancel") }
            }
        )
    }

    if (deleteEntry != null) {
        AlertDialog(
            onDismissRequest = { deleteEntry = null },
            title = {
                Text(
                    text = "Delete Entry?",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete this journal entry? This can’t be undone.",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val entryToDelete = deleteEntry!!
                        viewModel.deleteJournalEntry(entryToDelete)
                        deleteEntry = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { deleteEntry = null }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Daily Journal",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    TextButton(
                        onClick = onBack,
                        colors = ButtonDefaults.textButtonColors(contentColor = BellyGreenDark)
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
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedButton(
                onClick = { datePickerDialog.show() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, NeutralGray)
            ) {
                Text(
                    displayDateFormat.format(selectedDate),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            HorizontalDivider(thickness = 1.dp, color = LightGray)
            Spacer(modifier = Modifier.height(16.dp))

            if (journalEntry != null) {
                Text(
                    "Today's Entry",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                JournalEntryCard(
                    entry = journalEntry!!,
                    onDelete = { deleteEntry = journalEntry!! },
                    onEdit = { entry ->
                        editingEntry = entry
                        editNotes = entry.notes
                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No journal entry for this date",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            HorizontalDivider(thickness = 1.dp, color = LightGray)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "All Journal Entries",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (allJournalEntries.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No journal entries yet",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(allJournalEntries) { entry ->
                        JournalEntryCard(
                            entry = entry,
                            onDelete = { deleteEntry = entry },
                            onEdit = { e ->
                                editingEntry = e
                                editNotes = e.notes
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JournalEntryCard(
    entry: DailyJournal,
    onDelete: () -> Unit,
    onEdit: (DailyJournal) -> Unit,
    modifier: Modifier = Modifier
) {
    val displayDateFormat = remember { SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()) }
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val entryDate = remember(entry.date) { dateFormat.parse(entry.date) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, LightGray)
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
                        text = displayDateFormat.format(entryDate ?: Date()),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = BellyGreenDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = entry.notes,
                        fontSize = 14.sp,
                        color = Color.Black,
                        lineHeight = 20.sp
                    )
                }

                Row(verticalAlignment = Alignment.Top) {
                    IconButton(onClick = { onEdit(entry) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit entry",
                            tint = BellyGreenDark
                        )
                    }

                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete entry",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}