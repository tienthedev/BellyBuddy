package com.example.bellybuddy.userint.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bellybuddy.ui.theme.BellyGreen

@Composable
fun InitialProfileScreen(
    onComplete: (String, Int?, Double?, Double?, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var healthConditions by remember { mutableStateOf("") }

    // FIX: Replace the centering Box+Card with a plain scrollable Column.
    // The old Box(contentAlignment = Center) fought with the IME (keyboard),
    // causing the card to get clipped. A top-aligned scrollable Column lets
    // the keyboard push content up naturally without clipping anything.
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to BellyBuddy",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = BellyGreen
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Tell us about yourself",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(Modifier.height(32.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BellyGreen,
                        focusedLabelColor = BellyGreen
                    )
                )

                Spacer(Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text("Age") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BellyGreen,
                            focusedLabelColor = BellyGreen
                        )
                    )
                    Spacer(Modifier.width(16.dp))
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        label = { Text("Weight (lbs)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BellyGreen,
                            focusedLabelColor = BellyGreen
                        )
                    )
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Height (cm)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BellyGreen,
                        focusedLabelColor = BellyGreen
                    )
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = healthConditions,
                    onValueChange = { healthConditions = it },
                    label = { Text("Health Conditions / Allergies") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BellyGreen,
                        focusedLabelColor = BellyGreen
                    )
                )

                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = {
                        onComplete(
                            name,
                            age.toIntOrNull(),
                            weight.toDoubleOrNull(),
                            height.toDoubleOrNull(),
                            healthConditions
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp), // FIX: heightIn instead of height so text never clips
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BellyGreen,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Get Started",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}
