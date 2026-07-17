package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ui.screens.*
import com.example.ui.theme.PitchBlack
import com.example.ui.theme.VolcanicOrange
import com.example.viewmodel.DisciplineViewModel

@Composable
fun DisciplineApp(
    viewModel: DisciplineViewModel = viewModel()
) {
    val configState by viewModel.userConfig.collectAsState()
    val habits by viewModel.allHabits.collectAsState()
    val completions by viewModel.todayCompletions.collectAsState()
    val journalEntry by viewModel.todayJournal.collectAsState()
    val alarmLogs by viewModel.alarmLogs.collectAsState()
    
    val isAlarmRinging by viewModel.isAlarmRinging.collectAsState()
    val alarmVerificationType by viewModel.alarmVerificationType.collectAsState()
    val isCoachLoading by viewModel.isCoachLoading.collectAsState()
    val coachFeedback by viewModel.aiCoachFeedback.collectAsState()
    val motivationalQuote by viewModel.motivationalQuote.collectAsState()

    // Focus state
    val focusSecondsLeft by viewModel.focusSecondsLeft.collectAsState()
    val isFocusTimerRunning by viewModel.isFocusTimerRunning.collectAsState()
    val focusCompletedSessions by viewModel.focusCompletedSessions.collectAsState()
    val phoneUnlockCount by viewModel.phoneUnlockCount.collectAsState()

    // History logs
    val journalHistory by viewModel.allJournalEntries.collectAsState(initial = emptyList())

    val navController = rememberNavController()

    // Verify Onboarding Complete
    val config = configState
    if (config == null || !config.onboardingCompleted) {
        OnboardingScreen(
            onComplete = { name, wake, sleep, goal, challenge, badHabits, exercise, reading, reminders ->
                viewModel.completeOnboarding(name, wake, sleep, goal, challenge, badHabits, exercise, reading, reminders)
            }
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = PitchBlack,
                bottomBar = {
                    NavigationBar(
                        containerColor = PitchBlack,
                        contentColor = VolcanicOrange,
                        tonalElevation = 8.dp
                    ) {
                        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                        NavigationBarItem(
                            selected = currentRoute == "dashboard",
                            onClick = { navController.navigate("dashboard") { popUpTo(0) } },
                            icon = { Icon(Icons.Default.Terminal, contentDescription = "Dashboard") },
                            label = { Text("COMMAND", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = VolcanicOrange,
                                selectedTextColor = VolcanicOrange,
                                unselectedIconColor = Color.White.copy(alpha = 0.35f),
                                unselectedTextColor = Color.White.copy(alpha = 0.35f),
                                indicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.testTag("nav_dashboard")
                        )

                        NavigationBarItem(
                            selected = currentRoute == "habits",
                            onClick = { navController.navigate("habits") { popUpTo(0) } },
                            icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Habits") },
                            label = { Text("HABITS", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = VolcanicOrange,
                                selectedTextColor = VolcanicOrange,
                                unselectedIconColor = Color.White.copy(alpha = 0.35f),
                                unselectedTextColor = Color.White.copy(alpha = 0.35f),
                                indicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.testTag("nav_habits")
                        )

                        NavigationBarItem(
                            selected = currentRoute == "challenge",
                            onClick = { navController.navigate("challenge") { popUpTo(0) } },
                            icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Challenge") },
                            label = { Text("CHALLENGE", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = VolcanicOrange,
                                selectedTextColor = VolcanicOrange,
                                unselectedIconColor = Color.White.copy(alpha = 0.35f),
                                unselectedTextColor = Color.White.copy(alpha = 0.35f),
                                indicatorColor = Color.Transparent
                            )
                        )

                        NavigationBarItem(
                            selected = currentRoute == "journal",
                            onClick = { navController.navigate("journal") { popUpTo(0) } },
                            icon = { Icon(Icons.Default.Book, contentDescription = "Journal") },
                            label = { Text("JOURNAL", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = VolcanicOrange,
                                selectedTextColor = VolcanicOrange,
                                unselectedIconColor = Color.White.copy(alpha = 0.35f),
                                unselectedTextColor = Color.White.copy(alpha = 0.35f),
                                indicatorColor = Color.Transparent
                            )
                        )

                        NavigationBarItem(
                            selected = currentRoute == "stats",
                            onClick = { navController.navigate("stats") { popUpTo(0) } },
                            icon = { Icon(Icons.Default.Park, contentDescription = "Tree") },
                            label = { Text("TREE", fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = VolcanicOrange,
                                selectedTextColor = VolcanicOrange,
                                unselectedIconColor = Color.White.copy(alpha = 0.35f),
                                unselectedTextColor = Color.White.copy(alpha = 0.35f),
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "dashboard",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("dashboard") {
                        DashboardScreen(
                            config = config,
                            habits = habits,
                            completions = completions,
                            motivationalQuote = motivationalQuote,
                            onToggleHabit = { viewModel.toggleHabit(it) },
                            onNavigateToFocus = { navController.navigate("focus") },
                            onNavigateToJournal = { navController.navigate("journal") },
                            onNavigateToStats = { navController.navigate("stats") },
                            onTriggerAlarmDemo = { viewModel.triggerInAppAlarm(it) },
                            onRunReview = { viewModel.runCoachReview() },
                            isCoachLoading = isCoachLoading,
                            coachFeedback = coachFeedback,
                            onPurchaseHealer = { viewModel.purchaseTreeHealer() }
                        )
                    }

                    composable("habits") {
                        HabitsScreen(
                            habits = habits,
                            completions = completions,
                            onAddHabit = { name, icon, reminder, category, difficulty, target ->
                                viewModel.addCustomHabit(name, icon, reminder, category, difficulty, target)
                            },
                            onDeleteHabit = { viewModel.deleteHabit(it) }
                        )
                    }

                    composable("challenge") {
                        ChallengeScreen(
                            config = config,
                            habits = habits,
                            completions = completions,
                            onToggleHabit = { viewModel.toggleHabit(it) }
                        )
                    }

                    composable("journal") {
                        JournalScreen(
                            entry = journalEntry,
                            history = journalHistory,
                            onSaveMorning = { gratitude, plan, mood ->
                                viewModel.saveMorningJournal(gratitude, plan, mood)
                            },
                            onSaveNight = { reflection, win, mistake, tomorrow, mood ->
                                viewModel.saveNightJournal(reflection, win, mistake, tomorrow, mood)
                            }
                        )
                    }

                    composable("stats") {
                        StatsScreen(
                            config = config,
                            alarmLogs = alarmLogs,
                            onPurchaseHealer = { viewModel.purchaseTreeHealer() }
                        )
                    }

                    composable("focus") {
                        FocusScreen(
                            secondsLeft = focusSecondsLeft,
                            isRunning = isFocusTimerRunning,
                            completedSessions = focusCompletedSessions,
                            unlockCount = phoneUnlockCount,
                            onStartTimer = { viewModel.startFocusSession(it) },
                            onStopTimer = { viewModel.stopFocusSession() },
                            onResetTimer = { viewModel.resetFocusSession() },
                            onSimulatePhoneUnlock = { viewModel.incrementUnlockCount() }
                        )
                    }
                }
            }

            // High Priority Alarm Interceptor Screen
            if (isAlarmRinging) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PitchBlack),
                    contentAlignment = Alignment.Center
                ) {
                    AlarmScreen(
                        verificationType = alarmVerificationType,
                        onVerifySuccess = { bitmap ->
                            viewModel.verifyAndStopAlarm(bitmap) { isSuccess, msg ->
                                // Trigger callback if needed
                            }
                        },
                        onVerifyFailed = { msg ->
                            // Feedback on failure
                        },
                        isLoading = isCoachLoading
                    )
                }
            }
        }
    }
}
