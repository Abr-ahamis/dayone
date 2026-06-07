package com.dayone.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dayone.data.CalendarEntryEntity
import com.dayone.data.DayState
import com.dayone.data.StreakStateEntity
import com.dayone.domain.StreakColorEngine
import com.dayone.domain.epochDayToDate
import com.dayone.domain.formatMonthTitle
import com.dayone.domain.formatShort
import com.dayone.domain.todayEpochDay
import java.time.LocalDate
import java.time.YearMonth

private val BgDeep = Color(0xFF060B18)
private val BgBase = Color(0xFF0A1128)
private val BgSurface = Color(0xFF0F1A36)
private val BgElevated = Color(0xFF162040)
private val Accent = Color(0xFF4F8EF7)
private val AccentCyan = Color(0xFF38D9D9)
private val AccentGreen = Color(0xFF2DD87B)
private val AccentDanger = Color(0xFFFF4F6D)
private val AccentWarn = Color(0xFFFFB547)
private val TextPrimary = Color(0xFFF0F4FF)
private val TextSecondary = Color(0xFF8AA0CC)
private val TextMuted = Color(0xFF4A5980)
private val Border = Color(0x1F4F8EF7)
private val DisplayFont = FontFamily.SansSerif
private val MonoFont = FontFamily.Monospace

private enum class Tab(val label: String, val icon: String) {
    Home("Home", "◉"),
    Dashboard("Dash", "▦"),
    History("History", "□"),
    Settings("Settings", "⚙")
}

@Composable
fun DayOneApp(viewModel: DayOneViewModel = hiltViewModel()) {
    val ui by viewModel.uiState.collectAsState()
    val colors = androidx.compose.material3.darkColorScheme(
        background = BgDeep,
        surface = BgSurface,
        primary = Accent,
        onPrimary = BgDeep,
        onBackground = TextPrimary,
        onSurface = TextPrimary
    )
    MaterialTheme(colorScheme = colors) {
        Surface(Modifier.fillMaxSize(), color = BgDeep) {
            if (ui.loading || ui.state == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Accent)
                }
            } else {
                MainShell(ui, viewModel)
            }
        }
    }
}

@Composable
private fun MainShell(ui: DayOneUiState, viewModel: DayOneViewModel) {
    var tab by remember { mutableStateOf(Tab.Home) }
    Box(Modifier.fillMaxSize()) {
        MeshBackground()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 108.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                when (tab) {
                    Tab.Home -> HomeScreen(ui.state!!, ui.verse, viewModel::completeToday, viewModel::resetStreak)
                    Tab.Dashboard -> DashboardScreen(ui.state!!, ui.history)
                    Tab.History -> HistoryScreen(ui.state!!, ui.history)
                    Tab.Settings -> SettingsScreen(ui, viewModel)
                }
            }
        }
        BottomTabs(tab, onTab = { tab = it }, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun MeshBackground() {
    Canvas(Modifier.fillMaxSize().background(BgDeep)) {
        drawCircle(Color(0x2E4F8EF7), radius = size.maxDimension * 0.45f, center = Offset(size.width * 0.18f, size.height * 0.08f))
        drawCircle(Color(0x247B6FF0), radius = size.maxDimension * 0.36f, center = Offset(size.width * 0.88f, size.height * 0.82f))
        drawCircle(Color(0x1438D9D9), radius = size.maxDimension * 0.28f, center = Offset(size.width * 0.52f, size.height * 0.48f))
    }
}

@Composable
private fun HomeScreen(state: StreakStateEntity, verse: String, onComplete: () -> Unit, onReset: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(22.dp)) {
        HeaderRow(state.habitName, state.emoji)
        StreakRing(state)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Congrats, you're doing great", color = TextPrimary, fontWeight = FontWeight.Medium, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Text(verse, color = TextSecondary, fontSize = 14.sp, textAlign = TextAlign.Center, lineHeight = 20.sp)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PrimaryAction("Complete Today", AccentGreen, Modifier.weight(1f), onComplete)
            PrimaryAction("Reset", AccentDanger, Modifier.weight(1f), onReset)
        }
        GlassCard {
            Text("Next milestone", color = TextMuted, fontSize = 11.sp, letterSpacing = 1.sp)
            Text("${nextMilestone(state.currentStreak)} days", color = Accent, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text("Green recovery begins at day ${StreakColorEngine.greenThreshold(state.streakBreaks)}", color = TextSecondary, fontSize = 13.sp)
        }
    }
}

@Composable
private fun HeaderRow(name: String, emoji: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(10.dp).clip(CircleShape).background(AccentGreen))
            Spacer(Modifier.width(10.dp))
            Text(name, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        }
        Text(emoji, fontSize = 28.sp)
    }
}

@Composable
private fun StreakRing(state: StreakStateEntity) {
    val color = StreakColorEngine.composeColor(state.currentStreak, state.streakBreaks)
    val progress by animateFloatAsState((state.currentStreak % nextMilestone(state.currentStreak)).toFloat() / nextMilestone(state.currentStreak), label = "ring")
    Box(Modifier.fillMaxWidth().aspectRatio(1f), contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(280.dp)) {
            drawCircle(BgSurface, style = Stroke(18.dp.toPx(), cap = StrokeCap.Round))
            drawArc(color, -90f, 360f * progress.coerceAtLeast(0.08f), false, style = Stroke(18.dp.toPx(), cap = StrokeCap.Round))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(state.currentStreak.toString(), color = TextPrimary, fontSize = 94.sp, fontWeight = FontWeight.Bold, fontFamily = DisplayFont)
            Text("DAYS", color = TextSecondary, fontSize = 13.sp, letterSpacing = 2.sp, fontFamily = MonoFont)
            Spacer(Modifier.height(8.dp))
            Text(StreakColorEngine.icon(state.currentStreak), fontSize = 28.sp)
        }
    }
}

@Composable
private fun DashboardScreen(state: StreakStateEntity, history: List<CalendarEntryEntity>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ScreenTitle("Dashboard", "Your progress at a glance")
        ContributionGrid(history, weeks = 12)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Total Days", state.totalCompleted.toString(), AccentCyan, Modifier.weight(1f))
            StatCard("Longest", state.longestStreak.toString(), Accent, Modifier.weight(1f))
            val rate = successRate(history)
            StatCard("Success %", "$rate%", AccentWarn, Modifier.weight(1f))
        }
        HabitCard(state)
    }
}

@Composable
private fun HistoryScreen(state: StreakStateEntity, history: List<CalendarEntryEntity>) {
    val today = LocalDate.now()
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ScreenTitle("History", "Review your journey")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Current", state.currentStreak.toString(), AccentGreen, Modifier.weight(1f))
            StatCard("Best", state.longestStreak.toString(), Accent, Modifier.weight(1f))
            StatCard("Breaks", state.streakBreaks.toString(), AccentDanger, Modifier.weight(1f))
        }
        MonthCalendar(today.year, today.monthValue, history, state.streakBreaks)
        GlassCard {
            Text("52-Week Activity", color = TextPrimary, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(12.dp))
            ContributionGrid(history, weeks = 26)
        }
    }
}

@Composable
private fun SettingsScreen(ui: DayOneUiState, viewModel: DayOneViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ScreenTitle("Settings", "Preferences & data")
        SettingsRow("Default Habit", ui.state!!.habitName, "🏠")
        ToggleRow("Notifications", "Daily reminders & milestones", "🔔", ui.preferences.notificationsEnabled, viewModel::setNotifications)
        ToggleRow("Theme", if (ui.preferences.darkTheme) "Dark" else "Light", "🎨", ui.preferences.darkTheme, viewModel::setDarkTheme)
        GlassCard {
            Text("DayOne", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text("Version 1.0.0 · Android native MVVM", color = TextSecondary, fontSize = 13.sp)
        }
        PrimaryAction("Clear All Data", AccentDanger, Modifier.fillMaxWidth(), viewModel::clearAll)
    }
}

@Composable
private fun BottomTabs(current: Tab, onTab: (Tab) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier.fillMaxWidth().navigationBarsPadding().background(BgSurface.copy(alpha = 0.92f)).border(1.dp, Border).padding(horizontal = 18.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Tab.entries.forEach { tab ->
            val active = tab == current
            Column(
                Modifier.clip(RoundedCornerShape(999.dp)).clickable { onTab(tab) }.background(if (active) Color(0x1F4F8EF7) else Color.Transparent).padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(tab.icon, color = if (active) Accent else TextMuted, fontSize = 18.sp)
                Text(tab.label.uppercase(), color = if (active) Accent else TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun ScreenTitle(title: String, subtitle: String) {
    Column {
        Text(title, color = TextPrimary, fontSize = 36.sp, fontWeight = FontWeight.Bold, fontFamily = DisplayFont)
        Text(subtitle, color = TextSecondary, fontSize = 14.sp)
    }
}

@Composable
private fun GlassCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(BgSurface.copy(alpha = 0.82f)).border(1.dp, Border, RoundedCornerShape(24.dp)).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        content = content
    )
}

@Composable
private fun StatCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(modifier.clip(RoundedCornerShape(18.dp)).background(BgSurface).border(1.dp, Border, RoundedCornerShape(18.dp)).padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(label, color = TextMuted, fontSize = 10.sp, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun HabitCard(state: StreakStateEntity) {
    val color = StreakColorEngine.composeColor(state.currentStreak, state.streakBreaks)
    GlassCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(state.emoji, fontSize = 34.sp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(state.habitName, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                Text("Count Up · Since ${formatShort(state.startEpochDay)}", color = TextSecondary, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(state.currentStreak.toString(), color = color, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text("DAYS", color = TextMuted, fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun ContributionGrid(history: List<CalendarEntryEntity>, weeks: Int) {
    val byDay = history.associateBy { it.epochDay }
    val today = todayEpochDay()
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
        repeat(weeks) { week ->
            Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.weight(1f)) {
                repeat(7) { day ->
                    val epoch = today - ((weeks - 1 - week) * 7 + (6 - day))
                    val entry = byDay[epoch]
                    val color = when (entry?.state) {
                        DayState.COMPLETED -> StreakColorEngine.composeColor(entry.streakValue, 0)
                        DayState.MISSED -> AccentDanger.copy(alpha = 0.55f)
                        else -> BgElevated
                    }
                    Box(Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(3.dp)).background(color))
                }
            }
        }
    }
}

@Composable
private fun MonthCalendar(year: Int, month: Int, history: List<CalendarEntryEntity>, breaks: Int) {
    val byDay = history.associateBy { it.epochDay }
    val ym = YearMonth.of(year, month)
    val firstDow = ym.atDay(1).dayOfWeek.value % 7
    GlassCard {
        Text(formatMonthTitle(year, month), color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa").forEach { Text(it, color = TextMuted, fontSize = 11.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center) }
        }
        val cells = firstDow + ym.lengthOfMonth()
        repeat((cells + 6) / 7) { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(7) { col ->
                    val index = row * 7 + col
                    val day = index - firstDow + 1
                    if (day !in 1..ym.lengthOfMonth()) {
                        Spacer(Modifier.weight(1f).aspectRatio(1f))
                    } else {
                        val epoch = ym.atDay(day).toEpochDay()
                        val entry = byDay[epoch]
                        val color = when (entry?.state) {
                            DayState.COMPLETED -> StreakColorEngine.composeColor(entry.streakValue, breaks).copy(alpha = 0.85f)
                            DayState.MISSED -> AccentDanger.copy(alpha = 0.45f)
                            else -> BgElevated
                        }
                        Box(Modifier.weight(1f).aspectRatio(1f).clip(RoundedCornerShape(10.dp)).background(color), contentAlignment = Alignment.Center) {
                            Text(day.toString(), color = TextPrimary, fontSize = 12.sp, fontFamily = MonoFont)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsRow(title: String, subtitle: String, icon: String) {
    GlassCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 24.sp)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                Text(subtitle, color = TextSecondary, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun ToggleRow(title: String, subtitle: String, icon: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    GlassCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 24.sp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                Text(subtitle, color = TextSecondary, fontSize = 12.sp)
            }
            Switch(checked = checked, onCheckedChange = onChange)
        }
    }
}

@Composable
private fun PrimaryAction(text: String, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = modifier.height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = BgDeep), shape = RoundedCornerShape(16.dp)) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}

private fun nextMilestone(days: Int): Int =
    listOf(1, 3, 7, 14, 21, 30, 60, 90, 100, 180, 365).firstOrNull { it > days } ?: 1000

private fun successRate(history: List<CalendarEntryEntity>): Int {
    val counted = history.filter { it.state == DayState.COMPLETED || it.state == DayState.MISSED }
    if (counted.isEmpty()) return 100
    return (counted.count { it.state == DayState.COMPLETED } * 100f / counted.size).toInt()
}
