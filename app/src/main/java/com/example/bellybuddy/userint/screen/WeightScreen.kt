// ============================================================
// WeightScreen.kt
// ============================================================
package com.example.bellybuddy.userint.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.example.bellybuddy.ui.theme.BellyGreenDark
import com.example.bellybuddy.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightScreen(
    userViewModel: UserViewModel,
    onBottomSelect: (BottomItem) -> Unit,
    onBack: () -> Unit = {}
) {
    val loggedInUser by userViewModel.loggedInUser.collectAsState()

    var weightInput by remember(loggedInUser?.weight) {
        mutableStateOf(loggedInUser?.weight?.toString().orEmpty())
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Weight",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    TextButton(
                        onClick = onBack,
                        colors = ButtonDefaults.textButtonColors(contentColor = BellyGreenDark)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            BottomToolBar(selected = BottomItem.Grid, onSelect = onBottomSelect)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Update your weight",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = weightInput,
                onValueChange = { weightInput = it },
                label = { Text("Weight (lbs)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Button(
                onClick = {
                    val newWeight = weightInput.toDoubleOrNull()
                    val currentUser = loggedInUser ?: return@Button

                    if (newWeight != null) {
                        userViewModel.updateUser(
                            currentUser.copy(weight = newWeight)
                        )
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = weightInput.toDoubleOrNull() != null && loggedInUser != null
            ) {
                Text("Save Weight")
            }

            Text(
                text = "Current saved weight: ${loggedInUser?.weight ?: "--"} lbs",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}