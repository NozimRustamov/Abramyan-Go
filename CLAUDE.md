# CLAUDE.md

## Project Overview

**Abramyan-Go** is a Kotlin Multiplatform app for browsing tasks and solutions from M.E. Abramyan's "1000 Tasks in Programming" textbook. Three screens: category list → task list → task detail with collapsible solutions.

## Tech Stack

- **Kotlin Multiplatform** (KMP) with **Compose Multiplatform** 1.7.1
- **Kotlin** 2.0.21, **AGP** 8.13.2
- **kotlinx.serialization** 1.7.3 for JSON
- **Koin** 4.0.0 for DI
- **Navigation Compose** 2.8.0-alpha10

## Module Structure

```
composeApp/      — Compose Multiplatform UI + domain/data (commonMain, iosMain)
androidApp/      — Android entry (MainActivity + AbramyanGoApplication)
iosApp/          — iOS Xcode project (iosApp.xcodeproj)
```

There is **no separate `shared/` module** — all common code lives in `composeApp/src/commonMain/`.

### Package layout

All Kotlin packages live under `tj.abramyan.go`:

- `composeApp/src/commonMain/kotlin/tj/abramyan/go/data/` — `Category`, `CategoryTask`, `CategoryTasksFileJson`, `CategoryRepository`, `CategoryRepositoryImpl`
- `composeApp/src/commonMain/kotlin/tj/abramyan/go/ui/` — `App.kt` (NavHost + `Route` sealed class), `AppModule.kt` (Koin module)
- `composeApp/src/commonMain/kotlin/tj/abramyan/go/ui/screens/categories/` — categories list
- `composeApp/src/commonMain/kotlin/tj/abramyan/go/ui/screens/categorytasklist/` — task list
- `composeApp/src/commonMain/kotlin/tj/abramyan/go/ui/screens/taskdetail/` — task detail + solutions
- `composeApp/src/commonMain/kotlin/tj/abramyan/go/ui/theme/` — `Colors`, `Theme`, `Typography`
- `composeApp/src/iosMain/kotlin/tj/abramyan/go/MainViewController.kt` — iOS entry
- `androidApp/src/main/kotlin/tj/abramyan/go/` — `MainActivity`, `AbramyanGoApplication`

### Identifiers

- Android `applicationId` and `androidApp` `namespace`: `tj.abramyan.go`
- `composeApp` Android library `namespace`: `tj.abramyan.go.shared`
- Compose Resources generated `Res` package (overridden in `composeApp/build.gradle.kts`): `tj.abramyan.go.shared.resources`

## Architecture

- **MVI pattern**: `State`, `Intent`, `SideEffect` per screen
- **ViewModels** extend `androidx.lifecycle.ViewModel`, expose `StateFlow<State>` + `SharedFlow<SideEffect>`
- **Repository pattern**: interface + impl colocated in `data/Category.kt`
- **JSON as single source of truth** — no local database

## Data Model

```kotlin
data class Category(val id: String, val name: String)

data class CategoryTask(
    val id: String,
    val question: String,
    val solutions: Map<String, String> = emptyMap()   // key: language, value: code
)
```

Solution languages: `"java"`, `"csharp"`, `"javascript"`, `"python"`.

## Task Data System

JSON files live in `composeApp/src/commonMain/composeResources/files/`.

- `categories.json` — list of all categories `[{ "id": "begin_1-40", "name": "Begin" }, ...]`
- `begin_1-40.json`, `integer_1-30.json`, etc. — tasks per category

### Task file format

```json
{
  "tasks": [
    {
      "id": "Begin1",
      "question": "Условие задачи",
      "solutions": {
        "java": "...",
        "csharp": "...",
        "javascript": "...",
        "python": "..."
      }
    }
  ]
}
```

### Adding a new category

1. Create `<name>_<range>.json` in `composeApp/src/commonMain/composeResources/files/`.
2. Add an entry to `categories.json`.

The Koin module's `categoryTasksLoader` resolves the file dynamically via `Res.readBytes("files/$id.json")` — no Koin or build-time changes are needed.

## Theme

Catppuccin Mocha dark palette. Always dark — no light theme.

- `backgroundPrimary` = `#1E1E2E` (base)
- `glassSurface` = `#181825` (mantle) — card backgrounds
- `glassBorder` = `#313244` (surface0) — card borders
- `crust` = `#11111B` — code block background
- `accentPrimary` = `#A6E3A1` (green)
- Per-category accent colors: `categoryStyleFor()` in `Colors.kt`
- Per-language colors: `languageColor()` in `Colors.kt`

The theme is exposed through `AppTheme`:

```kotlin
AppTheme.colors      // AppColors  (Catppuccin palette)
AppTheme.fonts       // AppFonts   (sans = Inter, mono = JetBrains Mono)
AppTheme.typography  // AppTypography (Inter/JetBrainsMono presets)
AppTheme.shapes      // AppShapes  (rounded-corner shape tokens)
```

Fonts: Inter (Regular/Medium/SemiBold) and JetBrains Mono (Regular/Medium/Bold). TTF files in `composeApp/src/commonMain/composeResources/font/`. Loaded via `rememberAppFonts()` in `Typography.kt`.

## Coding Conventions

- Kotlin only
- Serialization: `kotlinx.serialization` with `@SerialName` for snake_case JSON fields
- UI: Compose Multiplatform; access theme via `AppTheme.colors`, `AppTheme.fonts`, `AppTheme.typography`, `AppTheme.shapes`
- DI: Koin — `appModule` in `composeApp`; Android starts Koin in `AbramyanGoApplication`, iOS calls `initKoin()` from Swift `init()`
- No hardcoded task data — all tasks from JSON files

## Build & Run

```bash
# Android
./gradlew :androidApp:assembleDebug

# iOS — open in Xcode (builds ComposeApp.framework via Gradle automatically)
open iosApp/iosApp.xcodeproj
```

Xcode requires a full Xcode install (not just CLI tools). After install: `sudo xcode-select -s /Applications/Xcode.app/Contents/Developer`.

## Important Files

| File | Purpose |
|------|---------|
| `composeApp/.../data/Category.kt` | Models + `CategoryRepository`/`CategoryRepositoryImpl` |
| `composeApp/.../ui/App.kt` | NavHost + type-safe `Route` sealed class |
| `composeApp/.../ui/AppModule.kt` | Koin `appModule` — wires repository + ViewModels |
| `composeApp/.../ui/theme/Colors.kt` | Catppuccin palette, `categoryStyleFor()`, `languageColor()` |
| `composeApp/.../ui/theme/Typography.kt` | `AppFonts`, `AppTypography`, `AppShapes` + remember helpers |
| `composeApp/.../ui/theme/Theme.kt` | `AbramyanGoTheme` + `AppTheme` accessor |
| `composeApp/src/iosMain/.../MainViewController.kt` | `initKoin()` + `MainViewController()` for iOS |
| `iosApp/iosApp/iOSApp.swift` | SwiftUI `@main`, calls `doInitKoin()` |
| `iosApp/iosApp/ContentView.swift` | `UIViewControllerRepresentable` wrapping the KMP VC |
| `composeApp/src/commonMain/composeResources/files/` | JSON data files |
| `androidApp/proguard-rules.pro` | R8 keep rules for serialization + nav routes |
