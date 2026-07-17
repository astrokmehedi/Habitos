package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.drawBehind
import com.example.data.Habit
import com.example.data.UserConfig
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    config: UserConfig,
    habits: List<Habit>,
    completions: Set<Int>,
    motivationalQuote: String,
    onToggleHabit: (Int) -> Unit,
    onNavigateToFocus: () -> Unit,
    onNavigateToJournal: () -> Unit,
    onNavigateToStats: () -> Unit,
    onTriggerAlarmDemo: (String) -> Unit,
    onRunReview: () -> Unit,
    isCoachLoading: Boolean,
    coachFeedback: String,
    onPurchaseHealer: () -> Unit
) {
    var showCoachSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PitchBlack
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Command Center Title (Artistic Flair Top App Bar)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SYSTEM STATUS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MutedSilver,
                        letterSpacing = 2.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "DISCIPLINE ",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "OS",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = VolcanicOrange,
                            letterSpacing = (-0.5).sp
                        )
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = MatteGray),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                    modifier = Modifier.clickable { onNavigateToStats() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(EmeraldGreen, shape = RoundedCornerShape(4.dp))
                                .drawBehind {
                                    drawCircle(
                                        color = EmeraldGreen.copy(alpha = 0.4f),
                                        radius = size.width * 1.5f
                                    )
                                }
                        )
                        Text(
                            text = "TREE: ${config.treeHealth}%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quote Board
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        val centerOffset = androidx.compose.ui.geometry.Offset(size.width, 0f)
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(VolcanicOrange.copy(alpha = 0.12f), Color.Transparent),
                                center = centerOffset,
                                radius = size.width * 0.7f
                            ),
                            radius = size.width * 0.7f,
                            center = centerOffset
                        )
                    },
                colors = CardDefaults.cardColors(containerColor = DarkCharcoal),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Terminal,
                            contentDescription = "Coach Prompt",
                            tint = VolcanicOrange,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "DIRECTIVE // COACH",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MutedSilver,
                            letterSpacing = 1.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "\"$motivationalQuote\"",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main Stats Row (Score, Streaks)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Score Widget
                Card(
                    modifier = Modifier
                        .weight(1.2f)
                        .height(156.dp)
                        .drawBehind {
                            val centerOffset = androidx.compose.ui.geometry.Offset(size.width * 0.5f, size.height * 0.5f)
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(VolcanicOrange.copy(alpha = 0.1f), Color.Transparent),
                                    center = centerOffset,
                                    radius = size.width * 0.6f
                                ),
                                radius = size.width * 0.6f,
                                center = centerOffset
                            )
                        },
                    colors = CardDefaults.cardColors(containerColor = DarkCharcoal),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(94.dp)) {
                            // Track
                            drawCircle(
                                color = LightGray,
                                style = Stroke(width = 7.dp.toPx())
                            )
                            // Progress
                            drawArc(
                                color = VolcanicOrange,
                                startAngle = -90f,
                                sweepAngle = (config.disciplineScore / 100f) * 360f,
                                useCenter = false,
                                style = Stroke(width = 7.dp.toPx())
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${config.disciplineScore}",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Light,
                                color = Color.White,
                                letterSpacing = (-1).sp
                            )
                            Text(
                                text = "SCORE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = VolcanicOrange,
                                letterSpacing = 1.5.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                // Streaks & Gamification Widget
                Card(
                    modifier = Modifier
                        .weight(1.5f)
                        .height(156.dp)
                        .drawBehind {
                            val centerOffset = androidx.compose.ui.geometry.Offset(size.width, size.height)
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(VolcanicOrange.copy(alpha = 0.08f), Color.Transparent),
                                    center = centerOffset,
                                    radius = size.width * 0.7f
                                ),
                                radius = size.width * 0.7f,
                                center = centerOffset
                            )
                        },
                    colors = CardDefaults.cardColors(containerColor = DarkCharcoal),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "LEVEL ${config.level}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "${config.xp}/${config.level * 150} XP",
                                    fontSize = 11.sp,
                                    color = MutedSilver,
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.MonetizationOn,
                                        contentDescription = "Coins",
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${config.coins}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                Text("COINS", fontSize = 9.sp, color = MutedSilver, fontFamily = FontFamily.Monospace)
                            }
                        }

                        // Streaks logs
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.LocalFireDepartment,
                                        contentDescription = "Streak",
                                        tint = VolcanicOrange,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "${config.currentStreak}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Text("STREAK", fontSize = 8.sp, color = MutedSilver, letterSpacing = 0.5.sp)
                            }

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.EmojiEvents,
                                        contentDescription = "Longest Streak",
                                        tint = Color(0xFFC0C0C0),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "${config.longestStreak}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Text("LONGEST", fontSize = 8.sp, color = MutedSilver, letterSpacing = 0.5.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Quick Links (Focus, Journal, AI Coach)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onNavigateToFocus() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MatteGray),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                ) {
                    Icon(Icons.Default.Timer, contentDescription = "Focus", tint = VolcanicOrange)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("FOCUS MODE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                }

                Button(
                    onClick = { onNavigateToJournal() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MatteGray),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                ) {
                    Icon(Icons.Default.Book, contentDescription = "Journal", tint = VolcanicOrange)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("QUICK JOURNAL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // AI Coach feedback button
            Button(
                onClick = {
                    onRunReview()
                    showCoachSheet = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = VolcanicOrange),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
            ) {
                if (isCoachLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
                } else {
                    Icon(Icons.Default.SmartButton, contentDescription = "AI Review")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("RUN EVALUATION WITH AI COACH", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Monospace)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Alarm & Verification trigger logs
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkCharcoal),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "WAKE ALARM SYSTEM",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = VolcanicOrange,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Configured wake target: ${config.wakeUpTime}. Standard exit verification is enabled.",
                        fontSize = 12.sp,
                        color = MutedSilver
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "TEST WAKE-UP CHANNELS (SIMULATION):",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MutedSilver,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onTriggerAlarmDemo("bathroom_photo") },
                            colors = ButtonDefaults.buttonColors(containerColor = MatteGray),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("BATHROOM PHOTO", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }

                        Button(
                            onClick = { onTriggerAlarmDemo("math") },
                            colors = ButtonDefaults.buttonColors(containerColor = MatteGray),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("MATH DRILL", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onTriggerAlarmDemo("memory") },
                            colors = ButtonDefaults.buttonColors(containerColor = MatteGray),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("MEMORY MATCH", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }

                        Button(
                            onClick = { onTriggerAlarmDemo("typing") },
                            colors = ButtonDefaults.buttonColors(containerColor = MatteGray),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("TYPING CODE", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Today's Missions Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TODAY'S MISSION LOG",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VolcanicOrange,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "${completions.size} / ${habits.size} DONE",
                    fontSize = 11.sp,
                    color = MutedSilver,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Habits/Missions list
            if (habits.isEmpty()) {
                Text(
                    text = "No active missions. Setup your custom habits in list tab.",
                    color = MutedSilver,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    habits.forEach { habit ->
                        val isCompleted = completions.contains(habit.id)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggleHabit(habit.id) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCompleted) VolcanicOrangeMuted else DarkCharcoal
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (isCompleted) VolcanicOrange.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.05f)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = when (habit.iconName) {
                                            "alarm" -> Icons.Default.Alarm
                                            "fitness_center" -> Icons.Default.FitnessCenter
                                            "book" -> Icons.Default.Book
                                            "edit" -> Icons.Default.Edit
                                            "self_improvement" -> Icons.Default.SelfImprovement
                                            "water_drop" -> Icons.Default.WaterDrop
                                            "no_food" -> Icons.Default.NoFood
                                            "directions_walk" -> Icons.Default.DirectionsWalk
                                            "bedtime" -> Icons.Default.Bedtime
                                            else -> Icons.Default.TaskAlt
                                        },
                                        contentDescription = "Habit Icon",
                                        tint = if (isCompleted) VolcanicOrange else Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )

                                    Column {
                                        Text(
                                            text = habit.name,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = "Category: ${habit.category} | Difficulty: ${habit.difficulty}",
                                            fontSize = 11.sp,
                                            color = MutedSilver
                                        )
                                    }
                                }

                                Checkbox(
                                    checked = isCompleted,
                                    onCheckedChange = { onToggleHabit(habit.id) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = VolcanicOrange,
                                        uncheckedColor = Color.DarkGray,
                                        checkmarkColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }

    // AI Coach feedback sheet dialog
    if (showCoachSheet) {
        AlertDialog(
            onDismissRequest = { showCoachSheet = false },
            confirmButton = {
                TextButton(onClick = { showCoachSheet = false }) {
                    Text("DISMISS DIRECTIVE", color = VolcanicOrange, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Terminal, contentDescription = "Terminal", tint = VolcanicOrange)
                    Text("AI COACH REVIEW LOG", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, fontFamily = FontFamily.Monospace)
                }
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    if (isCoachLoading) {
                        Box(modifier = Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = VolcanicOrange)
                        }
                    } else {
                        Text(
                            text = coachFeedback,
                            color = Color.LightGray,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            },
            containerColor = DarkCharcoal,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
        )
    }
}
