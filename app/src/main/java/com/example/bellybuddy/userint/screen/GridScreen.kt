package com.example.bellybuddy.userint.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.bellybuddy.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GridScreen(
    onSelectBottom: (BottomItem) -> Unit,
    onBowelMovementClick: () -> Unit,
    onJournalClick: () -> Unit,
    onWeightClickv2: () -> Unit,
    onFoodLogClick: () -> Unit,
    onDailyScoreClickv2: () -> Unit,
    onSymptomClick: () -> Unit,
    onUserListClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("All Features") }) },
        bottomBar = {
            BottomToolBar(
                selected = BottomItem.Grid,
                onSelect = onSelectBottom
            )
        }
    ) { padding ->
        val features = listOf(
            Triple("Daily Journal", R.drawable.journal_button, onJournalClick),
            Triple("Daily Score", R.drawable.daily_score_button, onDailyScoreClickv2),
            Triple("Weight", R.drawable.weight_scale_button, onWeightClickv2),
            Triple("Food Log", R.drawable.food_log_button, onFoodLogClick),
            Triple("Symptom Tracker", R.drawable.symptom_tracker_button, onSymptomClick),
            Triple("Bowel Movements", R.drawable.ic_toilet, onBowelMovementClick),
            Triple("View Database Users", R.drawable.profile_photo, onUserListClick)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(features) { (title, imageRes, click) ->
                val shape = RoundedCornerShape(12.dp)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable { click() },
                    shape = shape,
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE6E6E6))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = title,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = title,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth(0.7f)
                                .aspectRatio(1f),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}