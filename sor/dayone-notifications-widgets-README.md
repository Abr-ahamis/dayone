# DAYONE ANDROID — NOTIFICATIONS & WIDGETS IMPLEMENTATION PROMPT

> Focused build prompt extracted from `dayone-notification.html` and `dayone-widgets.html`.
> Use this prompt alone when the base app already exists and you are adding the notification system and home-screen widgets.

---

## CONTEXT

The DayOne Android app is already built. This prompt covers **two remaining systems only**:

1. The **Notification System** — banner, expanded card, actions, and channels
2. The **Home Screen Widget System** — Compact widget and Expanded widget

Every pixel, color, animation, and behavior described below is extracted directly from the HTML prototypes. Do not invent anything. Do not simplify. Recreate what is described exactly.

---

## DESIGN SYSTEM (apply to both systems)

```
Background Deep:    #060b18
Background Base:    #0a1128
Background Surface: #0f1a36
Background Elevated:#162040
Glass overlay:      rgba(15, 26, 54, 0.72–0.92)

Accent Blue:        #4f8ef7    Glow: rgba(79,142,247,0.35)
Accent Cyan:        #38d9d9    Glow: rgba(56,217,217,0.3)
Accent Purple:      #7b6ff0
Accent Green:       #2dd87b
Warning:            #ffb547
Danger/Error:       #ff4f6d

Text Primary:       #f0f4ff
Text Secondary:     #8aa0cc
Text Muted:         #4a5980

Border Subtle:      rgba(79,142,247,0.12)
Border Glass:       rgba(255,255,255,0.10)

Display Font:       Bebas Neue
Body Font:          DM Sans  (weights 300 / 400 / 500 / 600)
Mono Font:          DM Mono  (weights 400 / 500)

Border Radius Scale:
  sm   =  8dp
  md   = 16dp
  lg   = 24dp
  xl   = 32–40dp
  full = 9999dp (pill)
```

---

## STREAK COLOR ENGINE

Both the widget background and the calendar cells are colored by streak count.
Implement this as a single class:

**File:** `app/src/main/java/com/dayone/engine/StreakColorEngine.kt`

```
Color map (streak days → hex):
  0  → #ff1744   (bright red)
  1  → #e53935   (deep red)
  2  → #ef5350   (light red)
  3  → #f4511e   (orange-red)
  4  → #f4511e   (orange-red)
  5  → #fb8c00   (orange)
  6  → #ffa726   (orange)
  7  → #ffa726   (orange)
  8  → #c0ca33   (yellow-green)
  9  → #c0ca33   (yellow-green)
  10 → #aed131   (yellow-green)
  11 → #43a047   (green)
  12 → #2dd87b   (strong green)
  13+→ #00c853   (rich green, progressively richer)
```

**Green threshold rule:**
- Base green threshold = day 13
- Every time the user breaks a streak: `greenThreshold = 13 + totalStreakBreaks`
- Recovery path grows by one warm step per break
- Recovery steps are capped at `MAX_RECOVERY_STEPS` to avoid demotivation
- Green tier always stays exactly 3 levels deep

**Methods to implement:**
```kotlin
fun getStreakColor(currentStreak: Int, streakBreaks: Int = 0): Int
fun getDayColor(streak: Int, isCompleted: Boolean, isMissed: Boolean): Int
fun buildRecoveryPath(streakBreaks: Int): List<Int>  // ordered color list for recovery ladder
```

Widget background color = `getStreakColor(currentStreak)` as a gradient fill.
Calendar day cell color = `getDayColor(entry.streak, entry.isCompleted, entry.isMissed)`.

---

## PART 1 — NOTIFICATION SYSTEM

### Notification Channels

Create three Android notification channels:

| Channel ID | Name | Description | Importance |
|---|---|---|---|
| `dayone_daily` | Daily Reminder | Habit daily check-in | DEFAULT |
| `dayone_milestone` | Milestones | Streak achievements | HIGH |
| `dayone_weekly` | Weekly Summary | Weekly progress report | DEFAULT |

Register all channels in `NotificationChannelManager.kt` called from `Application.onCreate()`.

---

### Compact Banner Notification

This is the standard Android notification that appears in the status bar and as a heads-up banner.

**Visual specs (match the HTML exactly):**

```
Container:
  Background:       rgba(22, 32, 64, 0.82) with backdrop blur
  Border-radius:    28dp
  Border:           1dp rgba(255,255,255,0.10)
  Shadow:           8dp blur, rgba(0,0,0,0.5) + 2dp rgba(0,0,0,0.3)
  Padding:          12dp 16dp
  Layout:           horizontal, items centered, gap 12dp

App Icon:
  Size:             38×38dp
  Border-radius:    10dp
  Background:       gradient #1a2d60 → #2a3d80 (135°)
  Border:           1dp rgba(79,142,247,0.30)
  Shadow:           0 2dp 8dp rgba(79,142,247,0.20)
  Content:          Habit emoji (e.g. 🔥), font-size 20sp

Text block (flex 1):
  App name row:     11sp / weight 600 / color #4a5980 / uppercase / tracking 0.04em / mb 2dp
  Title row:        13sp / weight 600 / color #f0f4ff / single line ellipsis
  Body row:         12sp / weight 400 / color #8aa0cc / single line ellipsis

Chevron:
  Icon:             chevron-down SVG, 14×14dp, stroke #f0f4ff, opacity 0.35

Swipe hint:
  Text:             "↑ swipe to dismiss"
  Position:         absolute top 6dp, centered
  Font:             10sp / uppercase / color rgba(255,255,255,0.20)
  Visible:          only after banner is fully shown
```

**Android implementation:**
- Use `NotificationCompat.Builder` with `setCustomContentView` and `setCustomBigContentView`
- Custom layout XML: `res/layout/notification_banner.xml`
- Slide-in animation: enter from top, spring easing (`cubic-bezier(0.16, 1, 0.3, 1)`)
- The banner enters with a 0.6s delay after the trigger fires
- Dynamic Island equivalent: animate the status bar notification icon briefly on entry

**Swipe to dismiss:**
- User can swipe up on the banner to dismiss it
- If swipe distance > 40dp upward: dismiss with fade + slide-up animation
- If swipe released before 40dp: spring back to position
- Opacity while dragging: `max(0, 1 + deltaY / 80)` (fades as it moves up)

**Tap behavior:**
- Tapping the banner expands it to the full notification card
- `PendingIntent` pointing to `MainActivity` with the habit ID as an extra

---

### Expanded Full Notification Card

When the user taps the compact banner, it transitions to this full card.

**Transition animation:**
- Banner fades out + scales down (0.3s, ease-out)
- Expanded card slides up from center + scales from 0.88 to 1.0 (0.4s, spring)
- Background behind the card blurs: `rgba(6,11,24,0.6)` + blur(4px)
- Dismiss expanded → re-show banner with spring bounce

**Expanded card visual specs:**

```
Container:
  Width:            340dp (centered)
  Background:       rgba(14, 22, 50, 0.92) + backdrop blur 60dp
  Border-radius:    34dp
  Border:           1dp rgba(255,255,255,0.10)
  Shadow:           20dp card shadow + 0 0 80dp rgba(79,142,247,0.08)
  Overflow:         hidden

Top stripe:
  Height:           3dp
  Background:       gradient left-to-right: #4f8ef7 → #38d9d9 → #7b6ff0
  Opacity:          0.70

Handle bar:
  Width:            36dp / Height: 4dp
  Color:            rgba(255,255,255,0.12)
  Border-radius:    pill
  Centered, padding 12dp top 6dp bottom

Header section (padding 16dp 18dp 12dp):
  App icon:         44×44dp, border-radius 12dp, same gradient as banner icon
                    Shadow: 0 4dp 16dp rgba(79,142,247,0.25), emoji 24sp
  App name:         11sp / weight 600 / #4a5980 / uppercase / mb 2dp
  Notification title: 16sp / weight 700 / #f0f4ff / tracking -0.3px
  Sub-line:         13sp / #8aa0cc / mt 2dp  (e.g. "No Sugar · Day 12")
  Timestamp:        11sp / #4a5980 / right-aligned (e.g. "now")

Divider:           1dp / color rgba(79,142,247,0.12) / margin 0 18dp

Stats grid (margin 12dp 18dp):
  Layout:           3 columns equal width
  Gap:              1dp (border-style separator)
  Background:       rgba(79,142,247,0.12) (shows as separator lines)
  Border-radius:    18dp, overflow hidden
  Border:           1dp rgba(79,142,247,0.12)

  Each stat cell:
    Background:     rgba(15,26,54,0.60)
    Padding:        14dp 10dp
    Alignment:      center column
    Value text:     22sp / weight 700 / DM Mono / tracking -0.5px / line-height 1
      · Default color: #4f8ef7
      · danger state: #ff4f6d
      · success state: #2dd87b
    Label text:     10sp / weight 500 / #4a5980 / uppercase / tracking 0.06em
  First cell corners: 17dp 0 0 17dp
  Last cell corners:  0 17dp 17dp 0

  Three stats shown:
    Left:   Current streak (danger color when 0)
    Center: Best streak (accent blue)
    Right:  Goal days (success green)

Progress bar (padding 0 18dp 14dp):
  Label row:        flex, space-between
    Left label:     11sp / weight 500 / #8aa0cc — e.g. "Progress to Green milestone"
    Right label:    11sp / weight 600 / #2dd87b — e.g. "Day 20"
  Track:            height 5dp, background rgba(255,255,255,0.06), radius pill
  Fill:
    Height:         5dp, radius pill
    Background:     gradient left-to-right #4f8ef7 → #38d9d9
    Shadow:         0 0 10dp rgba(56,217,217,0.40)
    Entry animation: width goes from 0 to target% over 1s spring (delay 0.5s)
    End cap dot:    10×10dp circle, color #38d9d9, border 2dp rgba(10,17,40,0.9)
                    glow: 0 0 8dp #38d9d9

Message block (margin 0 18dp 14dp):
  Background:       rgba(255,79,109,0.08)
  Border:           1dp rgba(255,79,109,0.20)
  Border-radius:    14dp
  Padding:          12dp 14dp
  Layout:           row, gap 10dp
  Icon:             emoji, 20sp, top-aligned
  Text:             12sp / line-height 1.5 / color #8aa0cc
    Bold part:      weight 600 / color #f0f4ff

Action buttons (padding 0 18dp 18dp):
  Layout:           column, gap 8dp

  Primary button ("🗓 Log Activity"):
    Full width, padding 13dp 16dp, border-radius 16dp
    Background:     #4f8ef7
    Color:          white / 14sp / weight 600
    Shadow:         0 4dp 20dp rgba(79,142,247,0.35)
    Hover shadow:   0 4dp 24dp rgba(79,142,247,0.50)
    Press:          scale(0.97)

  Second row (flex, gap 8dp):
    Secondary ("View Details"):
      flex 1, same height
      Background:   rgba(255,255,255,0.07)
      Border:       1dp rgba(255,255,255,0.10)
      Color:        #f0f4ff / 14sp / weight 600
      Hover:        rgba(255,255,255,0.11)

    Danger ("Close"):
      flex 1, same height
      Background:   rgba(255,79,109,0.10)
      Border:       1dp rgba(255,79,109,0.25)
      Color:        #ff4f6d / 14sp / weight 600
      Hover:        rgba(255,79,109,0.18)

  All buttons:
    Active ripple:  rgba(255,255,255,0.08) overlay
    Active scale:   0.97
    Transition:     200ms ease
    Letter-spacing: -0.2px
    Gap (icon+text):6dp
```

**Android implementation files:**
```
NotificationService.kt
NotificationChannelManager.kt
res/layout/notification_expanded.xml
res/layout/notification_banner.xml
```

**Notification types to trigger:**

| Type | Trigger | Title | Body |
|---|---|---|---|
| Streak Update | Streak broken | "Streak Update" | "You broke your streak today" |
| Milestone | Day 1/3/7/14/21/30/60/90/100/180/365 | "Milestone Reached 🎉" | "Day {N} — {habit name}" |
| Daily Reminder | Scheduled time | "Check-in Time" | "{habit emoji} Don't break your streak" |
| Weekly Summary | Sunday evening | "Weekly Report" | "{N} days completed this week" |

---

### Notification Scheduling

**File:** `NotificationScheduler.kt` using WorkManager

```
Daily reminder:
  Type:              PeriodicWorkRequest, 24h interval
  Default time:      21:00 local time
  User-configurable: yes, via Settings screen
  Cancel if:         notifications toggle is OFF

Milestone notifications:
  Type:              one-shot, triggered immediately when streak hits milestone day
  Milestone days:    1, 3, 7, 14, 21, 30, 60, 90, 100, 180, 365

Weekly summary:
  Type:              PeriodicWorkRequest, 7-day interval
  Day:               Sunday, 20:00 local time
```

**Android 13+ permission handling:**
- Check `POST_NOTIFICATIONS` permission before scheduling
- Show rationale dialog if denied once
- Settings screen toggle is disabled with a note if permission is permanently denied

---

## PART 2 — HOME SCREEN WIDGET SYSTEM

### Widget 1: Compact Widget

**Android files:**
```
CompactWidgetProvider.kt            (AppWidgetProvider)
res/layout/widget_compact_small.xml  (1×1 grid cell)
res/layout/widget_compact_medium.xml (2×2 grid cells)
res/xml/widget_compact_info.xml      (AppWidgetProviderInfo)
```

**Size mapping:**
```
Small  (1×1): minWidth=73dp  minHeight=73dp  → renders at ~155dp square visually
Medium (2×2): minWidth=150dp minHeight=150dp → renders at ~330dp square visually
```

**Visual structure (both sizes share the same layout, scale differs):**

```
Root view (fills widget):
  Shape:          rounded rectangle, radius = 32dp (small) / 32dp (medium)
  Background:     solid color from StreakColorEngine.getStreakColor(streak)
                  This is the dominant visual — the color IS the widget
  Overflow:       clip to rounded corners

Gloss layer (absolute, inset 0):
  Background:     linear-gradient 145° from rgba(255,255,255,0.18) → transparent 50%
  Not interactive

Noise texture layer:
  SVG fractalNoise overlay, mix-blend-mode overlay, opacity 6%
  Gives the background a tactile, premium feel

App name label (top-left):
  Font:           Bebas Neue
  Size:           11sp (small: hidden) / 11sp (medium: visible)
  Text:           "DAYONE"
  Color:          rgba(255,255,255,0.50)
  Position:       top 12dp, left 14dp
  Hidden on small size

Day count — hero element (center):
  Font:           Bebas Neue
  Size:           72sp (small) / 140sp (medium)
  Color:          rgba(255,255,255,0.97)
  Text shadow:    0 2dp 20dp rgba(0,0,0,0.30)
  Content:        current streak count (integer)
  Transition:     animate value change with spring (400ms)

"DAYS" label (below count):
  Font:           DM Sans weight 500
  Size:           10sp (small) / 14sp (medium)
  Color:          rgba(255,255,255,0.70)
  Letter-spacing: 0.25em
  Text:           "DAYS"
  Uppercase

Flame icon (bottom-right):
  Emoji:          🔥 (or status-appropriate emoji)
  Size:           18sp (small) / 30sp (medium)
  Position:       bottom 10dp right 11dp (small) / bottom 14dp right 16dp (medium)
  Opacity:        0.70

Milestone ring (when streak = milestone day):
  Layer:          drawn behind all content
  Style:          2dp solid border rgba(255,255,255,0.50)
  Animation:      pulse: opacity 0.6→0 / scale 1→1.04 / 2s ease-in-out infinite
  Only shown:     when currentStreak ∈ {1,3,7,14,21,30,60,90,100,180,365}
```

**CompactWidgetProvider.kt behavior:**
```kotlin
override fun onUpdate(context, manager, ids) {
    val streak = StreakRepository.getCurrentStreak()
    val color  = StreakColorEngine.getStreakColor(streak)
    val isMilestone = streak in MILESTONE_DAYS

    for (id in ids) {
        val views = RemoteViews(context.packageName, layoutForSize(id))
        views.setInt(R.id.widgetRoot, "setBackgroundColor", color)
        views.setTextViewText(R.id.streakCount, streak.toString())
        views.setViewVisibility(R.id.milestoneRing,
            if (isMilestone) View.VISIBLE else View.GONE)
        // Tap: open MainActivity
        val intent = Intent(context, MainActivity::class.java)
        val pi = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_IMMUTABLE)
        views.setOnClickPendingIntent(R.id.widgetRoot, pi)
        manager.updateAppWidget(id, views)
    }
}
```

---

### Widget 2: Expanded Widget

**Android files:**
```
ExpandedWidgetProvider.kt
res/layout/widget_expanded_5x2.xml
res/layout/widget_expanded_5x3.xml
res/xml/widget_expanded_info.xml
```

**Size mapping:**
```
5×2: minWidth=250dp  minHeight=110dp → renders ~460×160dp visually
5×3: minWidth=250dp  minHeight=175dp → renders ~460×240dp visually
```

**Visual structure:**

```
Root (fills widget):
  Background:     var(--bg-surface) = #0f1a36
  Border:         1dp rgba(79,142,247,0.12)
  Border-radius:  32dp
  Shadow:         inset 0 1dp rgba(255,255,255,0.06) + outer shadows
  Overflow:       hidden

Background pattern:
  Subtle radial gradient: rgba(79,142,247,0.04) at top-right corner

Glow decoration:
  280×280dp circle, position top-right offset
  radial-gradient: rgba(79,142,247,0.07) → transparent
  pointer-events none

Inner layout: horizontal split
  Left column:  148dp wide
  Right column: fills remaining width
  Padding:      16dp 18dp (5×2) / 18dp 20dp (5×3)
  Gap between columns: 14dp (5×2) / 16dp (5×3)

─── LEFT COLUMN ───────────────────────────────────────

App name (top of column):
  Font:           Bebas Neue
  Size:           10sp
  Text:           "DAYONE"
  Color:          #4a5980
  Letter-spacing: 0.20em / uppercase

Streak count (hero):
  Font:           Bebas Neue
  Size:           68sp (5×2) / 96sp (5×3)
  Line-height:    0.85
  Color:          StreakColorEngine.getStreakColor(streak)
  Content:        streak integer

"DAYS" sublabel:
  9sp / weight 500 / letter-spacing 0.20em / uppercase / color #4a5980
  Margin-top: 2dp

Date line (hidden on 5×2, shown on 5×3):
  Font:           DM Mono / 10sp / color #8aa0cc / tracking 0.04em
  Content:        formatted date, e.g. "MON 09 JUN"

Status row (bottom of column):
  Layout:         row, gap 5dp, items centered
  Status dot:     5×5dp circle, color = StreakColorEngine.getStreakColor(streak)
  Status text:    10sp / weight 500 / color #8aa0cc
                  e.g. "On streak" or "Streak broken"

Habit name (bottom):
  Font:           DM Sans / 12sp / weight 500 / color #f0f4ff
  Truncation:     single line, ellipsis
  Hidden size:    5×2 shows at 11sp, 5×3 shows normally
  Content:        default habit name

─── RIGHT COLUMN ───────────────────────────────────────

Calendar header row:
  Left:  "ACTIVITY" — 9sp / weight 500 / uppercase / letter-spacing 0.15em / color #4a5980
  Right: streak count label — same style

Mini contribution grid:
  Structure:      columns of day cells (weeks), left = oldest, right = newest
  Cell size:      auto-sized to fill available height
  Cell gap:       3dp
  Column gap:     3dp
  Total columns:  show last 12–16 weeks (trim to fit width)

  Cell colors:
    Completed day:  StreakColorEngine.getDayColor(streak, true, false)
    Missed day:     StreakColorEngine.getDayColor(0, false, true)  → red
    Future day:     rgba(255,255,255,0.02)
    Empty (padding):var(--bg-elevated) = #162040

  Today cell:
    Additional border: 2dp #4f8ef7

Day-of-week labels (left side of grid):
  Labels:         S M T W T F S (7 rows)
  Font:           9sp / color #4a5980 / letter-spacing 0.05em
  Alignment:      centered in each row
  On 5×2:         hide rows 1,3,5,7 (show only Mon/Wed/Fri) to avoid crowding
```

**ExpandedWidgetProvider.kt behavior:**
```kotlin
override fun onUpdate(context, manager, ids) {
    val streak    = StreakRepository.getCurrentStreak()
    val history   = HistoryRepository.getLast16Weeks()
    val habitName = HabitRepository.getDefaultHabitName()
    val color     = StreakColorEngine.getStreakColor(streak)

    for (id in ids) {
        val layout = layoutForSize(id)
        val views  = RemoteViews(context.packageName, layout)

        views.setTextViewText(R.id.streakCount, streak.toString())
        views.setTextColor(R.id.streakCount, color)
        views.setTextViewText(R.id.habitName, habitName)
        views.setTextViewText(R.id.dateLabel, formatDate(today))

        // Build contrib grid via RemoteViews bitmap or GridView adapter
        val bmp = ContribCalendarBuilder.buildBitmap(context, history, layout)
        views.setImageViewBitmap(R.id.contribGrid, bmp)

        // Tap: open CalendarActivity
        val intent = Intent(context, CalendarActivity::class.java)
        val pi = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_IMMUTABLE)
        views.setOnClickPendingIntent(R.id.widgetRoot, pi)

        manager.updateAppWidget(id, views)
    }
}
```

**ContribCalendarBuilder.kt:**
```kotlin
object ContribCalendarBuilder {
    fun buildBitmap(context: Context, weeks: List<Week>, layoutId: Int): Bitmap {
        // Calculate cell size from available dp
        // Draw rounded-rect cells with colors from StreakColorEngine
        // Draw today indicator as 2dp border in accent blue
        // Return bitmap for RemoteViews.setImageViewBitmap
    }
}
```

---

### Widget Update Triggers

Widgets must refresh when:
- Streak count changes
- A new day begins (midnight)
- User opens the app and data changes
- Habit is created, deleted, or reset
- Import/restore completes

**Implementation:**
```kotlin
// Call from anywhere streak changes:
AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(...)

// Or use broadcast:
context.sendBroadcast(Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE))
```

Use `AlarmManager` with `ACTION_DATE_CHANGED` broadcast to trigger midnight refresh.

---

## PART 3 — CALENDAR SCREEN (referenced by expanded widget tap)

When the user taps the expanded widget, they land on the Calendar screen.
Full specs are in the companion base-app prompt, but the key calendar elements referenced by widgets are:

**Month calendar grid:**
```
Each day cell:
  Shape:          square, border-radius 8dp
  Color:          StreakColorEngine.getDayColor(entry.streak, completed, missed)
  Today:          extra 2dp border, color #4f8ef7
  Milestone day:  animated ring (2dp white border, pulse 2s loop)
  Streak-start:   small 4dp dot at bottom center, color rgba(255,255,255,0.8)
  Missed day:     red fill + "×" overlay character at 30% white opacity
  Future day:     rgba(255,255,255,0.04) fill, color #4a5980, no interaction
  Empty cell:     transparent, no interaction

Tap a day → open DayDetailBottomSheet
```

**Day Detail Bottom Sheet:**
```
Sheet shape:        top corners 32dp radius
Handle bar:         36×4dp, color #4a5980, opacity 0.5, centered, mb 24dp

Color bar:
  Height:           6dp
  Color:            StreakColorEngine.getStreakColor(entry.streak)
  Border-radius:    pill
  Margin-bottom:    20dp

Date:               DM Mono / 12sp / color #4a5980 / tracking 0.1em
Streak number:      Bebas Neue / 64sp / line-height 0.9 / color = streak color
"DAY STREAK" label: 12sp / weight 500 / letter-spacing 0.2em / uppercase / color #4a5980

Status badge (pill):
  Completed:  background streak-color+33 opacity / text = streak color / "✅ Completed"
  Missed:     background rgba(255,79,109,0.15) / text #ff4f6d / "❌ Missed"
  Future:     background rgba(255,255,255,0.04) / text #4a5980 / "⏳ Future"

Fields:
  Three rows with left label + right placeholder
  Background: #162040 / border 1dp rgba(79,142,247,0.12) / radius 16dp / padding 14dp 16dp
  · 📝 Notes       → editable text field
  · 😊 Mood        → picker (future)
  · 📊 Stats       → read-only (future)

Close button:
  Full width / padding 16dp
  Background: rgba(79,142,247,0.12) / border 1dp rgba(79,142,247,0.4)
  Color: #4f8ef7 / 14sp / weight 500
```

---

## ANIMATIONS TO IMPLEMENT

| Element | Animation | Spec |
|---|---|---|
| Banner slide-in | Translate Y from -120dp to 0, scale 0.9→1 | Duration 400ms, ease (0.16,1,0.3,1), delay 600ms |
| Banner dismiss | Translate Y to -110dp, scale →0.92, fade | Duration 450ms, ease (0.16,1,0.3,1) |
| Banner spring-back | Translate Y to 0, scale 1, opacity 1 | Duration 350ms, spring (0.34,1.56,0.64,1) |
| Expanded open | Translate from -110dp to 0, scale 0.88→1 | Duration 400ms, ease (0.16,1,0.3,1) |
| Expanded close | Reverse of above | Duration 400ms |
| Progress bar fill | Width 0→target%, spring | Duration 1000ms, delay 500ms |
| Calendar cell reveal | Scale 0.5→1, opacity 0→1 | Duration 300ms, staggered per cell |
| Milestone ring pulse | opacity 0.6→0, scale 1→1.04, repeat | Duration 2s, ease-in-out, infinite |
| Widget tap | Scale to 0.97 | Duration 120ms |
| Widget press release | Scale to 1.02, translateY -4dp | Duration 300ms, spring |
| Milestone toast | Slide down from top: translateY -80dp→0 | Duration 400ms, spring |
| Toast dismiss | Slide back up | Duration 400ms |

---

## DELIVERABLES

Generate all of the following:

```
app/src/main/java/com/dayone/
  engine/
    StreakColorEngine.kt
    ContribCalendarBuilder.kt
  notification/
    NotificationService.kt
    NotificationChannelManager.kt
    NotificationScheduler.kt
  widget/
    CompactWidgetProvider.kt
    ExpandedWidgetProvider.kt
  calendar/
    CalendarActivity.kt
    CalendarViewModel.kt
    CalendarDayAdapter.kt
    DayDetailBottomSheet.kt

res/layout/
  notification_banner.xml
  notification_expanded.xml
  widget_compact_small.xml
  widget_compact_medium.xml
  widget_expanded_5x2.xml
  widget_expanded_5x3.xml

res/xml/
  widget_compact_info.xml
  widget_expanded_info.xml

res/drawable/
  (any custom shapes, gradient backgrounds, milestone ring animation)
```

**AndroidManifest.xml additions:**
```xml
<!-- Widgets -->
<receiver android:name=".widget.CompactWidgetProvider"
    android:exported="true">
  <intent-filter>
    <action android:name="android.appwidget.action.APPWIDGET_UPDATE"/>
  </intent-filter>
  <meta-data android:name="android.appwidget.provider"
    android:resource="@xml/widget_compact_info"/>
</receiver>

<receiver android:name=".widget.ExpandedWidgetProvider"
    android:exported="true">
  <intent-filter>
    <action android:name="android.appwidget.action.APPWIDGET_UPDATE"/>
  </intent-filter>
  <meta-data android:name="android.appwidget.provider"
    android:resource="@xml/widget_expanded_info"/>
</receiver>

<!-- Notification permission (Android 13+) -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM"/>
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
```

---

## FINAL REQUIREMENT

The notification card must feel like a premium, glass-morphism, dark-UI notification — not a stock Android notification card. Every spacing value, color, blur, and animation listed above comes directly from the DayOne prototype HTML. Build it to match exactly.

The widgets must feel like the background color IS the streak status. At a glance, green = great, red = broken. The streak number should be the largest, most prominent element. Do not redesign for Material You or any other Android design language.

Both systems must update in real-time as the user's streak data changes.
