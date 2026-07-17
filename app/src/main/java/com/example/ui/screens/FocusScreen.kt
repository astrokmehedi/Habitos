package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun FocusScreen(
    secondsLeft: Int,
    isRunning: Boolean,
    completedSessions: Int,
    unlockCount: Int,
    onStartTimer: (Int) -> Unit,
    onStopTimer: () -> Unit,
    onResetTimer: () -> Unit,
    onSimulatePhoneUnlock: () -> Unit
) {
    var selectedMinutes by remember { mutableStateOf(25) }
    
    val minutes = secondsLeft / 60
    val seconds = secondsLeft % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)
    
    val totalSeconds = selectedMinutes * 60
    val progress = if (totalSeconds > 0) secondsLeft.toFloat() / totalSeconds.toFloat() else 0f

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

            Text(
                text = "DEEP WORK ENGINE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MutedSilver,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.Monospace
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "POMODORO ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "FOCUS",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VolcanicOrange,
                    letterSpacing = (-0.5).sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Timer Circle Card
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(240.dp)
            ) {
                Canvas(modifier = Modifier.size(200.dp)) {
                    drawCircle(
                        color = Color.DarkGray.copy(alpha = 0.5f),
                        style = Stroke(width = 10.dp.toPx())
                    )
                    drawArc(
                        color = VolcanicOrange,
                        startAngle = -90f,
                        sweepAngle = progress * 360f,
                        useCenter = false,
                        style = Stroke(width = 10.dp.toPx())
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formattedTime,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = if (isRunning) "ACTIVE FOCUS" else "STANDBY",
                        fontSize = 10.sp,
                        color = MutedSilver,
                        letterSpacing = 2.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Preset Selectors
            if (!isRunning) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(15, 25, 45, 60).forEach { mins ->
                        val isSelected = selectedMinutes == mins
                        Card(
                            onClick = { selectedMinutes = mins },
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) VolcanicOrange else DarkCharcoal
                            ),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
                                Text("${mins}M", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isRunning) {
                    IconButton(
                        onClick = { onStopTimer() },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.Pause, contentDescription = "Pause", tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                } else {
                    IconButton(
                        onClick = { onStartTimer(selectedMinutes) },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Start", tint = VolcanicOrange, modifier = Modifier.size(36.dp))
                    }
                }

                Spacer(modifier = Modifier.width(32.dp))

                IconButton(
                    onClick = { onResetTimer() },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = MutedSilver, modifier = Modifier.size(28.dp))
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Distraction Counters / Analytics
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCharcoal),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "DISTRACTION SHIELD LOG",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = VolcanicOrange,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LockOpen, contentDescription = "Unlock Counter", tint = MutedSilver, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("UNLOCKED COUNT TODAY", fontSize = 11.sp, color = MutedSilver, fontFamily = FontFamily.Monospace)
                            }
                            Text("$unlockCount UNLOCKS", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }

                        Button(
                            onClick = { onSimulatePhoneUnlock() },
                            colors = ButtonDefaults.buttonColors(containerColor = MatteGray),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("SIMULATE UNLOCK", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Divider(color = Color.White.copy(alpha = 0.05f))

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "FOCUS METRIC STATUS:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MutedSilver,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Completed Sessions: $completedSessions Deep Sessions (+${completedSessions * 25} XP | +${completedSessions * 10} Coins earned)",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
