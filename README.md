# DayOne

DayOne is an Android habit/streak app built with Kotlin and Gradle. The app wraps the DayOne HTML experience in an Android WebView and includes native Android support for notifications and home-screen widgets.

## Requirements

- JDK 17
- Android SDK with API 35 installed
- Android SDK Build Tools 35.0.0
- An emulator or Android device for installation

## Local Setup

Create `local.properties` in the repo root with your Android SDK path:

```properties
sdk.dir=/path/to/Android/Sdk
```

This file is intentionally ignored because it is machine-specific.

## Build

Use the included Gradle wrapper:

```bash
./gradlew clean assembleDebug
```

The debug APK is generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```
If you want you can file the apk in the releases
```text
https://github.com/Abr-ahamis/dayone/releases
```
## Install

With a device or emulator connected:

```bash
./gradlew installDebug
```

You can also run tests/checks with:

```bash
./gradlew test
./gradlew lint
```

## Project Layout

```text
app/src/main/java/com/dayone/     Kotlin application source
app/src/main/assets/              WebView HTML and local fonts
app/src/main/res/                 Android resources, layouts, widgets, icon
sor/                              Original source/prototype HTML and notes
gradle/wrapper/                   Gradle wrapper files required to build
```

