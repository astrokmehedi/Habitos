package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AlarmLog
import com.example.data.UserConfig
import com.example.ui.theme.*

@Composable
fun StatsScreen(
    config: UserConfig,
    alarmLogs: List<AlarmLog>,
    onPurchaseHealer: () -> Unit
) {
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
                text = "BIOMETRICS & VISUALIZATIONS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MutedSilver,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.Monospace
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
                    text = "TREE",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VolcanicOrange,
                    letterSpacing = (-0.5).sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Dynamic Discipline Tree Canvas
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCharcoal),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "VIRTUAL DISCIPLINE TREE // STATUS: ${config.treeHealth}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MutedSilver,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Draw Trunk
                            drawLine(
                                color = Color(0xFF5D4037),
                                start = Offset(w / 2, h - 20f),
                                end = Offset(w / 2, h / 2 + 10f),
                                strokeWidth = 14f
                            )

                            // Draw Main Branches depending on health
                            if (config.treeHealth > 20) {
                                drawLine(
                                    color = Color(0xFF5D4037),
                                    start = Offset(w / 2, h / 2 + 10f),
                                    end = Offset(w / 2 - 40f, h / 2 - 30f),
                                    strokeWidth = 8f
                                )
                                drawLine(
                                    color = Color(0xFF5D4037),
                                    start = Offset(w / 2, h / 2 + 10f),
                                    end = Offset(w / 2 + 40f, h / 2 - 30f),
                                    strokeWidth = 8f
                                )
                            }

                            if (config.treeHealth > 40) {
                                drawLine(
                                    color = Color(0xFF5D4037),
                                    start = Offset(w / 2 - 40f, h / 2 - 30f),
                                    end = Offset(w / 2 - 70f, h / 2 - 70f),
                                    strokeWidth = 6f
                                )
                                drawLine(
                                    color = Color(0xFF5D4037),
                                    start = Offset(w / 2 + 40f, h / 2 - 30f),
                                    end = Offset(w / 2 + 70f, h / 2 - 70f),
                                    strokeWidth = 6f
                                )
                            }

                            // Draw Leaves depending on health
                            if (config.treeHealth > 50) {
                                // Left Side Leaves
                                drawCircle(
                                    color = VolcanicOrange,
                                    radius = 18f,
                                    center = Offset(w / 2 - 70f, h / 2 - 70f)
                                )
                                drawCircle(
                                    color = EmeraldGreen,
                                    radius = 12f,
                                    center = Offset(w / 2 - 85f, h / 2 - 80f)
                                )

                                // Right Side Leaves
                                drawCircle(
                                    color = VolcanicOrange,
                                    radius = 18f,
                                    center = Offset(w / 2 + 70f, h / 2 - 70f)
                                )
                                drawCircle(
                                    color = EmeraldGreen,
                                    radius = 12f,
                                    center = Offset(w / 2 + 85f, h / 2 - 80f)
                                )
                            }

                            if (config.treeHealth > 80) {
                                // Full Bloom Center Leaves
                                drawCircle(
                                    color = VolcanicOrange,
                                    radius = 24f,
                                    center = Offset(w / 2, h / 2 - 50f)
                                )
                                drawCircle(
                                    color = EmeraldGreen,
                                    radius = 16f,
                                    center = Offset(w / 2 - 20f, h / 2 - 70f)
                                )
                                drawCircle(
                                    color = Color.White,
                                    radius = 12f,
                                    center = Offset(w / 2 + 20f, h / 2 - 70f)
                                )
                            }

                            // Dead / Sick indicators if health is low
                            if (config.treeHealth <= 30) {
                                // Dry leaf fallen
                                drawLine(
                                    color = Color.Gray,
                                    start = Offset(w / 2 - 20f, h - 30f),
                                    end = Offset(w / 2 - 30f, h - 25f),
                                    strokeWidth = 4f
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (config.treeHealth > 60) "Your discipline tree is healthy and blooming!" else "Your tree is dying. Complete missions to heal it.",
                        fontSize = 12.sp,
                        color = MutedSilver,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Store Purchase button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Spend Coins to Restore Health", fontSize = 11.sp, color = MutedSilver, fontFamily = FontFamily.Monospace)
                            Text("50 Coins = +15% Health Healer", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Button(
                            onClick = { onPurchaseHealer() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (config.coins >= 50) VolcanicOrange else Color.DarkGray
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = config.coins >= 50
                        ) {
                            Icon(Icons.Default.Healing, contentDescription = "Heal")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("BUY HEALER", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "ACHIEVEMENT VAULT",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = VolcanicOrange,
                letterSpacing = 1.sp,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Grids of badges
            val badges = listOf(
                BadgeItem("Early Bird", "Wake up at 5 AM verified", config.currentStreak >= 1, Icons.Default.Alarm),
                BadgeItem("Iron Mind", "Execute 5 habit sessions", config.level >= 2, Icons.Default.FitnessCenter),
                BadgeItem("Silent Monk", "Complete meditation drill", config.disciplineScore >= 70, Icons.Default.SelfImprovement),
                BadgeItem("Reader Pro", "Finish 50 pages of books", config.coins >= 150, Icons.Default.Book),
                BadgeItem("The Warrior", "Reach discipline score 90+", config.disciplineScore >= 90, Icons.Default.Security),
                BadgeItem("Unbreakable", "Establish 7+ day streak", config.currentStreak >= 7, Icons.Default.LocalFireDepartment),
                BadgeItem("90-Day Legend", "Complete transform mode", config.challengeType == "90_DAY" && config.currentStreak >= 90, Icons.Default.EmojiEvents)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                badges.forEach { badge ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (badge.isUnlocked) VolcanicOrangeMuted else DarkCharcoal
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (badge.isUnlocked) VolcanicOrange.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.05f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                badge.icon,
                                contentDescription = badge.name,
                                tint = if (badge.isUnlocked) VolcanicOrange else Color.DarkGray,
                                modifier = Modifier.size(32.dp)
                            )

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = badge.name.uppercase(),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (badge.isUnlocked) Color.White else MutedSilver,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    if (badge.isUnlocked) {
                                        Icon(Icons.Default.Verified, contentDescription = "Unlocked", tint = VolcanicOrange, modifier = Modifier.size(14.dp))
                                    }
                                }
                                Text(
                                    text = badge.description,
                                    fontSize = 11.sp,
                                    color = MutedSilver
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

data class BadgeItem(
    val name: String,
    val description: String,
    val isUnlocked: Boolean,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
