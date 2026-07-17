package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserConfigDao {
    @Query("SELECT * FROM user_config WHERE id = 1")
    fun getUserConfig(): Flow<UserConfig?>

    @Query("SELECT * FROM user_config WHERE id = 1")
    suspend fun getUserConfigDirect(): UserConfig?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(config: UserConfig)
}

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE isArchived = 0")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :id")
    fun getHabitById(id: Int): Flow<Habit?>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitByIdDirect(id: Int): Habit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)
}

@Dao
interface HabitCompletionLogDao {
    @Query("SELECT * FROM habit_completion_logs WHERE date = :date")
    fun getLogsForDate(date: String): Flow<List<HabitCompletionLog>>

    @Query("SELECT * FROM habit_completion_logs")
    fun getAllLogs(): Flow<List<HabitCompletionLog>>

    @Query("SELECT * FROM habit_completion_logs WHERE habitId = :habitId")
    fun getLogsForHabit(habitId: Int): Flow<List<HabitCompletionLog>>

    @Query("SELECT * FROM habit_completion_logs WHERE habitId = :habitId AND date = :date LIMIT 1")
    suspend fun getLogForHabitAndDateDirect(habitId: Int, date: String): HabitCompletionLog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HabitCompletionLog)

    @Query("DELETE FROM habit_completion_logs WHERE habitId = :habitId AND date = :date")
    suspend fun deleteLog(habitId: Int, date: String)
}

@Dao
interface JournalEntryDao {
    @Query("SELECT * FROM journal_entries WHERE date = :date")
    fun getEntryByDate(date: String): Flow<JournalEntry?>

    @Query("SELECT * FROM journal_entries WHERE date = :date")
    suspend fun getEntryByDateDirect(date: String): JournalEntry?

    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<JournalEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntry)
}

@Dao
interface AlarmLogDao {
    @Query("SELECT * FROM alarm_logs ORDER BY date DESC")
    fun getAllAlarmLogs(): Flow<List<AlarmLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarmLog(log: AlarmLog)
}
