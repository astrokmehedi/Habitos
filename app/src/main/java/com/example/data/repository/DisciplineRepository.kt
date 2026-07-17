package com.example.data.repository

import com.example.data.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.*

class DisciplineRepository(
    private val userConfigDao: UserConfigDao,
    private val habitDao: HabitDao,
    private val habitCompletionLogDao: HabitCompletionLogDao,
    private val journalEntryDao: JournalEntryDao,
    private val alarmLogDao: AlarmLogDao
) {
    val userConfig: Flow<UserConfig?> = userConfigDao.getUserConfig()
    val allHabits: Flow<List<Habit>> = habitDao.getAllHabits()
    val allJournalEntries: Flow<List<JournalEntry>> = journalEntryDao.getAllEntries()
    val allAlarmLogs: Flow<List<AlarmLog>> = alarmLogDao.getAllAlarmLogs()

    fun getLogsForDate(date: String): Flow<List<HabitCompletionLog>> = 
        habitCompletionLogDao.getLogsForDate(date)

    fun getJournalEntryForDate(date: String): Flow<JournalEntry?> = 
        journalEntryDao.getEntryByDate(date)

    suspend fun saveUserConfig(config: UserConfig) {
        userConfigDao.insertOrUpdate(config)
    }

    suspend fun insertHabit(habit: Habit): Long {
        return habitDao.insertHabit(habit)
    }

    suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit)
    }

    suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit)
    }

    suspend fun logHabitCompletion(habitId: Int, date: String, notes: String = "", isCheating: Boolean = false) {
        val existing = habitCompletionLogDao.getLogForHabitAndDateDirect(habitId, date)
        if (existing == null) {
            val log = HabitCompletionLog(
                habitId = habitId,
                date = date,
                notes = notes,
                isCheatingAttempt = isCheating
            )
            habitCompletionLogDao.insertLog(log)
            recalculateDisciplineScore(date)
        }
    }

    suspend fun removeHabitCompletion(habitId: Int, date: String) {
        habitCompletionLogDao.deleteLog(habitId, date)
        recalculateDisciplineScore(date)
    }

    suspend fun saveJournalEntry(entry: JournalEntry) {
        journalEntryDao.insertEntry(entry)
        recalculateDisciplineScore(entry.date)
    }

    suspend fun logAlarmEvent(log: AlarmLog) {
        alarmLogDao.insertAlarmLog(log)
        recalculateDisciplineScore(log.date)
    }

    // Populate default 30/90 Day Discipline challenge habits
    suspend fun prePopulateDefaultHabits() {
        val defaults = listOf(
            Habit(name = "Wake up at 5 AM", iconName = "alarm", category = "Mindset", difficulty = "Hard", target = 1, isChallengeDefault = true),
            Habit(name = "Exercise Daily", iconName = "fitness_center", category = "Health", difficulty = "Medium", target = 30, isChallengeDefault = true),
            Habit(name = "Read 10 Pages", iconName = "book", category = "Focus", difficulty = "Easy", target = 10, isChallengeDefault = true),
            Habit(name = "Journal Morning & Night", iconName = "edit", category = "Mindset", difficulty = "Easy", target = 2, isChallengeDefault = true),
            Habit(name = "Sit in Silence 30 mins", iconName = "self_improvement", category = "Mindset", difficulty = "Medium", target = 30, isChallengeDefault = true),
            Habit(name = "Drink 3L Water", iconName = "water_drop", category = "Health", difficulty = "Easy", target = 3, isChallengeDefault = true),
            Habit(name = "No Social Media", iconName = "block", category = "Focus", difficulty = "Medium", target = 1, isChallengeDefault = true),
            Habit(name = "Cold Shower", iconName = "ac_unit", category = "Health", difficulty = "Hard", target = 1, isChallengeDefault = true),
            Habit(name = "No Junk Food", iconName = "no_food", category = "Health", difficulty = "Medium", target = 1, isChallengeDefault = true),
            Habit(name = "Go For Walk (No Music)", iconName = "directions_walk", category = "Focus", difficulty = "Medium", target = 20, isChallengeDefault = true),
            Habit(name = "Sleep on Time", iconName = "bedtime", category = "Health", difficulty = "Medium", target = 1, isChallengeDefault = true),
            Habit(name = "Done Before Checking Phone", iconName = "phonelink_erase", category = "Focus", difficulty = "Hard", target = 1, isChallengeDefault = true)
        )
        for (habit in defaults) {
            habitDao.insertHabit(habit)
        }
    }

    // Discipline Score Algorithm: 0 - 100
    suspend fun recalculateDisciplineScore(date: String) {
        val config = userConfigDao.getUserConfigDirect() ?: return
        
        // Get all active habits
        val habits = habitDao.getAllHabits().firstOrNull() ?: emptyList()
        if (habits.isEmpty()) return

        // Get completions for today
        val logs = habitCompletionLogDao.getLogsForDate(date).firstOrNull() ?: emptyList()
        val completedIds = logs.map { it.habitId }.toSet()
        val cheatingAttempts = logs.count { it.isCheatingAttempt }

        // Core points from habits
        var totalPointsEarned = 0
        var maxPossiblePoints = 0

        for (habit in habits) {
            val habitPoints = when (habit.difficulty) {
                "Easy" -> 5
                "Medium" -> 10
                "Hard" -> 15
                else -> 8
            }
            maxPossiblePoints += habitPoints
            if (completedIds.contains(habit.id)) {
                totalPointsEarned += habitPoints
            }
        }

        // Journal points
        val journal = journalEntryDao.getEntryByDateDirect(date)
        if (journal != null) {
            maxPossiblePoints += 10
            if (journal.completedMorning) totalPointsEarned += 5
            if (journal.completedNight) totalPointsEarned += 5
        } else {
            maxPossiblePoints += 10 // potential
        }

        // Alarm points: Wake up at 5 AM
        val alarmLogs = alarmLogDao.getAllAlarmLogs().firstOrNull() ?: emptyList()
        val alarmToday = alarmLogs.firstOrNull { it.date == date }
        if (alarmToday != null) {
            maxPossiblePoints += 20
            if (alarmToday.isSuccessful) {
                val delayPenalty = (alarmToday.responseDelaySeconds / 60) * 2 // -2 pts per min delay
                val alarmEarned = (20 - delayPenalty).coerceAtLeast(5)
                totalPointsEarned += alarmEarned
            }
        } else {
            // potential
            maxPossiblePoints += 20
        }

        if (maxPossiblePoints == 0) maxPossiblePoints = 100

        var calculatedScore = ((totalPointsEarned.toFloat() / maxPossiblePoints.toFloat()) * 100).toInt()

        // Penalties
        // Cheating attempt penalty
        calculatedScore -= (cheatingAttempts * 15)

        // Cap score
        calculatedScore = calculatedScore.coerceIn(0, 100)

        // Grow/Shrink tree health & handle levels/xp based on completions
        var xpEarned = logs.size * 10
        if (journal?.completedMorning == true) xpEarned += 15
        if (journal?.completedNight == true) xpEarned += 15
        if (alarmToday?.isSuccessful == true) xpEarned += 30

        var newXp = config.xp + xpEarned
        var newLevel = config.level
        val xpNeededForNextLevel = newLevel * 150
        if (newXp >= xpNeededForNextLevel) {
            newXp -= xpNeededForNextLevel
            newLevel += 1
        }

        // Tree health corresponds to score
        val newTreeHealth = calculatedScore

        // Streaks logic
        var currentStreak = config.currentStreak
        var longestStreak = config.longestStreak

        // Simple daily streak check: if user finished at least 50% of goals today, increment/keep streak
        val finishedRatio = if (maxPossiblePoints > 0) totalPointsEarned.toFloat() / maxPossiblePoints.toFloat() else 0f
        if (finishedRatio >= 0.5f) {
            if (config.lastScoreUpdateDate != date) {
                currentStreak += 1
                if (currentStreak > longestStreak) {
                    longestStreak = currentStreak
                }
            }
        } else if (finishedRatio < 0.2f && config.lastScoreUpdateDate != date && date != getTodayDateString()) {
            // Reset streak if they failed heavily yesterday (not today)
            currentStreak = 0
        }

        val updatedConfig = config.copy(
            disciplineScore = calculatedScore,
            xp = newXp,
            level = newLevel,
            coins = config.coins + (xpEarned / 5), // Coins earned
            treeHealth = newTreeHealth,
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            lastScoreUpdateDate = date
        )

        userConfigDao.insertOrUpdate(updatedConfig)
    }

    fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}
