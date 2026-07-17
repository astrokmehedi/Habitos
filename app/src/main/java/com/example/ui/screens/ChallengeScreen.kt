package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Habit
import com.example.data.UserConfig
import com.example.ui.theme.*

@Composable
fun ChallengeScreen(
    config: UserConfig,
    habits: List<Habit>,
    completions: Set<Int>,
    onToggleHabit: (Int) -> Unit
) {
    val challengeHabits = habits.filter { it.isChallengeDefault }
    val completedChallengeHabitsCount = challengeHabits.count { completions.contains(it.id) }
    val completionPercentage = if (challengeHabits.isNotEmpty()) {
        (completedChallengeHabitsCount.toFloat() / challengeHabits.size.toFloat() * 100).toInt()
    } else {
        0
    }

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
                text = "ACTIVE CONTRACT",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MutedSilver,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.Monospace
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (config.challengeType == "30_DAY") "30 DAY " else "90 DAY ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = if (config.challengeType == "30_DAY") "CHALLENGE" else "TRANSFORMATION",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VolcanicOrange,
                    letterSpacing = (-0.5).sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Contract Progress Card
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCharcoal),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "CURRENT PROGRESS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MutedSilver,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "DAY ${config.currentStreak} ACTIVE",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }

                        Text(
                            text = "$completionPercentage%",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black,
                            color = VolcanicOrange
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { completionPercentage / 100f },
                        modifier = Modifier.fillMaxWidth().height(6.dp),
                        color = VolcanicOrange,
                        trackColor = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Completed $completedChallengeHabitsCount of ${challengeHabits.size} mandatory daily system pillars.",
                        fontSize = 12.sp,
                        color = MutedSilver
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "SYSTEM CHALLENGE PILLARS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = VolcanicOrange,
                letterSpacing = 1.sp,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Pillars checklist
            if (challengeHabits.isEmpty()) {
                Text("Prepopulating challenge details...", color = MutedSilver)
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    challengeHabits.forEach { habit ->
                        val isDone = completions.contains(habit.id)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggleHabit(habit.id) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isDone) VolcanicOrangeMuted else DarkCharcoal
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (isDone) VolcanicOrange.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.05f)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = habit.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Category: ${habit.category} | Difficulty: ${habit.difficulty}",
                                        fontSize = 11.sp,
                                        color = MutedSilver
                                    )
                                }

                                Checkbox(
                                    checked = isDone,
                                    onCheckedChange = { onToggleHabit(habit.id) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = VolcanicOrange,
                                        checkmarkColor = Color.White,
                                        uncheckedColor = Color.DarkGray
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Extreme Accountability Card
            if (config.challengeType == "90_DAY") {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCharcoal),
                    border = BorderStroke(1.dp, VolcanicOrange.copy(alpha = 0.35f)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            Icons.Default.Security,
                            contentDescription = "Strict Mode",
                            tint = VolcanicOrange,
                            modifier = Modifier.size(32.dp)
                        )

                        Column {
                            Text(
                                text = "EXTREME TRANSFORMATION ACTIVE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = VolcanicOrange,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Missing multiple daily tasks carries severe penalties: Discipline score drops and your virtual tree health decays rapidly.",
                                fontSize = 12.sp,
                                color = Color.White,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
