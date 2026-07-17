package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_config")
data class UserConfig(
    @PrimaryKey val id: Int = 1,
    val name: String = "",
    val wakeUpTime: String = "05:00",
    val sleepTime: String = "22:00",
    val biggestGoal: String = "",
    val challengeType: String = "30_DAY", // "30_DAY" or "90_DAY"
    val badHabits: String = "", // Comma-separated
    val dailyExerciseTarget: Int = 30, // in minutes
    val readingTarget: Int = 10, // in pages
    val reminderTimes: String = "08:00,12:00,18:00,21:00", // Comma-separated
    val onboardingCompleted: Boolean = false,
    val disciplineScore: Int = 50,
    val xp: Int = 0,
    val level: Int = 1,
    val coins: Int = 100,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val treeHealth: Int = 50, // 0 to 100
    val hasPinLock: Boolean = false,
    val pinCode: String = "",
    val lastScoreUpdateDate: String = ""
)

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val iconName: String, // String representation of icon (e.g., "fitness_center", "book")
    val reminderTime: String = "08:00",
    val category: String = "Mindset", // "Mindset", "Health", "Focus", "Nutrition"
    val difficulty: String = "Medium", // "Easy", "Medium", "Hard"
    val frequency: String = "Daily", // "Daily", "Weekly"
    val target: Int = 1, // number of times or minutes
    val notes: String = "",
    val isChallengeDefault: Boolean = false, // part of the 30/90 day preset
    val isArchived: Boolean = false
)

@Entity(tableName = "habit_completion_logs")
data class HabitCompletionLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val habitId: Int,
    val date: String, // "YYYY-MM-DD"
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = "",
    val isCheatingAttempt: Boolean = false
)

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey val date: String, // "YYYY-MM-DD"
    val morningGratitude: String = "",
    val morningPlan: String = "",
    val nightReflection: String = "",
    val biggestWin: String = "",
    val biggestMistake: String = "",
    val tomorrowPlan: String = "",
    val mood: Int = 3, // 1 to 5
    val completedMorning: Boolean = false,
    val completedNight: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "alarm_logs")
data class AlarmLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String, // "YYYY-MM-DD"
    val scheduledTime: String, // "05:00"
    val wakeUpTime: String, // "05:04"
    val verificationTime: String, // "05:05"
    val verificationType: String, // "bathroom_photo", "math", "memory", "typing"
    val responseDelaySeconds: Int,
    val isSuccessful: Boolean,
    val photoPath: String? = null
)
