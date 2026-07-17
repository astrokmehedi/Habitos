package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun OnboardingScreen(
    onComplete: (
        name: String,
        wakeUpTime: String,
        sleepTime: String,
        biggestGoal: String,
        challengeType: String,
        badHabits: List<String>,
        exerciseTarget: Int,
        readingTarget: Int,
        reminderTimes: String
    ) -> Unit
) {
    var step by remember { mutableStateOf(1) }
    
    // Inputs
    var name by remember { mutableStateOf("") }
    var wakeUpTime by remember { mutableStateOf("05:00") }
    var sleepTime by remember { mutableStateOf("22:00") }
    var biggestGoal by remember { mutableStateOf("") }
    var challengeType by remember { mutableStateOf("30_DAY") }
    var badHabitsInput by remember { mutableStateOf("") }
    var exerciseTarget by remember { mutableStateOf(30) }
    var readingTarget by remember { mutableStateOf(10) }
    var reminderTimes by remember { mutableStateOf("08:00,12:00,18:00,21:00") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PitchBlack
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header Title
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DISCIPLINE ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "OS",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = VolcanicOrange,
                    letterSpacing = (-0.5).sp
                )
            }

            Text(
                text = "SYSTEM INITIALIZATION",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MutedSilver,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Step Counter
            Text(
                text = "PHASE $step OF 4",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = VolcanicOrange,
                letterSpacing = 1.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LinearProgressIndicator(
                progress = { step / 4f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .padding(horizontal = 32.dp),
                color = VolcanicOrange,
                trackColor = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(48.dp))

            when (step) {
                1 -> {
                    Text(
                        text = "Identify Yourself",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "No excuses begin here. Let us set your identity.",
                        fontSize = 14.sp,
                        color = MutedSilver,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Warrior Name") },
                        placeholder = { Text("Enter your name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("onboarding_name_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VolcanicOrange,
                            focusedLabelColor = VolcanicOrange,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                            unfocusedLabelColor = MutedSilver,
                            cursorColor = VolcanicOrange
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = biggestGoal,
                        onValueChange = { biggestGoal = it },
                        label = { Text("Your Ultimate Discipline Goal") },
                        placeholder = { Text("e.g. Master mental toughness & coding skills") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VolcanicOrange,
                            focusedLabelColor = VolcanicOrange,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                            unfocusedLabelColor = MutedSilver,
                            cursorColor = VolcanicOrange
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                2 -> {
                    Text(
                        text = "The Biological Clock",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Sleep is a tactical recharge, not an escape. Strict wake times build character.",
                        fontSize = 14.sp,
                        color = MutedSilver,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    OutlinedTextField(
                        value = wakeUpTime,
                        onValueChange = { wakeUpTime = it },
                        label = { Text("Target Wake Up Time (24h)") },
                        placeholder = { Text("05:00") },
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

                    OutlinedTextField(
                        value = sleepTime,
                        onValueChange = { sleepTime = it },
                        label = { Text("Target Sleep Time (24h)") },
                        placeholder = { Text("22:00") },
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
                }
                3 -> {
                    Text(
                        text = "Choose Your Contract",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "How long are you willing to fight for your future?",
                        fontSize = 14.sp,
                        color = MutedSilver,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            onClick = { challengeType = "30_DAY" },
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = if (challengeType == "30_DAY") VolcanicOrangeMuted else DarkCharcoal
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (challengeType == "30_DAY") VolcanicOrange.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.05f)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("30 DAY", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp, fontFamily = FontFamily.Monospace)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Consistency Foundation", fontSize = 11.sp, color = MutedSilver)
                            }
                        }

                        Card(
                            onClick = { challengeType = "90_DAY" },
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = if (challengeType == "90_DAY") VolcanicOrangeMuted else DarkCharcoal
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (challengeType == "90_DAY") VolcanicOrange.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.05f)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("90 DAY", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp, fontFamily = FontFamily.Monospace)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Complete Rebirth", fontSize = 11.sp, color = MutedSilver)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = badHabitsInput,
                        onValueChange = { badHabitsInput = it },
                        label = { Text("Identify Bad Habits to Destroy") },
                        placeholder = { Text("e.g. social media, junk food, snooze button") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VolcanicOrange,
                            focusedLabelColor = VolcanicOrange,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                            unfocusedLabelColor = MutedSilver
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                4 -> {
                    Text(
                        text = "Commitment Parameters",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Define daily targets. They will be logged, calculated, and enforced.",
                        fontSize = 14.sp,
                        color = MutedSilver,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = exerciseTarget.toString(),
                            onValueChange = { exerciseTarget = it.toIntOrNull() ?: 30 },
                            label = { Text("Exercise (min)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VolcanicOrange,
                                focusedLabelColor = VolcanicOrange,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                                unfocusedLabelColor = MutedSilver
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = readingTarget.toString(),
                            onValueChange = { readingTarget = it.toIntOrNull() ?: 10 },
                            label = { Text("Reading (pages)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VolcanicOrange,
                                focusedLabelColor = VolcanicOrange,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                                unfocusedLabelColor = MutedSilver
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = reminderTimes,
                        onValueChange = { reminderTimes = it },
                        label = { Text("Active Reminder Schedules") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VolcanicOrange,
                            focusedLabelColor = VolcanicOrange,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                            unfocusedLabelColor = MutedSilver
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Navigation Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (step > 1) {
                    TextButton(
                        onClick = { step -= 1 },
                        colors = ButtonDefaults.textButtonColors(contentColor = MutedSilver)
                    ) {
                        Text("BACK", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Spacer(modifier = Modifier.width(60.dp))
                }

                Button(
                    onClick = {
                        if (step < 4) {
                            step += 1
                        } else {
                            val habitsList = badHabitsInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                            onComplete(
                                name.ifBlank { "Warrior" },
                                wakeUpTime,
                                sleepTime,
                                biggestGoal.ifBlank { "Achieve absolute personal consistency" },
                                challengeType,
                                habitsList,
                                exerciseTarget,
                                readingTarget,
                                reminderTimes
                            )
                        }
                    },
                    modifier = Modifier
                        .testTag("onboarding_next_button")
                        .width(140.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VolcanicOrange,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (step == 4) "ACTIVATE" else "NEXT",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
