package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.BorderStroke
import kotlinx.coroutines.delay
import com.example.data.JournalEntry
import com.example.ui.theme.*

@Composable
fun JournalScreen(
    entry: JournalEntry?,
    history: List<JournalEntry>,
    onSaveMorning: (String, String, Int) -> Unit,
    onSaveNight: (String, String, String, String, Int) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Morning, 1 = Night, 2 = Logs

    // Morning Input
    var gratitude by remember(entry) { mutableStateOf(entry?.morningGratitude ?: "") }
    var morningPlan by remember(entry) { mutableStateOf(entry?.morningPlan ?: "") }

    // Night Input
    var reflection by remember(entry) { mutableStateOf(entry?.nightReflection ?: "") }
    var biggestWin by remember(entry) { mutableStateOf(entry?.biggestWin ?: "") }
    var biggestMistake by remember(entry) { mutableStateOf(entry?.biggestMistake ?: "") }
    var tomorrowPlan by remember(entry) { mutableStateOf(entry?.tomorrowPlan ?: "") }
    var moodSelected by remember(entry) { mutableStateOf(entry?.mood ?: 3) }

    var saveFeedback by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PitchBlack
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "COGNITIVE STABILIZATION",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MutedSilver,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.Monospace
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "JOURNAL ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "LOG",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VolcanicOrange,
                    letterSpacing = (-0.5).sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Navigation Tab Bar
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = PitchBlack,
                contentColor = VolcanicOrange,
                divider = { Divider(color = Color.White.copy(alpha = 0.05f)) }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("MORNING", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("NIGHT", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("LOGS", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (selectedTab) {
                0 -> {
                    // Morning Routine Reflection
                    Text(
                        text = "MORNING INTENTIONAL REBOOT",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Write your intentions as soon as you wake up. Set your path before the chaos begins.",
                        fontSize = 11.sp,
                        color = MutedSilver,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = gratitude,
                        onValueChange = { gratitude = it },
                        label = { Text("What are you strictly grateful for today?") },
                        placeholder = { Text("Write 3 concrete facts (e.g. fresh water, coding ability, clean air)...") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VolcanicOrange,
                            focusedLabelColor = VolcanicOrange,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                            unfocusedLabelColor = MutedSilver
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = morningPlan,
                        onValueChange = { morningPlan = it },
                        label = { Text("How will you dominate today? (Strategic Intent)") },
                        placeholder = { Text("Detail the single highest impact action for today...") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VolcanicOrange,
                            focusedLabelColor = VolcanicOrange,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                            unfocusedLabelColor = MutedSilver
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            onSaveMorning(gratitude, morningPlan, moodSelected)
                            saveFeedback = "Morning mission log updated."
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VolcanicOrange),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("COMMIT MORNING LOG", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }

                1 -> {
                    // Night Review Reflection
                    Text(
                        text = "NIGHT BATTLE ASSESSMENT",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Deconstruct your day. Document your execution gaps, record wins, and formulate tomorrow's plans.",
                        fontSize = 11.sp,
                        color = MutedSilver,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = biggestWin,
                        onValueChange = { biggestWin = it },
                        label = { Text("Your Biggest Win of the Day") },
                        placeholder = { Text("What did you execute perfectly today?") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VolcanicOrange,
                            focusedLabelColor = VolcanicOrange,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                            unfocusedLabelColor = MutedSilver
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = biggestMistake,
                        onValueChange = { biggestMistake = it },
                        label = { Text("Your Biggest Execution Failure (Mistake)") },
                        placeholder = { Text("Identify where your discipline cracked today...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VolcanicOrange,
                            focusedLabelColor = VolcanicOrange,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                            unfocusedLabelColor = MutedSilver
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = reflection,
                        onValueChange = { reflection = it },
                        label = { Text("General Self-Discipline Reflection") },
                        placeholder = { Text("Analyze your mental states and obstacles today...") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VolcanicOrange,
                            focusedLabelColor = VolcanicOrange,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                            unfocusedLabelColor = MutedSilver
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = tomorrowPlan,
                        onValueChange = { tomorrowPlan = it },
                        label = { Text("Tomorrow's Tactical Battlefield Plan") },
                        placeholder = { Text("Detail the core task you will finish tomorrow morning...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VolcanicOrange,
                            focusedLabelColor = VolcanicOrange,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                            unfocusedLabelColor = MutedSilver
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Tactical Mood State Index (1-5)", fontSize = 11.sp, color = MutedSilver, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(1, 2, 3, 4, 5).forEach { score ->
                            val isSelected = moodSelected == score
                            Card(
                                onClick = { moodSelected = score },
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) VolcanicOrange else MatteGray
                                ),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                            ) {
                                Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
                                    Text("$score", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            onSaveNight(reflection, biggestWin, biggestMistake, tomorrowPlan, moodSelected)
                            saveFeedback = "Night battle log finalized."
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VolcanicOrange),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("COMMIT NIGHT BATTLE LOG", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                    }
                }

                2 -> {
                    // Logs / History
                    Text(
                        text = "HISTORICAL JOURNAL LOGS",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (history.isEmpty()) {
                        Text("No records in history. Commit your first entry.", color = MutedSilver, fontSize = 13.sp)
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            history.forEach { log ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = DarkCharcoal),
                                    shape = RoundedCornerShape(16.dp),
                                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = log.date,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = VolcanicOrange,
                                                fontFamily = FontFamily.Monospace
                                            )

                                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Icon(Icons.Default.Mood, contentDescription = "Mood rating", tint = MutedSilver, modifier = Modifier.size(14.dp))
                                                Text("INDEX ${log.mood}", fontSize = 11.sp, color = MutedSilver, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                            }
                                        }

                                        if (log.morningGratitude.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text("Morning Gratitude: ${log.morningGratitude}", fontSize = 12.sp, color = Color.White)
                                        }

                                        if (log.biggestWin.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text("Biggest Win: ${log.biggestWin}", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                        }

                                        if (log.biggestMistake.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text("Mistake: ${log.biggestMistake}", fontSize = 12.sp, color = MutedSilver)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (saveFeedback.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = VolcanicOrangeMuted),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, VolcanicOrange.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = saveFeedback,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Auto-clear feedback message
                LaunchedEffect(saveFeedback) {
                    delay(3000)
                    saveFeedback = ""
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
