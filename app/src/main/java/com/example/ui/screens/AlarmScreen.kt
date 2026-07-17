package com.example.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun AlarmScreen(
    verificationType: String, // "bathroom_photo", "math", "memory", "typing"
    onVerifySuccess: (Bitmap?) -> Unit,
    onVerifyFailed: (String) -> Unit,
    isLoading: Boolean
) {
    // Pulsing Color effect
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alphaColor by infiniteTransition.animateColor(
        initialValue = Color(0xFF1A0505),
        targetValue = Color(0xFF4A0D0D),
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_color"
    )

    var verificationFeedback by remember { mutableStateOf("") }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Math Challenge state
    var mathNum1 by remember { mutableStateOf(12) }
    var mathNum2 by remember { mutableStateOf(7) }
    var mathNum3 by remember { mutableStateOf(9) } // 12 * 7 + 9 = 93
    var mathAnswerInput by remember { mutableStateOf("") }
    var mathCompletedQuestions by remember { mutableStateOf(0) }

    // Memory match state
    var memoryCards by remember { mutableStateOf(generateMemoryCards()) }
    var selectedMemoryIndices by remember { mutableStateOf<List<Int>>(emptyList()) }
    var matchedMemoryIndices by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var memorySecondsLeft by remember { mutableStateOf(20) }

    // Typing state
    val targetTypeQuote = "No man is free who is not master of himself."
    var typingInput by remember { mutableStateOf("") }

    // Launcher for camera photo capture
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            capturedBitmap = bitmap
            verificationFeedback = "Image captured successfully. Verifying with AI..."
            onVerifySuccess(bitmap)
        } else {
            verificationFeedback = "Capture cancelled. You must take a real-time photo."
        }
    }

    // Memory countdown timer
    if (verificationType == "memory") {
        LaunchedEffect(Unit) {
            while (memorySecondsLeft > 0 && matchedMemoryIndices.size < 12) {
                delay(1000)
                memorySecondsLeft -= 1
            }
            if (matchedMemoryIndices.size < 12) {
                onVerifyFailed("Memory Match timed out. The system reset.")
                memorySecondsLeft = 20
                matchedMemoryIndices = emptySet()
                selectedMemoryIndices = emptyList()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(alphaColor)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Alarm,
                contentDescription = "Alarm Active",
                tint = VolcanicOrange,
                modifier = Modifier
                    .size(64.dp)
                    .padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "WAKE UP ",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "PROTOCOL",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = VolcanicOrange,
                    letterSpacing = (-0.5).sp
                )
            }

            Text(
                text = "ALARM SYSTEM IS ACTIVE // SOLVE VERIFICATION TO SILENCE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MutedSilver,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            if (isLoading) {
                Box(modifier = Modifier.height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = VolcanicOrange)
                }
            } else {
                when (verificationType) {
                    "bathroom_photo" -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DarkCharcoal),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, VolcanicOrange.copy(alpha = 0.35f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "BATHROOM PHOTO VERIFICATION",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Leave your bed immediately. Go to your bathroom sink or mirror and snap a real-time photo to prove you are awake.",
                                    fontSize = 12.sp,
                                    color = MutedSilver,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 18.sp
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Button(
                                    onClick = { cameraLauncher.launch() },
                                    colors = ButtonDefaults.buttonColors(containerColor = VolcanicOrange),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.testTag("alarm_camera_capture")
                                ) {
                                    Icon(Icons.Default.CameraAlt, contentDescription = "Camera")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("SNAP BATHROOM", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    }

                    "math" -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DarkCharcoal),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, VolcanicOrange.copy(alpha = 0.35f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "MATH REASONING DRILL (${mathCompletedQuestions}/3)",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "What is: $mathNum1 * $mathNum2 + $mathNum3 ?",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    fontFamily = FontFamily.Monospace
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                OutlinedTextField(
                                    value = mathAnswerInput,
                                    onValueChange = { mathAnswerInput = it },
                                    label = { Text("Answer") },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = VolcanicOrange,
                                        focusedLabelColor = VolcanicOrange,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                                        unfocusedLabelColor = MutedSilver
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        val expected = mathNum1 * mathNum2 + mathNum3
                                        val entered = mathAnswerInput.trim().toIntOrNull()
                                        if (entered == expected) {
                                            mathCompletedQuestions += 1
                                            if (mathCompletedQuestions >= 3) {
                                                onVerifySuccess(null)
                                            } else {
                                                // Generate next
                                                mathNum1 = (8..15).random()
                                                mathNum2 = (5..9).random()
                                                mathNum3 = (4..15).random()
                                                mathAnswerInput = ""
                                                verificationFeedback = "Correct. Solve the next one."
                                            }
                                        } else {
                                            verificationFeedback = "Incorrect. Answer was $expected. System recalculated a new formula."
                                            mathNum1 = (8..15).random()
                                            mathNum2 = (5..9).random()
                                            mathNum3 = (4..15).random()
                                            mathAnswerInput = ""
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = VolcanicOrange),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("SUBMIT", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    }

                    "memory" -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DarkCharcoal),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, VolcanicOrange.copy(alpha = 0.35f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "MEMORY DRILL // TIME LEFT: $memorySecondsLeft",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                // Grid of cards (12 total)
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    for (row in 0 until 4) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            for (col in 0 until 3) {
                                                val idx = row * 3 + col
                                                val card = memoryCards[idx]
                                                val isRevealed = selectedMemoryIndices.contains(idx) || matchedMemoryIndices.contains(idx)

                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(60.dp)
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(if (isRevealed) VolcanicOrange else Color.DarkGray)
                                                        .clickable {
                                                            if (selectedMemoryIndices.size < 2 && !isRevealed) {
                                                                val newList = selectedMemoryIndices + idx
                                                                selectedMemoryIndices = newList
                                                                if (newList.size == 2) {
                                                                    // Check for match
                                                                    if (memoryCards[newList[0]] == memoryCards[newList[1]]) {
                                                                        matchedMemoryIndices = matchedMemoryIndices + newList
                                                                        selectedMemoryIndices = emptyList()
                                                                        if (matchedMemoryIndices.size == 12) {
                                                                            onVerifySuccess(null)
                                                                        }
                                                                    } else {
                                                                        // Flip back after a short delay
                                                                        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                                                            selectedMemoryIndices = emptyList()
                                                                        }, 800)
                                                                    }
                                                                }
                                                            }
                                                        },
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    if (isRevealed) {
                                                        Text(card, fontWeight = FontWeight.ExtraBold, color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                                                    } else {
                                                        Text("?", color = MutedSilver, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    "typing" -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DarkCharcoal),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, VolcanicOrange.copy(alpha = 0.35f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "PHILOSOPHY DRILL",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontFamily = FontFamily.Monospace
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "\"$targetTypeQuote\"",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VolcanicOrange,
                                    textAlign = TextAlign.Center,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                OutlinedTextField(
                                    value = typingInput,
                                    onValueChange = { typingInput = it },
                                    label = { Text("Type exactly as written") },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = VolcanicOrange,
                                        focusedLabelColor = VolcanicOrange,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                                        unfocusedLabelColor = MutedSilver
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        if (typingInput.trim() == targetTypeQuote) {
                                            onVerifySuccess(null)
                                        } else {
                                            verificationFeedback = "Mismatched character. Type exact string."
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = VolcanicOrange),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("ACTIVATE EXIT", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    }
                }
            }

            if (verificationFeedback.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = VolcanicOrangeMuted),
                    border = BorderStroke(1.dp, VolcanicOrange.copy(alpha = 0.35f)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = verificationFeedback,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

private fun generateMemoryCards(): List<String> {
    val list = listOf("WILL", "IRON", "MIND", "ZEAL", "WAKE", "GRIT", "WILL", "IRON", "MIND", "ZEAL", "WAKE", "GRIT")
    return list.shuffled()
}
