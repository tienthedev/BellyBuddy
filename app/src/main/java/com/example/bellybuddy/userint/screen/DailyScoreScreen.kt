package com.example.bellybuddy.userint.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.bellybuddy.ui.theme.BellyGreenDark
import com.example.bellybuddy.userint.component.DailyScoreCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyScoreScreen(
    onBottomSelect: (BottomItem) -> Unit,
    onBack: () -> Unit = {},
    score: Int = 88
) {
    val (label, labelColor) = scoreLabel(score)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Daily Score",
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
                selected = BottomItem.Grid,
                onSelect = onBottomSelect
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                DailyScoreCard(
                    score = score,
                    showLabel = false,
                    modifier = Modifier
                        .width(220.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

private fun scoreLabel(score: Int): Pair<String, Color> {
    return when {
        score >= 80 -> "Excellent" to Color(0xFF5CB85C)
        score >= 60 -> "Okay" to Color(0xFFF0AD4E)
        else -> "Bad" to Color(0xFFD9534F)
    }
}