
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
import androidx.compose.material3.Button
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.bellybuddy.ui.theme.BellyGreenDark
import com.example.bellybuddy.viewmodel.UserViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    onSelectBottom: (BottomItem) -> Unit,
    onBack: () -> Unit,
    onGoToSettings: (() -> Unit)? = null,
    onEditProfile: (() -> Unit)? = null
) {
    val loggedInUser by userViewModel.loggedInUser.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profile", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    TextButton(onClick = onBack, colors = ButtonDefaults.textButtonColors(contentColor = BellyGreenDark)) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = { BottomToolBar(selected = BottomItem.Home, onSelect = onSelectBottom) }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(shape = CircleShape, color = Color.Transparent, modifier = Modifier.size(100.dp)) {
                Image(painter = painterResource(id = R.drawable.profile_photo), contentDescription = "Profile", modifier = Modifier.fillMaxSize())
            }
            Text(
                loggedInUser?.name ?: "User",
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = { onEditProfile?.invoke() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Profile")
            }

            Button(
                onClick = { onGoToSettings?.invoke() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Settings")
            }
        }
    }
}