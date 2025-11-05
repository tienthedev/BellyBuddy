package com.example.bellybuddy.userint.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DailyScoreCard(
    score: Int,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val scoreColor = when {
        score < 60 -> Color(0xFFD9534F)
        score < 80 -> Color(0xFFF0AD4E)
        else -> Color(0xFF5CB85C)
    }

    Card(
        modifier = modifier
            .clickable { onClick?.invoke() },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .offset(y = (-4).dp)
                    .background(Color.LightGray.copy(alpha = 0.3f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = score / 100f,
                    color = scoreColor,
                    strokeWidth = 8.dp,
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = "$score",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = scoreColor
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Daily Score",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}
