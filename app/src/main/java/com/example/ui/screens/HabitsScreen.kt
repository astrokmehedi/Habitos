package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Habit
import com.example.ui.theme.*

@Composable
fun HabitsScreen(
    habits: List<Habit>,
    completions: Set<Int>,
    onAddHabit: (String, String, String, String, String, Int) -> Unit,
    onDeleteHabit: (Habit) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    
    // Custom Habit Form State
    var habitName by remember { mutableStateOf("") }
    var habitCategory by remember { mutableStateOf("Health") }
    var habitDifficulty by remember { mutableStateOf("Medium") }
    var habitIcon by remember { mutableStateOf("fitness_center") }
    var habitTarget by remember { mutableStateOf("30") }
    var habitReminder by remember { mutableStateOf("08:00") }

    val categories = listOf("Mindset", "Health", "Focus", "Nutrition")
    val difficulties = listOf("Easy", "Medium", "Hard")
    val icons = listOf("fitness_center", "alarm", "book", "edit", "self_improvement", "water_drop")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PitchBlack,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = VolcanicOrange,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("add_habit_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit")
            }
        }
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
                text = "DISCIPLINE PARAMETERS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MutedSilver,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.Monospace
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "ROUTINES & ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "LOGS",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = VolcanicOrange,
                    letterSpacing = (-0.5).sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "These habits represent the active contract you are currently executing. Missing habits decreases your Daily Discipline Score.",
                fontSize = 12.sp,
                color = MutedSilver,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (habits.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No habits configured. Tap the '+' button to declare your routine.", color = MutedSilver, fontSize = 13.sp, textAlign = TextAlign.Center)
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    habits.forEach { habit ->
                        val isDone = completions.contains(habit.id)
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DarkCharcoal),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
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
                                            else -> Icons.Default.Check
                                        },
                                        contentDescription = habit.name,
                                        tint = if (isDone) VolcanicOrange else Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )

                                    Column {
                                        Text(
                                            text = habit.name,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Category: ${habit.category} | Daily target: ${habit.target} min/reps",
                                            fontSize = 11.sp,
                                            color = MutedSilver
                                        )
                                    }
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = when (habit.difficulty) {
                                                "Easy" -> Color(0x334CAF50)
                                                "Medium" -> Color(0x33FF9800)
                                                "Hard" -> Color(0x33FF5722)
                                                else -> Color.DarkGray
                                            }
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                                    ) {
                                        Text(
                                            text = habit.difficulty.uppercase(),
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontFamily = FontFamily.Monospace,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }

                                    // Delete action button
                                    if (!habit.isChallengeDefault) {
                                        IconButton(onClick = { onDeleteHabit(habit) }) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Delete Habit",
                                                tint = WarningRed,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        if (habitName.isNotBlank()) {
                            onAddHabit(
                                habitName,
                                habitIcon,
                                habitReminder,
                                habitCategory,
                                habitDifficulty,
                                habitTarget.toIntOrNull() ?: 1
                            )
                            habitName = ""
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VolcanicOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("ADD HABIT", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("CANCEL", color = MutedSilver, fontFamily = FontFamily.Monospace)
                }
            },
            title = {
                Text("DECLARE NEW HABIT", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, fontFamily = FontFamily.Monospace)
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = habitName,
                        onValueChange = { habitName = it },
                        label = { Text("Habit Name") },
                        placeholder = { Text("e.g. Read Philosophy, Meditate") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VolcanicOrange,
                            focusedLabelColor = VolcanicOrange,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                            unfocusedLabelColor = MutedSilver
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Text("Category", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MutedSilver, fontFamily = FontFamily.Monospace)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        categories.forEach { cat ->
                            val isSelected = habitCategory == cat
                            Card(
                                onClick = { habitCategory = cat },
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) VolcanicOrange else MatteGray
                                ),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                            ) {
                                Box(modifier = Modifier.padding(6.dp), contentAlignment = Alignment.Center) {
                                    Text(cat, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }

                    Text("Difficulty", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MutedSilver, fontFamily = FontFamily.Monospace)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        difficulties.forEach { diff ->
                            val isSelected = habitDifficulty == diff
                            Card(
                                onClick = { habitDifficulty = diff },
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) VolcanicOrange else MatteGray
                                ),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                            ) {
                                Box(modifier = Modifier.padding(6.dp), contentAlignment = Alignment.Center) {
                                    Text(diff, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }

                    Text("Choose Icon", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MutedSilver, fontFamily = FontFamily.Monospace)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        icons.forEach { icon ->
                            val isSelected = habitIcon == icon
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        if (isSelected) VolcanicOrange else MatteGray,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { habitIcon = icon },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (icon) {
                                        "alarm" -> Icons.Default.Alarm
                                        "fitness_center" -> Icons.Default.FitnessCenter
                                        "book" -> Icons.Default.Book
                                        "edit" -> Icons.Default.Edit
                                        "self_improvement" -> Icons.Default.SelfImprovement
                                        "water_drop" -> Icons.Default.WaterDrop
                                        else -> Icons.Default.Check
                                    },
                                    contentDescription = icon,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = habitTarget,
                            onValueChange = { habitTarget = it },
                            label = { Text("Daily Target") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = VolcanicOrange,
                                focusedLabelColor = VolcanicOrange,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                                unfocusedLabelColor = MutedSilver
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = habitReminder,
                            onValueChange = { habitReminder = it },
                            label = { Text("Reminder (HH:mm)") },
                            modifier = Modifier.weight(1f),
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
            },
            containerColor = DarkCharcoal,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
        )
    }
}
