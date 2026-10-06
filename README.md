# Parabellum

> *"A minimalist personal task manager and temporal Pomodoro focus system styled after the strange, sterile, retro-futuristic corporate computer interfaces of Lumon Industries."*

---

## Overview

**Parabellum** is a native Android application built using Jetpack Compose, Room, Android DataStore, and Jetpack Glance. It combines an offline-first work queue task manager with a secondary Pomodoro focus timer, designed with a distinct visual language heavily inspired by retro corporate terminal software (e.g., *Severance* Macrodata Refinement terminals).

---

## Visual Design Identity

* **Sterile & Corporate**: Off-white / cream surfaces (`#F4F1EA`), dark green (`#1B4D3E`), muted teal (`#2E5A52`), and charcoal typography.
* **Technical Monospace Typography**: Monospaced labels, section codes (`01 // IMMEDIATE`), status badges (`SYS-OK`), and timestamp logs.
* **Rectangular Geometry**: Crisp 1dp thin borders, flat layered panels, square checkboxes, and restrained negative space without rounded consumer cards or gradients.
* **Restrained Retro Motion**: A corporate transfer animation that highlights completed tasks and moves them into a bottom "Archive Deposit" panel.

---

## App Features & Structure

### 1. Work Queue (Tasks)
The primary screen features 5 vertically ordered independent sections:
1. **01 // IMMEDIATE**: Critical queue requiring immediate action.
2. **02 // SOON**: Priority 2 pending items.
3. **03 // LATER**: Deferred queue for future processing.
4. **04 // EXTRAS**: Supplemental non-critical tasks.
5. **05 // MAYBE**: Speculative / conditional items.

* **Add Task**: Clean retro dialog (`+ ADD`) for instant inline data entry into any section.
* **Task Completion**: Checking a task triggers a retro deposit animation transferring the item to the permanent archive.

### 2. Work History (Archive)
* Permanently stores all completed tasks grouped by date (e.g., `OCTOBER 6, 2026`).
* Displays completion timestamps, original section tags, and a **Restore** function to return tasks to the active Work Queue.
* Includes a history clearing option with confirmation safeguard.

### 3. Temporal Focus Controller (Pomodoro)
* **Standard 25 / 5 / 15 Cycle**:
  * 25-minute Focus Session
  * 5-minute Short Break
  * 15-minute Long Break after 4 completed focus sessions
* Large monospace countdown timer with ASCII-style progress bar and session tracker badges.
* Interactive controls: Start, Pause, Resume, Reset, and Skip.

### 4. System Settings
* **Theme Options**: Default Parabellum (Cream), Clinical Light, Terminal Dark.
* **Accent Colors**: Lumon Green, Muted Teal, Corporate Blue, Terminal Amber, Alert Red.
* **UI Customization**: Motion intensity (Normal / Minimal) and Compact Task Density toggles.
* **Pomodoro Customization**: Adjustable Focus duration, Short break, Long break, and session counts.
* **Preferences Persistence**: Saved locally via Android DataStore Preferences.

### 5. Home Screen Widget
* Built with **Jetpack Glance**.
* Displays real-time counts of Immediate tasks and Total Open tasks.
* Tapping the widget launches the Parabellum Work Queue instantly.
* Completely offline and lightweight.

---

## Technology Stack & Architecture

* **Language**: Kotlin 2.0+
* **UI Framework**: Jetpack Compose with Material 3 customization
* **Local Database**: Room DB for task persistence
* **Preferences**: Android DataStore Preferences
* **Widget Framework**: Jetpack Glance AppWidget
* **Architecture**: Unidirectional Data Flow (UI ← ViewModel ← Repository ← Room / DataStore)

```
com.parabellum.app
├── data
│   ├── database      (TaskEntity, TaskDao, ParabellumDatabase, Converters)
│   ├── datastore     (SettingsRepository)
│   └── repository    (TaskRepository)
├── model             (TaskSection, TaskEntity, UserSettings, PomodoroState)
├── ui
│   ├── components    (ParabellumPanel, RetroHeaderBar, RetroButton, DepositContainerPanel)
│   ├── screens       (TasksScreen, ArchiveScreen, PomodoroScreen, SettingsScreen)
│   └── theme         (Color, Type, Shape, Theme)
├── viewmodel         (TasksViewModel, ArchiveViewModel, PomodoroViewModel, SettingsViewModel)
├── navigation        (ParabellumNavGraph, Screen)
└── widget            (ParabellumWidget, ParabellumWidgetReceiver)
```

---

## How to Build the Debug APK

### Prerequisites
* JDK 17+ or JDK 21+
* Android SDK Platform 35 / Build-Tools 34+

### Build Command

**On Linux / macOS:**
```bash
./gradlew assembleDebug
```

**On Windows (PowerShell / Command Prompt):**
```powershell
.\gradlew.bat assembleDebug
```

### APK Build Output Path
Upon successful compilation, the generated APK will be located at:
```
app/build/outputs/apk/debug/app-debug.apk
```
