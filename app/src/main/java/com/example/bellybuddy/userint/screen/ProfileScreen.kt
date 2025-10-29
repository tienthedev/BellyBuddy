package com.example.bellybuddy.userint.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bellybuddy.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSelectBottom: (BottomItem) -> Unit,
    onBack: (() -> Unit)? = null,
    onGoToSettings: (() -> Unit)? = null,
    onEditProfile: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = { onBack?.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
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
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(100.dp)
            ) { Image(
                painter = painterResource(id = R.drawable.profile_photo),
                contentDescription = "Profile",
                modifier = Modifier
                    .fillMaxSize()
            )
            }
            Text("Robie")

            Button(
                onClick = { onEditProfile?.invoke() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Edit Profile") }

            Button(
                onClick = { onGoToSettings?.invoke() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Go to Settings") }
        }
    }
}
