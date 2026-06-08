# DayOne — Complete Android App Conversion Prompt

> Give this entire document to your AI coding tool (Codex, CodeGPT, Cursor, etc.).
> It describes every pixel, every function, and every behavior from the source HTML app.

---

## MISSION

Convert the attached `dayone.html` single-file web app into a complete, installable
Android APK. The Android app must be **visually and functionally identical** to the
web page. Do not redesign it. Do not simplify it. Do not replace anything with
Material Design components. The web app IS the design spec — match it exactly.

---

## IMPLEMENTATION STRATEGY (MANDATORY)

Use an **Android WebView** wrapper, not a native Kotlin rebuild.

**Why WebView:** The app uses CSS glassmorphism, SVG ring animations, custom fonts,
CSS keyframe animations, backdrop-filter blur, radial-gradient backgrounds, CSS
custom properties, and pixel-perfect layout math. A native rebuild will never match
this without months of work. WebView renders the HTML/CSS/JS almost identically because
it runs on Chromium (Android System WebView).

## what it modify or work on the prieves app code edite the full code to make it look like this html code 
neo@neo:~/pro/app-app$ ls
app    


## sources 
neo@neo:~/pro/app-app$ ls
app                                     DayOne_README.md
dayone.html                             dayone-widgets.html
dayone-notification.html                image-icon.png
dayone-notifications-widgets-README.md
neo@neo:~/pro/app-app$ 

DayOne_README.md              rule or peompt 
dayone-notifications-widgets-README.md       rule or peompt
dayone.html                source how the app need look
dayone-widgets.html              source how the dayone-widgets
dayone-notification.html         source how the dayone-notification
          


## use this icon for the app 
image-icon.png image 


### Project Setup
- Language: **Kotlin**
- Minimum SDK: API 26 (Android 8.0)
- Target SDK: API 34
- Single `MainActivity.kt` with one `WebView` that fills the entire screen
- No other Activities or Fragments needed

### WebView Configuration (required settings)
```kotlin
webView.settings.apply {
    javaScriptEnabled = true
    domStorageEnabled = true          // localStorage persistence
    databaseEnabled = true
    allowFileAccess = true
    allowContentAccess = true
    loadWithOverviewMode = true
    useWideViewPort = true
    setSupportZoom(false)
    builtInZoomControls = false
    displayZoomControls = false
    mediaPlaybackRequiresUserGesture = false
    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
    cacheMode = WebSettings.LOAD_DEFAULT
}
webView.setLayerType(View.LAYER_TYPE_HARDWARE, null) // GPU acceleration for blur effects
webView.isVerticalScrollBarEnabled = false
webView.isHorizontalScrollBarEnabled = false
```

### Asset Placement
Place the HTML file and all assets in `app/src/main/assets/`:
```
app/src/main/assets/
├── dayone.html          ← the main app file
├── fonts/
│   ├── BebasNeue-Regular.ttf
│   ├── DMSans-Light.ttf
│   ├── DMSans-Regular.ttf
│   ├── DMSans-Medium.ttf
│   ├── DMMono-Regular.ttf
│   └── DMMono-Medium.ttf
```

Load the HTML with:
```kotlin
webView.loadUrl("file:///android_asset/dayone.html")
```

### Font Loading Fix (critical)
The HTML currently uses Google Fonts via CDN. This will fail when offline.
Replace the `<link>` tag in the HTML head with local `@font-face` rules:
```css
@font-face {
  font-family: 'Bebas Neue';
  src: url('fonts/BebasNeue-Regular.ttf') format('truetype');
}
@font-face {
  font-family: 'DM Sans';
  src: url('fonts/DMSans-Light.ttf') format('truetype');
  font-weight: 300;
}
@font-face {
  font-family: 'DM Sans';
  src: url('fonts/DMSans-Regular.ttf') format('truetype');
  font-weight: 400;
}
@font-face {
  font-family: 'DM Sans';
  src: url('fonts/DMSans-Medium.ttf') format('truetype');
  font-weight: 500;
}
@font-face {
  font-family: 'DM Mono';
  src: url('fonts/DMMono-Regular.ttf') format('truetype');
  font-weight: 400;
}
@font-face {
  font-family: 'DM Mono';
  src: url('fonts/DMMono-Medium.ttf') format('truetype');
  font-weight: 500;
}
```

---

## CRITICAL FIRST-LAUNCH REQUIREMENT

**The app must start completely empty on first install. Zero. Blank. Nothing.**

### What the HTML currently does (WRONG — must be fixed)
In the HTML source, the `init()` function contains:
```javascript
function init() {
  loadState();
  if (!state.habits.length) seedSampleData(); // ← THIS LINE MUST BE DELETED
  ...
}
```

The `seedSampleData()` function creates a fake "No Sugar" habit with a 12-day timer
already running and fake history already filled in. **Remove this entirely.**

### What the app must do instead
Replace that check with nothing:
```javascript
function init() {
  loadState();
  // No seedSampleData call — user starts fresh
  applyTheme();
  buildEmojiRow();
  buildColorRow();
  startTimer();
  const now = new Date();
  calYear  = now.getFullYear();
  calMonth = now.getMonth();
  if (state.settings.notifications && 'Notification' in window) {
    Notification.requestPermission();
  }
  scheduleDailyNotifs();
}
```

### Initial state on first launch
```javascript
let state = {
  habits: [],                          // empty — no habits exist
  settings: {
    defaultHabitId: null,              // no default
    theme: 'dark',                     // dark mode by default
    notifications: false               // notifications off by default
  },
  history: {}                          // no history entries
};
```

### Empty state behavior
- **Home screen:** Timer ring shows 0. Days display shows "0". H:M:S show "00:00:00".
  No habit name in the header label. The milestone chip is hidden.
- **Dashboard screen:** The habits list shows the `.empty-state` div with the SVG icon
  and the text "No habits yet. Tap + to add your first habit."
- **History screen:** The habit tabs row is empty. The calendar shows the current month
  with no colored days.
- **Settings → Default Habit:** Shows "—" (the em dash placeholder).

---

## DESIGN SYSTEM (exact values — do not approximate)

### Color Tokens — Dark Theme (default)
```
--bg-deep:        #060b18
--bg-base:        #0a1128
--bg-surface:     #0f1a36
--bg-elevated:    #162040
--bg-glass:       rgba(15, 26, 54, 0.7)
--bg-glass2:      rgba(255, 255, 255, 0.04)

--accent-primary:     #4f8ef7
--accent-glow:        rgba(79, 142, 247, 0.35)
--accent-soft:        rgba(79, 142, 247, 0.12)
--accent-cyan:        #38d9d9
--accent-cyan-glow:   rgba(56, 217, 217, 0.3)
--accent-purple:      #7b6ff0
--accent-success:     #2dd87b
--accent-danger:      #ff4f6d
--accent-warn:        #ffb547

--text-primary:   #f0f4ff
--text-secondary: #8aa0cc
--text-muted:     #4a5980
--text-inverse:   #060b18

--border-subtle: rgba(79, 142, 247, 0.12)
--border-active: rgba(79, 142, 247, 0.4)
```

### Color Tokens — Light Theme
```
--bg-deep:       #eef2ff
--bg-base:       #f4f7ff
--bg-surface:    #ffffff
--bg-elevated:   #ffffff
--bg-glass:      rgba(255, 255, 255, 0.85)
--bg-glass2:     rgba(0, 0, 0, 0.03)
--text-primary:  #0d1b3e
--text-secondary:#4a6090
--text-muted:    #9baac8
--text-inverse:  #f0f4ff
--border-subtle: rgba(79, 142, 247, 0.15)
--border-active: rgba(79, 142, 247, 0.5)
--shadow-md:     0 8px 32px rgba(79, 142, 247, 0.12)
```

### Fonts
- `--font-display`: 'Bebas Neue' — used for all large numbers and screen titles
- `--font-body`: 'DM Sans' — used for all body text, labels, buttons
- `--font-mono`: 'DM Mono' — used for the H:M:S timer values

### Spacing Scale
```
--space-xs:  4px
--space-sm:  8px
--space-md:  16px
--space-lg:  24px
--space-xl:  40px
--space-2xl: 64px
```

### Border Radius
```
--radius-sm:   8px
--radius-md:   16px
--radius-lg:   24px
--radius-xl:   40px
--radius-full: 9999px (pill shape)
```

### Shadows
```
--shadow-sm:   0 2px 8px rgba(0,0,0,0.3)
--shadow-md:   0 8px 32px rgba(0,0,0,0.4)
--shadow-glow: 0 0 40px rgba(79,142,247,0.35)
--shadow-cyan: 0 0 30px rgba(56,217,217,0.3)
```

### Transitions
```
--t-fast:   150ms ease
--t-base:   250ms ease
--t-slow:   400ms cubic-bezier(0.16, 1, 0.3, 1)
--t-spring: 500ms cubic-bezier(0.34, 1.56, 0.64, 1)
```

---

## ANIMATED BACKGROUND

The background is not a static color — it is a layered animated effect:

### Layer 1 — Mesh Gradient (`.bg-mesh`)
A `position: fixed; inset: 0` div with:
- `::before` pseudo-element with three overlapping radial gradients:
  - `radial-gradient(ellipse 80% 60% at 20% 10%, rgba(79,142,247,0.18), transparent 60%)`
  - `radial-gradient(ellipse 60% 50% at 80% 80%, rgba(123,111,240,0.14), transparent 55%)`
  - `radial-gradient(ellipse 50% 40% at 50% 50%, rgba(56,217,217,0.07), transparent 60%)`
- `::after` pseudo-element with an SVG fractal noise texture at `background-size: 256px`
  and `opacity: 0.5` for a subtle grain texture

### Layer 2 — Floating Orbs (`.orb`)
Three `position: fixed` divs, each with:
- `border-radius: 50%`
- `filter: blur(80px)`
- `pointer-events: none`
- Each one animated with `orbFloat` keyframes (20s ease-in-out infinite)
  which translates them 30–60px and scales them slightly

**Orb 1:** 500×500px, `rgba(79,142,247,0.15)` radial gradient, top:-200px left:-200px
**Orb 2:** 400×400px, `rgba(123,111,240,0.12)` radial gradient, bottom:-150px right:-100px,
  animation-delay: -7s
**Orb 3:** 300×300px, `rgba(56,217,217,0.10)` radial gradient, top:50% left:60%,
  animation-delay: -14s

```css
@keyframes orbFloat {
  0%, 100% { transform: translate(0,0) scale(1); }
  33%       { transform: translate(40px,-60px) scale(1.05); }
  66%       { transform: translate(-30px,40px) scale(0.95); }
}
```

---

## BOTTOM NAVIGATION BAR

Fixed at the bottom of the screen across all 4 screens. All 4 tabs are always visible.

```
position: fixed; bottom: 0; left: 0; right: 0;
z-index: 100;
display: flex; justify-content: space-around;
padding: 12px 40px calc(12px + env(safe-area-inset-bottom));
background: rgba(15, 26, 54, 0.7);
backdrop-filter: blur(20px) saturate(1.5);
border-top: 1px solid rgba(79,142,247,0.12);
```

### Tab Items (4 total)
Each tab item:
```
display: flex; flex-direction: column; align-items: center; gap: 4px;
padding: 8px 20px;
border-radius: 9999px;
font-size: 11px; font-weight: 500;
letter-spacing: 0.05em; text-transform: uppercase;
color: var(--text-muted);      ← inactive state
transition: all 250ms ease;
```

**Tab 1 — Timer:**
- Icon: clock/circle with a clock hand SVG (22×22px, stroke-width: 2)
- Label: "Timer"
- Screen: home

**Tab 2 — Habits:**
- Icon: 2×2 grid of rectangles SVG (22×22px)
- Label: "Habits"
- Screen: dashboard

**Tab 3 — History:**
- Icon: calendar SVG with a grid of dots (22×22px)
- Label: "History"
- Screen: history

**Tab 4 — Settings:**
- Icon: gear/cog SVG with inner circle (22×22px)
- Label: "Settings"
- Screen: settings

### Active Tab State
```css
.nav-item.active {
  color: #4f8ef7;    /* --accent-primary */
}
.nav-item.active svg {
  transform: translateY(-2px) scale(1.1);
  filter: drop-shadow(0 0 6px rgba(79,142,247,0.35));
}
/* Top indicator line */
.nav-item.active::before {
  content: '';
  position: absolute;
  top: 0; left: 50%; transform: translateX(-50%);
  width: 30px; height: 2px;
  background: #4f8ef7;
  border-radius: 0 0 2px 2px;
  box-shadow: 0 0 8px rgba(79,142,247,0.35);
}
```

---

## SCREEN 1 — HOME (TIMER)

**Purpose:** Shows the live elapsed time for the currently selected default habit.

### Layout
```
display: flex;
flex-direction: column;
justify-content: center;
align-items: center;
min-height: 100vh;
padding-bottom: 80px;   ← space for nav bar
position: relative;
```

### Fixed Header (`.home-header`)
Position: fixed, top 0, full width, z-index 50.
Padding: `calc(16px + env(safe-area-inset-top)) 40px 16px`
`display: flex; justify-content: space-between; align-items: center;`
`pointer-events: none;` (children re-enable)

**Left side — Habit Label (`.habit-label`):**
```
font-family: DM Sans; font-size: 13px; font-weight: 500;
letter-spacing: 0.15em; text-transform: uppercase;
color: #8aa0cc;
display: flex; align-items: center; gap: 8px;
```
Contains:
- A pulsing green dot (`.habit-dot`):
  `width: 7px; height: 7px; border-radius: 50%;`
  `background: #2dd87b; box-shadow: 0 0 8px #2dd87b;`
  Animated: `pulse-dot 2s ease-in-out infinite` (scale 1→1.4→1, opacity 1→0.7→1)
- The habit name text (updated when default habit changes)

**Right side — Settings icon button (`.btn-icon`):**
```
width: 40px; height: 40px;
border-radius: 8px;
background: rgba(255,255,255,0.04);
border: 1px solid rgba(79,142,247,0.12);
color: #8aa0cc;
backdrop-filter: blur(8px);
```
Clicking navigates to the Settings screen.

### Hero Timer (`.hero-timer`)
```
display: flex; flex-direction: column; align-items: center;
gap: 40px; text-align: center; position: relative;
```

#### Timer Ring (`.timer-ring`)
```
position: relative;
width: 280px; height: 280px;
display: flex; align-items: center; justify-content: center;
```

**SVG ring** (`.timer-ring-svg`), `position: absolute; inset: 0; transform: rotate(-90deg)`:
- Background circle: `fill: none; stroke: rgba(79,142,247,0.1); stroke-width: 2`
- Fill arc: `fill: none; stroke: #4f8ef7; stroke-width: 2; stroke-linecap: round;`
  `stroke-dasharray: 800; stroke-dashoffset: 800;` (changes 0→800 as day progresses)
  `filter: drop-shadow(0 0 6px #4f8ef7);`
  `transition: stroke-dashoffset 1s ease;`

**Inner content** (`.timer-ring-inner`), `position: relative; z-index: 1; text-align: center`:

**Days number** (`.timer-days`):
```
font-family: 'Bebas Neue';
font-size: clamp(96px, 20vw, 140px);
line-height: 0.9;
color: #f0f4ff;
letter-spacing: -2px;
text-shadow: 0 0 60px rgba(79,142,247,0.3), 0 0 120px rgba(79,142,247,0.1);
```
When the day count changes, this element gets the `countFlip` animation:
```css
@keyframes countFlip {
  from { transform: translateY(-10px) scale(0.96); opacity: 0.6; }
  to   { transform: translateY(0) scale(1); opacity: 1; }
}
```

**Days label** (`.timer-days-label`):
```
font-family: DM Sans; font-size: 12px; font-weight: 500;
letter-spacing: 0.3em; text-transform: uppercase;
color: #4f8ef7; margin-top: 4px;
```
Text: "DAYS" when counting up; "REMAINING" when countdown.

#### Hours / Minutes / Seconds Row (`.timer-hms`)
```
display: flex; gap: 40px; align-items: center;
```
Contains three unit groups separated by two separator colons.

**Each unit** (`.timer-unit`):
```
display: flex; flex-direction: column; align-items: center; gap: 6px;
```

**Unit value** (`.timer-unit-value`):
```
font-family: 'DM Mono'; font-size: clamp(28px, 6vw, 44px);
font-weight: 400; color: #f0f4ff; line-height: 1;
min-width: 60px; text-align: center;
```
When the value changes, add class `.tick` for 300ms:
```css
@keyframes numTick {
  0%   { transform: translateY(-8px); opacity: 0; }
  100% { transform: translateY(0);    opacity: 1; }
}
```

**Unit label** (`.timer-unit-label`):
```
font-size: 9px; font-weight: 500; letter-spacing: 0.25em;
text-transform: uppercase; color: #4a5980;
```
Text: "HRS" / "MIN" / "SEC"

**Separators** (`.timer-sep`):
```
font-family: 'DM Mono'; font-size: 32px; color: #4a5980; opacity: 0.5;
animation: blink 2s ease-in-out infinite;
margin-top: -12px;
```
```css
@keyframes blink { 0%,100%{opacity:0.5} 50%{opacity:0.15} }
```

#### Milestone Chip (`.milestone-chip`)
Shown just below the H:M:S row when a milestone day is reached:
```
display: inline-flex; align-items: center; gap: 8px;
padding: 8px 20px;
background: rgba(79,142,247,0.12); border: 1px solid rgba(79,142,247,0.4);
border-radius: 9999px; font-size: 13px; font-weight: 500;
color: #4f8ef7; letter-spacing: 0.05em;
opacity: 0; transform: translateY(10px); transition: all 400ms;
```
When visible, add `.visible` class: `opacity: 1; transform: translateY(0)`

### Swipe Hint
```
position: fixed; bottom: 90px; left: 50%; transform: translateX(-50%);
display: flex; flex-direction: column; align-items: center; gap: 6px;
color: #4a5980; font-size: 11px; letter-spacing: 0.1em; text-transform: uppercase;
animation: swipeHintFade 3s ease-in-out 2s both;   ← auto-disappears
pointer-events: none;
```
SVG arrow bounces with `swipeArrow` animation. Fades in after 2s, fades out after 3s.
Text: "SWIPE DOWN TO RESET"

### Swipe-Down Gesture (Reset Streak)
On the home screen, detecting a downward swipe of more than 60px opens the Reset Panel.
```javascript
let touchStartY = 0;
document.getElementById('screen-home').addEventListener('touchstart', e => {
  touchStartY = e.touches[0].clientY;
});
document.getElementById('screen-home').addEventListener('touchmove', e => {
  if (e.touches[0].clientY - touchStartY > 60) {
    openResetPanel();
    touchStartY = 0;
  }
});
```

### Reset Panel (`.reset-panel`)
Slides up from the bottom of the screen:
```
position: fixed; bottom: -100%; left: 0; right: 0; z-index: 200;
background: #0f1a36;
border-top: 1px solid rgba(79,142,247,0.12);
border-radius: 24px 24px 0 0;
padding: 40px; padding-bottom: calc(40px + env(safe-area-inset-bottom));
transition: bottom 400ms cubic-bezier(0.16,1,0.3,1);
```
`.open` state: `bottom: 0`

Contents:
- Handle bar: `width: 40px; height: 4px; background: #4a5980; border-radius: 9999px;`
  `margin: 0 auto 40px; opacity: 0.4;`
- Title `<h3>` (Bebas Neue, 28px): "Reset Streak?"
- Body `<p>` (DM Sans 14px, color: #8aa0cc): "This will reset your streak back to zero..."
- Two buttons in a flex row:
  - "Reset" (`btn-danger`): `background: rgba(255,79,109,0.15); border: 1px solid rgba(255,79,109,0.4); color: #ff4f6d`
  - "Cancel" (`btn-ghost`): `background: rgba(255,255,255,0.04); border: 1px solid rgba(79,142,247,0.12); color: #8aa0cc`

The overlay backdrop dims the screen behind the reset panel. Tapping the overlay closes it.

---

## SCREEN 2 — DASHBOARD (HABITS)

### Screen Header
```
padding: calc(20px + env(safe-area-inset-top)) 40px 24px;
display: flex; justify-content: space-between; align-items: flex-end;
```
- Title "HABITS" in Bebas Neue, 40px, color: #f0f4ff
- Subtitle "Your progress · N habits" in DM Sans 13px, color: #4a5980, margin-top: 4px

### Contribution Graph Section (`.contrib-section`)
```
margin: 0 40px 40px;
background: rgba(15,26,54,0.7); backdrop-filter: blur(12px);
border: 1px solid rgba(79,142,247,0.12); border-radius: 24px;
padding: 24px;
```
- Section label: "ACTIVITY" in 11px uppercase, letter-spacing: 0.2em, color: #4a5980
- Contribution grid (`.contrib-grid`):
  `display: flex; gap: 4px; flex-wrap: wrap; overflow: hidden`

This is a GitHub-style heatmap of the last **16 weeks** (112 days).
Each week is a column (`.contrib-week`): `display: flex; flex-direction: column; gap: 4px`
Each day is a cell (`.contrib-day`): `width: 13px; height: 13px; border-radius: 3px`

Day cell colors by class:
- No class → `background: #162040` (empty/no data)
- `.level-1` → `rgba(45,216,123,0.25)` (1 day at this date? or habit active)
- `.level-2` → `rgba(45,216,123,0.45)`
- `.level-3` → `rgba(45,216,123,0.65)`
- `.level-4` → `rgba(45,216,123,0.85)` (most active / most habits tracked)
- `.fail` → `rgba(255,79,109,0.4)` (relapse recorded)

The level is determined by how many habits had a success recorded on that day.
On hover: `transform: scale(1.5); z-index: 1`

### Stats Row (`.stats-row`)
```
display: grid; grid-template-columns: repeat(3, 1fr);
gap: 8px; margin: 0 40px 40px;
```
Three stat cards:

Each card (`.stat-card`):
```
background: rgba(15,26,54,0.7); backdrop-filter: blur(12px);
border: 1px solid rgba(79,142,247,0.12); border-radius: 16px;
padding: 16px 8px; text-align: center;
```
On hover: `border-color: rgba(79,142,247,0.4); transform: translateY(-2px)`

Value (`.stat-value`): Bebas Neue, 32px, color: #f0f4ff
Label (`.stat-label`): 10px, uppercase, letter-spacing: 0.15em, color: #4a5980

Stats displayed:
1. **Current streak** — label: "STREAK"
2. **Total habits** — label: "HABITS"
3. **Longest streak** across all habits — label: "LONGEST"

### Habits List (`.habits-list`)
```
display: flex; flex-direction: column; gap: 16px;
padding: 0 40px;
```

Empty state (`.empty-state`):
```
text-align: center; padding: 64px 40px; color: #4a5980;
```
Contains:
- SVG icon (opacity: 0.3, margin-bottom: 16px) — a simple grid/squares icon
- Text: "No habits yet" (larger text)
- Subtext: "Tap + to add your first habit" (14px, line-height: 1.6)

### Habit Card (`.habit-card`)
```
background: rgba(15,26,54,0.7); backdrop-filter: blur(12px);
border: 1px solid rgba(79,142,247,0.12); border-radius: 16px;
padding: 24px; display: flex; align-items: center; gap: 16px;
cursor: pointer; position: relative; overflow: hidden;
```

**Left accent bar:** `position: absolute; left: 0; top: 0; bottom: 0; width: 3px;`
`background: <habit color>; border-radius: 0 3px 3px 0;`

**Icon square** (`.habit-card-icon`):
`width: 44px; height: 44px; border-radius: 8px; background: <habit color at 12% opacity>;`
`display: flex; align-items: center; justify-content: center; font-size: 22px;`
Contains the habit emoji.

**Info area** (`.habit-card-info`, `flex: 1; min-width: 0`):
- Name: `font-size: 15px; font-weight: 500; color: #f0f4ff; white-space: nowrap;`
  `overflow: hidden; text-overflow: ellipsis;`
- Meta: `font-size: 12px; color: #4a5980; margin-top: 3px;`
  Text: "COUNT UP · Since Jan 1, 2025" or "COUNTDOWN · N days left"

**Streak area** (`.habit-card-streak`, `text-align: right; flex-shrink: 0`):
- Day count: Bebas Neue, 28px, color: habit's accent color
- Label: `font-size: 10px; letter-spacing: 0.15em; text-transform: uppercase; color: #4a5980;`
  Text: "DAYS"

**Default badge** (`.badge-default`, shown only on the default habit card):
`position: absolute; top: 12px; right: 12px;`
`font-size: 9px; font-weight: 600; letter-spacing: 0.15em; text-transform: uppercase;`
`color: #4f8ef7; background: rgba(79,142,247,0.12); border: 1px solid rgba(79,142,247,0.4);`
`border-radius: 9999px; padding: 3px 8px;`
Text: "DEFAULT"

Default habit card also has extra highlight:
`.is-default { border-color: rgba(79,142,247,0.4); background: rgba(79,142,247,0.12); }`

On hover: `border-color: rgba(79,142,247,0.4); transform: translateY(-2px); box-shadow: 0 8px 32px rgba(0,0,0,0.4);`

Tapping a habit card opens the **Habit Detail Panel**.

### FAB (Floating Action Button)
```
position: fixed; bottom: 88px; right: 40px;
width: 56px; height: 56px; border-radius: 50%;
background: #4f8ef7; color: #060b18;
box-shadow: 0 0 40px rgba(79,142,247,0.35), 0 8px 24px rgba(79,142,247,0.4);
```
Contains a "+" SVG icon (24×24px, stroke-width: 2).
On hover: `transform: scale(1.1) rotate(45deg)`
Tapping opens the Create Habit Modal.

---

## SCREEN 3 — HISTORY

### Screen Header
- Title: "HISTORY" (Bebas Neue, 40px)
- Subtitle: "Your streaks over time" (13px, color: #4a5980)

### Habit Tab Strip (`.hist-habit-tabs`)
```
display: flex; gap: 8px; overflow-x: auto;
padding-bottom: 4px; scrollbar-width: none;
margin: 0 40px 40px;
```
One pill tab per habit. Active tab is highlighted with:
```
background: rgba(79,142,247,0.12); border-color: rgba(79,142,247,0.4);
color: #4f8ef7;
```
Inactive tabs: `background: rgba(255,255,255,0.04); color: #4a5980;`
Each tab pill: `padding: 8px 18px; border-radius: 9999px; font-size: 13px;`

### Calendar Navigation (`.calendar-nav`)
```
display: flex; align-items: center; justify-content: space-between;
margin-bottom: 40px;
```
- Prev button: `<` arrow (40×40px, same btn-icon style)
- Month + Year title: Bebas Neue, 32px, letter-spacing: 1px
  Example: "JUNE 2025"
- Next button: `>` arrow

### Calendar Grid (`.calendar-grid`)
```
display: grid; grid-template-columns: repeat(7, 1fr);
gap: 8px;
```
**Header row** (7 cells): "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"
`font-size: 10px; font-weight: 500; letter-spacing: 0.15em; text-transform: uppercase;`
`color: #4a5980; text-align: center; padding-bottom: 8px;`

**Day cells** (`.cal-day`):
```
aspect-ratio: 1; display: flex; align-items: center; justify-content: center;
border-radius: 8px; font-size: 13px; font-weight: 500; color: #4a5980;
```
- `.success` → `background: rgba(45,216,123,0.2); color: #2dd87b; box-shadow: 0 0 10px rgba(45,216,123,0.1)`
- `.fail` → `background: rgba(255,79,109,0.15); color: #ff4f6d`
- `.today` → `border: 1px solid rgba(79,142,247,0.4); color: #4f8ef7; background: rgba(79,142,247,0.12)`
- `.empty` → `opacity: 0; pointer-events: none` (padding cells at start of month)

### Calendar Legend
Centered row below the grid:
- Green square + "Success"
- Red square + "Relapse"
Each: `display: flex; align-items: center; gap: 8px; font-size: 12px; color: #4a5980;`
Square: `width: 12px; height: 12px; border-radius: 3px;`

---

## SCREEN 4 — SETTINGS

### Screen Header
- Title: "SETTINGS" (Bebas Neue, 40px)
- Subtitle: "Preferences & data" (13px, color: #4a5980)

### Section Label Style
```
font-size: 11px; font-weight: 500; letter-spacing: 0.2em; text-transform: uppercase;
color: #4a5980; margin: 40px 40px 8px;
```

### Settings List Container
```
margin: 0 40px 40px;
background: rgba(15,26,54,0.7); backdrop-filter: blur(12px);
border: 1px solid rgba(79,142,247,0.12); border-radius: 16px;
overflow: hidden;
```

### Settings Item Structure
Each item:
```
display: flex; align-items: center; justify-content: space-between;
padding: 16px 24px; cursor: pointer; transition: background 150ms;
border-bottom: 1px solid rgba(79,142,247,0.12);
```
Last item in a group has no bottom border.
On hover: `background: rgba(255,255,255,0.04)`

Left side:
- Icon square (36×36, border-radius: 10px, background: #162040, font-size: 18px)
- Text column: label (14px, font-weight: 500, #f0f4ff) + sub (12px, #4a5980)

Right side: value text and/or chevron `>` icon (16px, stroke-width: 2)

### Section 1 — GENERAL

**Default Habit row:**
- Icon: 🏠
- Label: "Default Habit"
- Sub: "Shown on homepage"
- Right: habit name text (or "—") + chevron
- Action: opens Default Habit Picker

**Notifications row:**
- Icon: 🔔
- Label: "Notifications"
- Sub: "Daily reminders & milestones"
- Right: toggle switch (see below)

**Toggle switch structure:**
```html
<label class="toggle">
  <input type="checkbox">
  <div class="toggle-track"></div>
  <div class="toggle-thumb"></div>
</label>
```
Track: `width: 44px; height: 26px; border-radius: 9999px;`
  Off: `background: #162040; border: 1px solid rgba(79,142,247,0.12);`
  On: `background: #4f8ef7; border-color: #4f8ef7;`
Thumb: `width: 18px; height: 18px; background: white; border-radius: 50%;`
  Off position: `top: 3px; left: 3px`
  On position: `left: 21px` (animated with spring transition)

### Section 2 — APPEARANCE

**Theme row:**
- Icon: 🎨
- Label: "Theme"
- Sub: "Light or dark mode"
- Right: "Dark" or "Light" text label (updates when toggled)
- Action: `cycleTheme()` — toggles between dark and light

### Section 3 — DATA

**Export Backup row:**
- Icon: 📤
- Label: "Export Backup"
- Sub: "Download your data as JSON"
- Action: `exportData()` — serializes `state` object and triggers a file download

**Import Backup row:**
- Icon: 📥
- Label: "Import Backup"
- Sub: "Restore from a JSON file"
- Action: triggers a hidden `<input type="file" accept=".json">` click

**Clear All Data row:**
- Icon: 🗑️
- Label: "Clear All Data" (color: #ff4f6d)
- Sub: "Permanently delete everything"
- Action: opens confirm dialog → wipes localStorage and reinitializes state

### Section 4 — ABOUT
- Icon: 💡
- Label: "DayOne"
- Sub: "Version 1.0.0 — Built for you"
- No right content or action

---

## MODAL — CREATE / EDIT HABIT

Triggered by: FAB (+) button on Dashboard, or "Edit" button on Habit Detail Panel.

### Backdrop
```
position: fixed; inset: 0; z-index: 500;
background: rgba(6,11,24,0.8); backdrop-filter: blur(8px);
display: flex; align-items: flex-end;
opacity: 0; pointer-events: none; transition: opacity 250ms;
```
`.open` → `opacity: 1; pointer-events: all`
Tapping the backdrop closes the modal.

### Modal Sheet
```
width: 100%; max-height: 92vh; overflow-y: auto;
background: #0f1a36;
border-radius: 24px 24px 0 0;
padding: 40px; padding-bottom: calc(40px + env(safe-area-inset-bottom));
transform: translateY(100%); transition: transform 400ms cubic-bezier(0.16,1,0.3,1);
```
`.open` on backdrop → `.modal { transform: translateY(0) }`

### Handle Bar
```
width: 40px; height: 4px; background: #4a5980; opacity: 0.4;
border-radius: 9999px; margin: 0 auto 40px;
```

### Form Fields

**1. Habit Name**
- Label: "HABIT NAME" (form-label style)
- Input: full-width text input, placeholder: "e.g. No Sugar, Daily Run…", maxlength: 40
  `background: #162040; border: 1px solid rgba(79,142,247,0.12); border-radius: 16px;`
  `padding: 14px 16px; color: #f0f4ff; font-size: 15px;`
  On focus: `border-color: #4f8ef7; box-shadow: 0 0 0 3px rgba(79,142,247,0.12);`

**2. Icon (Emoji Picker)**
- Label: "ICON"
- A row of 12 emoji buttons, each 44×44px, `border-radius: 8px;`
  `background: #162040; border: 2px solid transparent; font-size: 22px;`
  Selected: `border-color: #4f8ef7; background: rgba(79,142,247,0.12);`
  `box-shadow: 0 0 12px rgba(79,142,247,0.35);`

Emoji list: 🔥 💪 🏃 📚 🧘 🥗 💤 ✍️ 🎯 💧 🚭 🧹

**3. Color Palette**
- Label: "COLOR"
- A row of 6 color swatches, each 36×36px, `border-radius: 50%;`
  `border: 3px solid transparent; cursor: pointer;`
  Selected: `border-color: white; box-shadow: 0 0 12px rgba(255,255,255,0.3); transform: scale(1.15);`

Colors: #4f8ef7 (blue), #2dd87b (green), #ff4f6d (red), #ffb547 (orange), #7b6ff0 (purple), #38d9d9 (cyan)

**4. Count Type**
- Label: "COUNT TYPE"
- Two-segment pill selector (`.count-tabs`):
  `display: grid; grid-template-columns: 1fr 1fr; gap: 8px;`
  `background: #162040; border-radius: 16px; padding: 4px;`
  
  Each tab: `padding: 12px; border-radius: 8px; text-align: center; font-size: 13px;`
  Inactive: `color: #4a5980`
  Active: `background: #4f8ef7; color: #060b18; box-shadow: 0 2px 8px rgba(79,142,247,0.4);`

  Options:
  - "Count Up ↑" — habit tracks elapsed days
  - "Countdown ↓" — habit tracks days until a target date

**5. Start Date**
- Label: "START DATE"
- `<input type="date">` styled as form-input
- Default: today's date
- Logic: if user picks today, start time is exact `new Date().toISOString()` (timer begins at 0:0:0);
  if past date, use local midnight of that date

**6. Target Date** (countdown only — visible only when "Countdown" tab is active)
- Label: "TARGET DATE"
- `<input type="date">` styled as form-input
- This field is hidden (`display: none`) when Count Up is selected,
  visible (`display: block`) when Countdown is selected

### Save Button
```
width: 100%; padding: 18px;
background: #4f8ef7; color: #060b18;
border-radius: 16px; font-size: 15px; font-weight: 500; letter-spacing: 0.03em;
box-shadow: 0 4px 16px rgba(79,142,247,0.3);
transition: all 500ms cubic-bezier(0.34,1.56,0.64,1);
```
On hover: `transform: translateY(-2px); box-shadow: 0 8px 24px rgba(79,142,247,0.4);`

In create mode: button text is "Create Habit"
In edit mode: button text is "Save Changes"

### Modal Title
Bebas Neue, 32px, letter-spacing: 1px.
Create mode: "New Habit"
Edit mode: "Edit Habit"

### Validation
- If name is empty: show error toast "Please enter a habit name."
- If start date is missing: show error toast "Please select a start date."
- If countdown with no target date: show error toast "Please set a target date for countdown."

---

## HABIT DETAIL PANEL

A full-screen panel that slides in from the right when a habit card is tapped.

```
position: fixed; inset: 0; z-index: 400;
background: #0a1128;
transform: translateX(100%); transition: transform 400ms cubic-bezier(0.16,1,0.3,1);
overflow-y: auto; padding-bottom: 80px;
```
`.open` → `transform: translateX(0)`

### Header (`.detail-header`)
```
position: sticky; top: 0; z-index: 10;
display: flex; align-items: center; gap: 16px;
padding: calc(16px + env(safe-area-inset-top)) 40px 16px;
background: rgba(15,26,54,0.7); backdrop-filter: blur(20px);
border-bottom: 1px solid rgba(79,142,247,0.12);
```
- Back button (40×40px, same btn-icon style with `<` chevron icon)
  Tapping closes the panel.
- Habit name text (15px, font-weight: 500)

### Hero Card (`.detail-hero`)
```
padding: 40px; text-align: center;
background: rgba(15,26,54,0.7);
margin: 40px; border-radius: 24px;
border: 1px solid rgba(79,142,247,0.12);
```

Contents:
- Emoji: `font-size: 56px; margin-bottom: 16px`
- Habit name: Bebas Neue, 36px, letter-spacing: 1px
- Type + since text: `font-size: 13px; color: #4a5980; letter-spacing: 0.1em`
  Example: "COUNT UP · SINCE JAN 1, 2025"
- Big number (day count): Bebas Neue, 100px, color: habit's accent color
- "DAYS STRONG" label below it: `font-size: 13px; letter-spacing: 0.2em; text-transform: uppercase; color: #4a5980`

### Action Buttons Row (`.detail-actions`)
```
display: flex; gap: 8px; margin: 0 40px 40px;
```
Three buttons equal width:

**Set Default button** (`.btn-set-default`):
`background: rgba(79,142,247,0.12); border: 1px solid rgba(79,142,247,0.4); color: #4f8ef7;`
Text: "⭐ Default" if this habit is already default, "☆ Set Default" otherwise.
If already default, `opacity: 0.6`
Action: `setAsDefault()` — sets this habit as the default and updates home timer.

**Edit button** (`.btn-edit`):
`background: rgba(255,255,255,0.04); border: 1px solid rgba(79,142,247,0.12); color: #8aa0cc;`
Text: "✏️ Edit"
Action: closes panel, after 350ms delay opens edit modal for this habit.

**Delete button** (`.btn-delete`):
`background: rgba(255,79,109,0.1); border: 1px solid rgba(255,79,109,0.3); color: #ff4f6d;`
Text: "🗑️"
Action: opens confirm dialog "Delete Habit?" → if confirmed, deletes habit + history + default fallback.

### Mini Stats Row
Same `.stats-row` style.
Cards: "Current" streak / "Best" (longest) streak / "Total" days tracked.

---

## DEFAULT HABIT PICKER MODAL

Triggered from Settings → Default Habit row.
A slide-up sheet listing all habits with radio-style selection.
Structure mirrors the Create Modal (backdrop + sliding sheet).
Each habit listed as a row with its emoji, name, and a checkmark if selected.
Tapping a row calls `setDefaultHabit(id)` and closes the picker.

---

## CONFIRM DIALOG

A centered modal that appears over a blurred backdrop for destructive actions.

```
position: fixed; inset: 0; z-index: 600;
display: flex; align-items: center; justify-content: center; padding: 40px;
background: rgba(6,11,24,0.85); backdrop-filter: blur(8px);
opacity: 0; pointer-events: none; transition: opacity 250ms;
```
`.open` → `opacity: 1; pointer-events: all`

Inner box (`.confirm-box`):
```
background: #0f1a36; border: 1px solid rgba(79,142,247,0.12);
border-radius: 24px; padding: 40px; max-width: 320px; width: 100%;
text-align: center;
transform: scale(0.9); transition: transform 500ms cubic-bezier(0.34,1.56,0.64,1);
```
`.open .confirm-box` → `transform: scale(1)`

Contents:
- Emoji icon (40px)
- Title (Bebas Neue, 26px)
- Body text (14px, color: #8aa0cc, line-height: 1.6)
- Two buttons side by side: "Cancel" (btn-ghost) and "Confirm" (btn-danger)

---

## TOAST NOTIFICATIONS

A stack of toast messages at the top center of the screen.

Container:
```
position: fixed; top: calc(24px + env(safe-area-inset-top));
left: 50%; transform: translateX(-50%); z-index: 1000;
display: flex; flex-direction: column; gap: 8px;
min-width: 280px;
```

Each toast:
```
background: #162040; border: 1px solid rgba(79,142,247,0.12);
border-radius: 16px; padding: 14px 20px; font-size: 14px; font-weight: 500;
display: flex; align-items: center; gap: 10px;
animation: toastIn 0.4s cubic-bezier(0.34,1.56,0.64,1);
backdrop-filter: blur(12px);
```

Type variants:
- `.success` → `border-color: rgba(45,216,123,0.4)` + "✅" icon
- `.error` → `border-color: rgba(255,79,109,0.4)` + "❌" icon
- `.info` → `border-color: rgba(79,142,247,0.4)` + "ℹ️" icon

Animation in:
```css
@keyframes toastIn {
  from { opacity:0; transform:translateY(-20px) scale(0.92); }
  to   { opacity:1; transform:translateY(0) scale(1); }
}
```
Auto-dismiss: each toast is removed after 2800ms with `toastOut` animation.

---

## MILESTONE CELEBRATION OVERLAY

Shown when the current habit's day count hits: 1, 7, 14, 21, 30, 50, 100, 365.

```
position: fixed; inset: 0; z-index: 800;
background: rgba(6,11,24,0.9); backdrop-filter: blur(8px);
display: flex; align-items: center; justify-content: center;
opacity: 0; pointer-events: none;
```
`.open` → `opacity: 1; pointer-events: all`

Content (`.milestone-content`), animated with:
```css
@keyframes milestoneIn {
  from { transform: scale(0.7); opacity: 0; }
  to   { transform: scale(1); opacity: 1; }
}
```
- Emoji (80px) — uses the habit's emoji
- Day count: Bebas Neue, 120px, color: #4f8ef7, text-shadow glow
- "DAYS" label: Bebas Neue, 32px, letter-spacing: 4px, color: #8aa0cc
- Message text: 16px, color: #8aa0cc, line-height: 1.6
  Messages vary by day count:
  - 1 day: "The journey of 1,000 miles begins with a single step. ✨"
  - 7 days: "One week strong! The first week is the hardest. 💪"
  - 14 days: "Two weeks! Research shows habits start forming now. 🧠"
  - 21 days: "Three weeks! Almost a solid habit now. 🔥"
  - 30 days: "One month! You have officially built a new habit. 🏆"
  - 50 days: "Fifty days of pure dedication. Legendary. ⚡"
  - 100 days: "100 days. One hundred. You are unstoppable. 🚀"
  - 365 days: "A full year. You changed your life. 🌟"
- "Continue →" button (btn-primary, max-width: 220px, centered)

### Confetti
Behind the overlay (z-index: 799), 60 falling particles:
```
.confetti-piece {
  position: absolute; top: -20px;
  width: 8px; height: 8px;
  border-radius: 2px;
  animation: confettiFall linear infinite;
}
@keyframes confettiFall {
  0%   { transform: translateY(-20px) rotate(0deg);   opacity: 1; }
  100% { transform: translateY(110vh) rotate(720deg); opacity: 0; }
}
```
Each piece gets random: left% (0-100), color (from accent palette), duration (2–5s), delay (-5 to 0s).

---

## JAVASCRIPT LOGIC

### State Object
```javascript
let state = {
  habits: [],          // array of habit objects
  settings: {
    defaultHabitId: null,
    theme: 'dark',
    notifications: false
  },
  history: {}          // { [habitId]: { 'YYYY-MM-DD': 'success' | 'fail' } }
};
```

### Persistence (localStorage)
- Key: `'dayone_v1'`
- Save: `localStorage.setItem('dayone_v1', JSON.stringify(state))`
- Load: `JSON.parse(localStorage.getItem('dayone_v1'))`
- Load is called once on `init()`. Every mutation calls `saveState()`.

### Habit Object Shape
```javascript
{
  id:        String,       // uid() — timestamp36 + random5
  name:      String,       // max 40 chars
  emoji:     String,       // single emoji character
  color:     String,       // hex color e.g. '#4f8ef7'
  countType: 'up'|'down',  // count up from start / countdown to target
  startDate: ISOString,    // exact ISO datetime string (local midnight or exact now)
  targetDate: ISOString|null, // only for countdown type
  createdAt: Number        // Date.now() timestamp
}
```

### uid() Function
```javascript
function uid() {
  return Date.now().toString(36) + Math.random().toString(36).slice(2, 7);
}
```

### getElapsed(habit)
Computes time since `habit.startDate` (or time remaining to `targetDate` for countdown).
Returns: `{ days, hours, mins, secs, totalSecs, isNegative }`
```javascript
function getElapsed(habit) {
  const now   = Date.now();
  const start = new Date(habit.startDate).getTime();
  const diff  = now - start;  // for count-up; negate for countdown
  const totalSecs = Math.floor(Math.abs(diff) / 1000);
  const days  = Math.floor(totalSecs / 86400);
  const hours = Math.floor((totalSecs % 86400) / 3600);
  const mins  = Math.floor((totalSecs % 3600) / 60);
  const secs  = totalSecs % 60;
  return { days, hours, mins, secs, totalSecs, isNegative: diff < 0 };
}
```

### Timer Loop (startTimer)
`setInterval(() => updateHomeTimer(), 1000)` — runs every second.
`updateHomeTimer()`:
1. Get default habit. If none, show zeros.
2. Call `getElapsed(habit)`.
3. Update DOM elements for days, hours, mins, secs.
4. Add `.tick` class to changed units (remove after 300ms).
5. Update ring SVG: `strokeDashoffset = 800 - (800 * (totalSecs % 86400) / 86400)` (ring fills over 24h).
6. If `days` changed from previous: animate days number + check milestones.
7. For countdown: update `.timer-days-label` to "REMAINING" and show negative handling.

### Milestone Check
On each day change, if `days` is in `[1, 7, 14, 21, 30, 50, 100, 365]`:
1. Show milestone chip at bottom of timer.
2. Open milestone overlay with correct emoji, day count, and message.
3. Start confetti particles.

### resolveStartDate(str)
```javascript
function resolveStartDate(str) {
  const today = todayStr();
  if (str === today) return new Date().toISOString();  // exact now → timer starts at 0
  return parseDateLocal(str).toISOString();             // past date → local midnight
}
```

### parseDateLocal(str)
```javascript
function parseDateLocal(str) {
  const [y, m, d] = str.split('-').map(Number);
  return new Date(y, m - 1, d);  // local midnight, not UTC midnight
}
```

### Contribution Grid Rendering
Called each time the dashboard is shown.
- Determine today's date as local date string.
- Go back 16 weeks (112 days) from today.
- Build week columns from oldest to newest, each with 7 day cells.
- For each cell: check `state.history` across ALL habits for that date.
  Count how many habits had `'success'` on that day.
  - 0 habits with success and no fails → empty (bg-elevated)
  - Any fail → `.fail`
  - 1 success → `.level-1`
  - 2 successes → `.level-2`
  - 3 successes → `.level-3`
  - 4+ successes → `.level-4`

### Calendar Rendering (History Screen)
`calYear` and `calMonth` track the currently viewed month.
Build a 7-column grid for the viewed month:
1. First day of month — determine its day-of-week (0=Sun), add `.empty` cells before it.
2. Each day of month: check `state.history[activeHabitId][dateStr]`.
3. Apply `.success`, `.fail`, `.today` (if date === todayStr()) classes.

### Export Data
```javascript
function exportData() {
  const blob = new Blob([JSON.stringify(state, null, 2)], {type:'application/json'});
  const url  = URL.createObjectURL(blob);
  const a    = document.createElement('a');
  a.href     = url;
  a.download = `dayone-backup-${todayStr()}.json`;
  a.click();
  URL.revokeObjectURL(url);
}
```
**This requires an Android bridge** — see Android-Specific Requirements below.

### Import Data
```javascript
function importData(input) {
  const file = input.files[0];
  if (!file) return;
  const reader = new FileReader();
  reader.onload = e => {
    try {
      const imported = JSON.parse(e.target.result);
      // Validate shape before applying
      state = imported;
      saveState();
      // Re-render all screens
    } catch { showToast('Invalid backup file.', 'error'); }
  };
  reader.readAsText(file);
}
```
**The hidden `<input type="file">` must work on Android** — see Android-Specific Requirements.

### Notifications (scheduleDailyNotifs)
Schedules a daily notification at 6:00 PM for the active habit's streak.
Uses browser `Notification` API — **must be replaced with an Android bridge**.

### Clear All Data
```javascript
function clearAllData() {
  openConfirm('🗑️', 'Clear All Data?',
    'This will permanently delete all your habits and history.',
    () => {
      state = { habits: [], settings: { defaultHabitId: null, theme: state.settings.theme, notifications: false }, history: {} };
      saveState();
      renderDashboard();
      updateHomeTimer();
      showToast('All data cleared.', 'info');
    });
}
```

### Theme Cycling
```javascript
function cycleTheme() {
  state.settings.theme = state.settings.theme === 'dark' ? 'light' : 'dark';
  saveState();
  applyTheme();
}
function applyTheme() {
  document.documentElement.setAttribute('data-theme', state.settings.theme);
  document.getElementById('themeLabel').textContent = state.settings.theme === 'dark' ? 'Dark' : 'Light';
}
```

### Navigation
```javascript
function navigateTo(screenId) {
  document.querySelectorAll('.screen').forEach(s => s.classList.remove('active'));
  document.getElementById('screen-' + screenId).classList.add('active');
  document.querySelectorAll('.nav-item').forEach(n => {
    n.classList.toggle('active', n.dataset.screen === screenId);
  });
  if (screenId === 'dashboard') renderDashboard();
  if (screenId === 'history')   renderHistory();
  if (screenId === 'settings')  renderSettings();
}
```

---

## ANDROID-SPECIFIC REQUIREMENTS

### 1. AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
    android:maxSdkVersion="28" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32" />

<activity
    android:name=".MainActivity"
    android:windowSoftInputMode="adjustResize"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

### 2. Full-Screen Immersive Mode
The WebView must fill the screen including behind the status bar:
```kotlin
window.setFlags(
    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
)
WindowCompat.setDecorFitsSystemWindows(window, false)
// Make status bar transparent
window.statusBarColor = Color.TRANSPARENT
window.navigationBarColor = Color.TRANSPARENT
```

The app's own fixed bottom nav handles safe area spacing via
`env(safe-area-inset-bottom)` which WebView supports automatically.

### 3. Back Button Handling
```kotlin
override fun onBackPressed() {
    val handled = webView.evaluateJavascript(
        """
        (function() {
          if (document.getElementById('milestoneOverlay').classList.contains('open')) {
            closeMilestone(); return true;
          }
          if (document.getElementById('confirmDialog').classList.contains('open')) {
            closeConfirm(); return true;
          }
          if (document.getElementById('createModal').classList.contains('open')) {
            closeModal(); return true;
          }
          if (document.getElementById('habitDetailPanel').classList.contains('open')) {
            closeHabitDetail(); return true;
          }
          if (document.getElementById('defaultPickerModal').classList.contains('open')) {
            closeDefaultPicker(); return true;
          }
          if (document.getElementById('resetPanel').classList.contains('open')) {
            closeResetPanel(); return true;
          }
          return false;
        })()
        """.trimIndent()
    ) { result ->
        if (result != "true") {
            // Nothing was open — let default back behavior happen
            super.onBackPressed()
        }
    }
}
```

### 4. File Export (Android Bridge)
Browser `Blob` + `URL.createObjectURL()` downloads don't work reliably in WebView.
Create an `AndroidBridge` class and inject it:

```kotlin
class AndroidBridge(private val activity: MainActivity) {
    @JavascriptInterface
    fun saveFile(filename: String, content: String) {
        activity.runOnUiThread {
            // Use MediaStore or Downloads folder on API 29+
            val resolver = activity.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, filename)
                put(MediaStore.Downloads.MIME_TYPE, "application/json")
            }
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                resolver.openOutputStream(it)?.use { stream ->
                    stream.write(content.toByteArray())
                }
                activity.showToast("Exported to Downloads/$filename")
            }
        }
    }
}

// In Activity setup:
webView.addJavascriptInterface(AndroidBridge(this), "Android")
```

In the HTML, replace `exportData()` with:
```javascript
function exportData() {
  const json = JSON.stringify(state, null, 2);
  const filename = 'dayone-backup-' + todayStr() + '.json';
  if (typeof Android !== 'undefined') {
    Android.saveFile(filename, json);
  } else {
    // fallback: browser download
    const blob = new Blob([json], {type: 'application/json'});
    const url  = URL.createObjectURL(blob);
    const a    = document.createElement('a');
    a.href = url; a.download = filename; a.click();
    URL.revokeObjectURL(url);
  }
}
```

### 5. File Import (Android Bridge)
WebView's `<input type="file">` requires a custom `WebChromeClient`:

```kotlin
webView.webChromeClient = object : WebChromeClient() {
    override fun onShowFileChooser(
        webView: WebView,
        filePathCallback: ValueCallback<Array<Uri>>,
        fileChooserParams: FileChooserParams
    ): Boolean {
        this@MainActivity.filePathCallback = filePathCallback
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/json"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, FILE_CHOOSER_REQUEST)
        return true
    }
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == FILE_CHOOSER_REQUEST) {
        val result = if (resultCode == Activity.RESULT_OK) {
            data?.data?.let { arrayOf(it) } ?: arrayOf()
        } else arrayOf()
        filePathCallback?.onReceiveValue(result)
        filePathCallback = null
    }
}
```

### 6. Notifications (Android Bridge)
Replace the browser `Notification` API with Android notifications:

```kotlin
@JavascriptInterface
fun scheduleNotification(title: String, body: String, delayMs: Long) {
    activity.runOnUiThread {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val notifManager = activity.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            val channel = NotificationChannel(
                "dayone_daily", "Daily Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notifManager.createNotificationChannel(channel)
            
            val notification = NotificationCompat.Builder(activity, "dayone_daily")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .build()
            notifManager.notify(1, notification)
        }, delayMs)
    }
}

@JavascriptInterface
fun requestNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        activity.requestPermissions(
            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100
        )
    }
}
```

In the HTML, replace `scheduleDailyNotifs()` to use `Android.scheduleNotification()` and
`Android.requestNotificationPermission()` when the `Android` object is available.

### 7. LocalStorage Persistence
`domStorageEnabled = true` makes WebView localStorage persist across app restarts.
The storage path is tied to the app's `webView` origin (`file:///`).
No additional code needed — localStorage just works.

### 8. Orientation Lock (Optional but Recommended)
Add to the `<activity>` tag in the Manifest:
```xml
android:screenOrientation="portrait"
```

### 9. WebView Client (handle page load)
```kotlin
webView.webViewClient = object : WebViewClient() {
    override fun onPageFinished(view: WebView, url: String) {
        // Page loaded — could inject any post-load JS here if needed
    }
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        // Keep all navigation inside WebView
        return false
    }
}
```

---

## HTML FIXES REQUIRED FOR WEBVIEW COMPATIBILITY

Apply these changes to `dayone.html` before placing it in assets:

1. **Remove Google Fonts link** — replace with local `@font-face` rules (see Font Loading Fix).

2. **Remove seedSampleData call** in `init()` — replace with nothing.

3. **Remove the seedSampleData function entirely** (lines defining `function seedSampleData() {...}`).

4. **Fix exportData()** — add Android bridge path (see Android Bridge section).

5. **Fix scheduleDailyNotifs()** — add Android bridge path for notifications.

6. **Add safe-area meta tag** if not present:
   ```html
   <meta name="viewport" content="width=device-width, initial-scale=1.0, viewport-fit=cover">
   ```
   Note: `viewport-fit=cover` is already in the source.

7. **body padding fix** for Android status bar:
   ```css
   body { padding-top: env(safe-area-inset-top); }
   ```

8. **Dark status bar icons** (since app background is dark):
   In Kotlin, after WebView is set up:
   ```kotlin
   WindowInsetsControllerCompat(window, window.decorView)
       .isAppearanceLightStatusBars = false
   ```

---

## RESPONSIVE RULES

At screen width ≤ 420px:
- Home header padding reduces from 40px to 24px
- Timer days font: `clamp(80px, 22vw, 120px)` (slightly smaller)
- Timer ring: 240×240px (instead of 280×280px)
- All screen margins reduce from 40px to 16px

---

## BUILD OUTPUT REQUESTED

Please output all of the following:

1. **Complete Android Studio project** with correct directory structure
2. **`app/src/main/java/.../MainActivity.kt`** — full Kotlin source
3. **`app/src/main/AndroidManifest.xml`** — with all permissions and activity config
4. **`app/src/main/res/layout/activity_main.xml`** — WebView filling the screen
5. **`app/build.gradle`** — with correct dependencies (appcompat, webkit)
6. **`app/src/main/assets/dayone.html`** — the HTML with all fixes applied
7. **Asset folder structure diagram** showing where fonts go
8. **Step-by-step APK build instructions** in Android Studio

---

## WHAT NOT TO DO

- ❌ Do not replace the UI with Jetpack Compose or Material Design components
- ❌ Do not simplify or remove any screen, modal, button, or behavior
- ❌ Do not add a demo/sample habit or any pre-seeded data
- ❌ Do not start the timer before the user creates their first habit
- ❌ Do not change any color, spacing, or typography
- ❌ Do not add a splash screen or onboarding that isn't in the original HTML
- ❌ Do not load the HTML from a remote URL — it must load from app assets
- ❌ Do not break the glassmorphism (backdrop-filter: blur) effects
- ❌ Do not break the animated background orbs or mesh gradient

---

## FINAL CHECKLIST

Before the APK is considered complete, verify:

- [ ] App opens to the Home/Timer screen with 0 days shown
- [ ] No habits exist on first launch
- [ ] Tapping FAB on Habits tab opens the Create Habit modal
- [ ] Creating a habit adds it to the list and starts the timer
- [ ] Habit card tap opens the Habit Detail panel from the right
- [ ] Edit → modal opens pre-filled with habit data
- [ ] Delete → confirm dialog → habit removed
- [ ] History screen calendar correctly shows success/fail days
- [ ] Settings → Theme toggle switches dark/light correctly
- [ ] Settings → Export creates a downloadable JSON file
- [ ] Settings → Import reads a JSON file and restores state
- [ ] Settings → Clear All wipes everything and returns to empty state
- [ ] Toast messages appear at the top center
- [ ] Milestone overlay appears at day 1, 7, 14, 21, 30, 50, 100, 365
- [ ] Confetti falls behind milestone overlay
- [ ] Android back button closes modals before exiting
- [ ] Fonts render correctly (Bebas Neue, DM Sans, DM Mono)
- [ ] Background orbs and gradient visible
- [ ] Glassmorphism blur effects working
- [ ] App works completely offline (no network needed)
- [ ] Data persists after closing and reopening the app
