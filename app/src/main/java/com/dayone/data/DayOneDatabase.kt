package com.dayone.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [StreakStateEntity::class, CalendarEntryEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DayOneDatabase : RoomDatabase() {
    abstract fun dao(): DayOneDao
}
