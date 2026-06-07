package com.dayone.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter fun toDayState(value: String): DayState = DayState.valueOf(value)
    @TypeConverter fun fromDayState(value: DayState): String = value.name
}
