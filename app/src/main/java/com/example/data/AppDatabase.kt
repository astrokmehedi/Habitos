package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserConfig::class,
        Habit::class,
        HabitCompletionLog::class,
        JournalEntry::class,
        AlarmLog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userConfigDao(): UserConfigDao
    abstract fun habitDao(): HabitDao
    abstract fun habitCompletionLogDao(): HabitCompletionLogDao
    abstract fun journalEntryDao(): JournalEntryDao
    abstract fun alarmLogDao(): AlarmLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "discipline_os_database"
                )
                .fallbackToDestructiveMigration() // simple fallback for schema changes during development
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
