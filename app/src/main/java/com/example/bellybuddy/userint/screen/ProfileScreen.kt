package com.example.bellybuddy.userint.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSelectBottom: (BottomItem) -> Unit,
    onBack: (() -> Unit)? = null,
    onGoToSettings: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    TextButton(onClick = { onBack?.invoke() }) {
                        Text("Back") // later you can replace with an icon
                    }
                }
            )
        },
        bottomBar = {
            BottomToolBar(
                selected = BottomItem.Home,
                onSelect = onSelectBottom
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                "Profile screen content here",
                style = MaterialTheme.typography.headlineSmall
            )

            Button(
                onClick = { onGoToSettings?.invoke() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Settings")
            }
        }
    }
}
