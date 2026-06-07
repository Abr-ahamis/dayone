package com.dayone.domain

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun todayEpochDay(): Long = LocalDate.now().toEpochDay()
fun epochDayToDate(epochDay: Long): LocalDate = LocalDate.ofEpochDay(epochDay)
fun nowMillis(): Long = Instant.now().toEpochMilli()

fun formatShort(epochDay: Long): String =
    epochDayToDate(epochDay).format(DateTimeFormatter.ofPattern("EEE · MMM d", Locale.US))

fun formatMonthTitle(year: Int, month: Int): String =
    LocalDate.of(year, month, 1).format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US))

fun millisToEpochDay(millis: Long): Long =
    Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate().toEpochDay()
