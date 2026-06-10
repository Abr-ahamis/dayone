package com.dayone.di

import android.content.Context
import androidx.room.Room
import com.dayone.alarm.DayOneAlarmScheduler
import com.dayone.data.DayOneDao
import com.dayone.data.DayOneDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun database(@ApplicationContext context: Context): DayOneDatabase =
        Room.databaseBuilder(context, DayOneDatabase::class.java, "dayone.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun dao(database: DayOneDatabase): DayOneDao = database.dao()

    @Provides
    @Singleton
    fun alarmScheduler(@ApplicationContext context: Context): DayOneAlarmScheduler =
        DayOneAlarmScheduler(context)
}
