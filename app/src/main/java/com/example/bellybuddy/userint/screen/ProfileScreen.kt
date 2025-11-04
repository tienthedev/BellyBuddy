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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.bellybuddy.ui.theme.BellyGreenDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSelectBottom: (BottomItem) -> Unit,
    onBack: () -> Unit,
    onGoToSettings: (() -> Unit)? = null,
    onEditProfile: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Profile",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    TextButton(
                        onClick = onBack,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = BellyGreenDark
                        )
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
                color = Color.Transparent,
                modifier = Modifier.size(100.dp)
            ) { Image(
                painter = painterResource(id = R.drawable.profile_photo),
                contentDescription = "Profile",
                modifier = Modifier
                    .fillMaxSize()
            )
            }
            Text("Robie",
                    fontWeight = FontWeight.Bold)

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
