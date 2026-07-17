package com.example.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.data.api.GeminiClient
import com.example.data.repository.DisciplineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DisciplineViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = DisciplineRepository(
        db.userConfigDao(),
        db.habitDao(),
        db.habitCompletionLogDao(),
        db.journalEntryDao(),
        db.alarmLogDao()
    )

    // Flow states
    val userConfig: StateFlow<UserConfig?> = repository.userConfig.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val allHabits: StateFlow<List<Habit>> = repository.allHabits.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _todayDate = MutableStateFlow(getTodayDateString())
    val todayDate: StateFlow<String> = _todayDate.asStateFlow()

    val todayCompletions: StateFlow<Set<Int>> = _todayDate.flatMapLatest { date ->
        repository.getLogsForDate(date).map { list -> list.map { it.habitId }.toSet() }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptySet()
    )

    val todayJournal: StateFlow<JournalEntry?> = _todayDate.flatMapLatest { date ->
        repository.getJournalEntryForDate(date)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val allJournalEntries: StateFlow<List<JournalEntry>> = repository.allJournalEntries.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val alarmLogs: StateFlow<List<AlarmLog>> = repository.allAlarmLogs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // UI/In-App Interactive States
    private val _isAlarmRinging = MutableStateFlow(false)
    val isAlarmRinging: StateFlow<Boolean> = _isAlarmRinging.asStateFlow()

    private val _alarmVerificationType = MutableStateFlow("bathroom_photo") // "bathroom_photo", "math", "memory", "typing"
    val alarmVerificationType: StateFlow<String> = _alarmVerificationType.asStateFlow()

    // Focus Mode states
    private val _focusSecondsLeft = MutableStateFlow(25 * 60)
    val focusSecondsLeft: StateFlow<Int> = _focusSecondsLeft.asStateFlow()

    private val _isFocusTimerRunning = MutableStateFlow(false)
    val isFocusTimerRunning: StateFlow<Boolean> = _isFocusTimerRunning.asStateFlow()

    private val _focusCompletedSessions = MutableStateFlow(0)
    val focusCompletedSessions: StateFlow<Int> = _focusCompletedSessions.asStateFlow()

    private val _phoneUnlockCount = MutableStateFlow(0)
    val phoneUnlockCount: StateFlow<Int> = _phoneUnlockCount.asStateFlow()

    // AI Coach feedback text
    private val _aiCoachFeedback = MutableStateFlow<String>("Your Discipline OS Coach is analyzing your dashboard. Run review to hear your evaluation.")
    val aiCoachFeedback: StateFlow<String> = _aiCoachFeedback.asStateFlow()

    private val _isCoachLoading = MutableStateFlow(false)
    val isCoachLoading: StateFlow<Boolean> = _isCoachLoading.asStateFlow()

    private val _motivationalQuote = MutableStateFlow("Excellence is not an act, but a habit.")
    val motivationalQuote: StateFlow<String> = _motivationalQuote.asStateFlow()

    // Timer Job
    private var focusTimerJob: Job? = null
    private var alarmTriggerTime: Long = 0

    init {
        viewModelScope.launch {
            // Check if user has completed onboarding, else pre-populate defaults
            repository.userConfig.firstOrNull()?.let { config ->
                if (config == null) {
                    // Pre-populate some defaults
                }
            }
            
            // Loop to check and update date at midnight
            launch {
                while (true) {
                    delay(30000)
                    val current = getTodayDateString()
                    if (current != _todayDate.value) {
                        _todayDate.value = current
                    }
                }
            }

            // Load a tailored coaching quote on startup
            launch {
                delay(1500)
                refreshMotivationalQuote()
            }
        }
    }

    fun completeOnboarding(
        name: String,
        wakeUpTime: String,
        sleepTime: String,
        biggestGoal: String,
        challengeType: String,
        badHabits: List<String>,
        exerciseTarget: Int,
        readingTarget: Int,
        reminderTimes: String
    ) {
        viewModelScope.launch {
            repository.prePopulateDefaultHabits()
            val newConfig = UserConfig(
                name = name,
                wakeUpTime = wakeUpTime,
                sleepTime = sleepTime,
                biggestGoal = biggestGoal,
                challengeType = challengeType,
                badHabits = badHabits.joinToString(","),
                dailyExerciseTarget = exerciseTarget,
                readingTarget = readingTarget,
                reminderTimes = reminderTimes,
                onboardingCompleted = true,
                disciplineScore = 50,
                xp = 0,
                level = 1,
                coins = 100,
                currentStreak = 1,
                longestStreak = 1,
                treeHealth = 50,
                lastScoreUpdateDate = getTodayDateString()
            )
            repository.saveUserConfig(newConfig)
            // Save initial empty journal for today to make it interactive
            val initialJournal = JournalEntry(
                date = getTodayDateString(),
                mood = 3,
                completedMorning = false,
                completedNight = false
            )
            repository.saveJournalEntry(initialJournal)
            refreshMotivationalQuote()
        }
    }

    // Habits actions
    fun toggleHabit(habitId: Int) {
        viewModelScope.launch {
            val date = _todayDate.value
            val currentLogs = todayCompletions.value
            if (currentLogs.contains(habitId)) {
                repository.removeHabitCompletion(habitId, date)
            } else {
                repository.logHabitCompletion(habitId, date)
            }
        }
    }

    fun addCustomHabit(name: String, icon: String, reminder: String, category: String, difficulty: String, target: Int) {
        viewModelScope.launch {
            val h = Habit(
                name = name,
                iconName = icon,
                reminderTime = reminder,
                category = category,
                difficulty = difficulty,
                target = target,
                frequency = "Daily"
            )
            repository.insertHabit(h)
            repository.recalculateDisciplineScore(_todayDate.value)
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
            repository.recalculateDisciplineScore(_todayDate.value)
        }
    }

    fun incrementUnlockCount() {
        _phoneUnlockCount.value += 1
    }

    // Journal actions
    fun saveMorningJournal(gratitude: String, plan: String, mood: Int) {
        viewModelScope.launch {
            val date = _todayDate.value
            val existing = repository.getJournalEntryForDate(date).firstOrNull() ?: JournalEntry(date = date)
            val updated = existing.copy(
                morningGratitude = gratitude,
                morningPlan = plan,
                mood = mood,
                completedMorning = true
            )
            repository.saveJournalEntry(updated)
        }
    }

    fun saveNightJournal(reflection: String, win: String, mistake: String, tomorrowPlan: String, mood: Int) {
        viewModelScope.launch {
            val date = _todayDate.value
            val existing = repository.getJournalEntryForDate(date).firstOrNull() ?: JournalEntry(date = date)
            val updated = existing.copy(
                nightReflection = reflection,
                biggestWin = win,
                biggestMistake = mistake,
                tomorrowPlan = tomorrowPlan,
                mood = mood,
                completedNight = true
            )
            repository.saveJournalEntry(updated)
        }
    }

    // Alarm triggers and actions
    fun triggerInAppAlarm(verificationType: String = "bathroom_photo") {
        _alarmVerificationType.value = verificationType
        _isAlarmRinging.value = true
        alarmTriggerTime = System.currentTimeMillis()
    }

    fun verifyAndStopAlarm(bitmap: Bitmap?, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val type = _alarmVerificationType.value
            if (type == "bathroom_photo") {
                if (bitmap == null) {
                    onResult(false, "Photo is empty. Capturing is required.")
                    return@launch
                }
                _isCoachLoading.value = true
                val isBathroom = GeminiClient.verifyBathroomPhoto(bitmap)
                _isCoachLoading.value = false
                if (isBathroom) {
                    completeAlarmSuccess(type, bitmap)
                    onResult(true, "Bathroom verification success. Morning mission accomplished.")
                } else {
                    // Penalty for cheat attempt
                    logCheatingAttempt()
                    onResult(false, "Verification failed. Image does not appear to be a bathroom sink or tiles. Strict mode is active. Retake the photo.")
                }
            } else {
                // Non-photo verifications
                completeAlarmSuccess(type, null)
                onResult(true, "Verification successful. Alarm deactivated.")
            }
        }
    }

    private suspend fun completeAlarmSuccess(verificationType: String, bitmap: Bitmap?) {
        _isAlarmRinging.value = false
        val delay = ((System.currentTimeMillis() - alarmTriggerTime) / 1000).toInt()
        val log = AlarmLog(
            date = _todayDate.value,
            scheduledTime = userConfig.value?.wakeUpTime ?: "05:00",
            wakeUpTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            verificationTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
            verificationType = verificationType,
            responseDelaySeconds = delay,
            isSuccessful = true,
            photoPath = if (bitmap != null) "SAVED_LOCAL_PHOTO" else null
        )
        repository.logAlarmEvent(log)
    }

    private fun logCheatingAttempt() {
        viewModelScope.launch {
            // Log a completion but mark as cheating so the algorithm reduces points
            val randomDefaultHabit = allHabits.value.firstOrNull { it.isChallengeDefault }
            if (randomDefaultHabit != null) {
                repository.logHabitCompletion(randomDefaultHabit.id, _todayDate.value, "Failed bathroom verification bypass check", true)
            }
        }
    }

    // Focus mode timer
    fun startFocusSession(minutes: Int = 25) {
        _focusSecondsLeft.value = minutes * 60
        _isFocusTimerRunning.value = true
        focusTimerJob?.cancel()
        focusTimerJob = viewModelScope.launch {
            while (_focusSecondsLeft.value > 0) {
                delay(1000)
                _focusSecondsLeft.value -= 1
            }
            _isFocusTimerRunning.value = false
            _focusCompletedSessions.value += 1
            // Reward focus completion
            userConfig.value?.let { config ->
                val updated = config.copy(
                    xp = config.xp + 25,
                    coins = config.coins + 10
                )
                repository.saveUserConfig(updated)
            }
        }
    }

    fun stopFocusSession() {
        _isFocusTimerRunning.value = false
        focusTimerJob?.cancel()
    }

    fun resetFocusSession() {
        stopFocusSession()
        _focusSecondsLeft.value = 25 * 60
    }

    // AI Coach operations
    fun refreshMotivationalQuote() {
        viewModelScope.launch {
            val goal = userConfig.value?.biggestGoal ?: "Excellence"
            val habitsCount = allHabits.value.size
            val finishedCount = todayCompletions.value.size
            val summary = "Completed $finishedCount of $habitsCount tasks today."
            val q = GeminiClient.generateCoachingQuote(goal, summary)
            _motivationalQuote.value = q
        }
    }

    fun runCoachReview() {
        viewModelScope.launch {
            _isCoachLoading.value = true
            val config = userConfig.value
            val name = config?.name ?: "Discipline Warrior"
            val goal = config?.biggestGoal ?: "Build strict mental toughness"
            
            val habitsList = allHabits.value
            val completions = todayCompletions.value
            val habitsCompletedText = habitsList.joinToString("\n") { h ->
                "- ${h.name} (${h.difficulty}): " + if (completions.contains(h.id)) "COMPLETED" else "PENDING"
            }

            val journal = todayJournal.value
            val journalText = if (journal != null) {
                """
                Morning gratitude: ${journal.morningGratitude}
                Morning plan: ${journal.morningPlan}
                Night reflection: ${journal.nightReflection}
                Biggest Win: ${journal.biggestWin}
                Biggest Mistake: ${journal.biggestMistake}
                Tomorrow's Plan: ${journal.tomorrowPlan}
                Mood rating: ${journal.mood}/5
                """.trimIndent()
            } else {
                "No journal recorded for today yet."
            }

            val scoreHistoryText = "Current score: ${config?.disciplineScore ?: 50}/100. Tree health: ${config?.treeHealth ?: 50}%. Current Streak: ${config?.currentStreak ?: 0} days."

            val feedback = GeminiClient.getCoachFeedback(
                name = name,
                goal = goal,
                habitsCompleted = habitsCompletedText,
                journalEntries = journalText,
                scoreHistory = scoreHistoryText
            )
            _aiCoachFeedback.value = feedback
            _isCoachLoading.value = false
        }
    }

    fun purchaseTreeHealer() {
        viewModelScope.launch {
            val config = userConfig.value ?: return@launch
            if (config.coins >= 50) {
                val updated = config.copy(
                    coins = config.coins - 50,
                    treeHealth = (config.treeHealth + 15).coerceAtMost(100)
                )
                repository.saveUserConfig(updated)
            }
        }
    }

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}
