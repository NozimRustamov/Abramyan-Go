# CLAUDE.md

## Project Overview

**Abramyan-Go** is a Kotlin Multiplatform app for browsing tasks and solutions from M.E. Abramyan's "1000 Tasks in Programming" textbook. Three screens: category list → task list → task detail with collapsible solutions.

## Tech Stack

- **Kotlin Multiplatform** (KMP) with **Compose Multiplatform** 1.7.1
- **Kotlin** 2.0.21, **AGP** 8.13.2
- **kotlinx.serialization** 1.7.3 for JSON
- **Koin** 4.0.0 for DI
- **Navigation Compose** 2.8.0-alpha10
- **Coroutines** 1.9.0

## Module Structure

```
shared/          — Domain models, repository interfaces, CategoryRepositoryImpl
composeApp/      — Compose Multiplatform UI (screens, components, theme)
androidApp/      — Android app entry point (MainActivity + AbramyanGoApplication)
iosApp/          — iOS Xcode project (iosApp.xcodeproj)
```

### Key Packages

- `shared/.../domain/model/` — `Category`, `CategoryTask`, `CategoryTasksFileJson`
- `shared/.../domain/repository/` — `CategoryRepository` interface
- `shared/.../data/repository/` — `CategoryRepositoryImpl` (parses JSON via lambdas)
- `composeApp/.../ui/di/` — Koin `uiModule` (ViewModels + CategoryRepository with Compose resource loaders)
- `composeApp/.../ui/screens/categories/` — categories list screen
- `composeApp/.../ui/screens/categorytasklist/` — task list screen
- `composeApp/.../ui/screens/taskdetail/` — task detail + solutions screen
- `composeApp/.../ui/components/` — `GlassCard`, `GlassButton`
- `composeApp/.../ui/theme/` — `AppTheme`, `AppColors` (Catppuccin Mocha), `AppTypography`, `AppShapes`, `Spacing`
- `composeApp/.../ui/navigation/` — type-safe `Route` sealed class
- `composeApp/src/iosMain/` — `MainViewController.kt` (iOS entry point)

## Architecture

- **MVI pattern**: `State`, `Intent`, `SideEffect` per screen
- **ViewModels** extend `ViewModel` with `StateFlow<State>` + `SharedFlow<SideEffect>`
- **Repository pattern**: interface in `domain/repository/`, implementation in `data/repository/`
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

1. Create `<name>_<range>.json` in `composeApp/src/commonMain/composeResources/files/`
2. Add entry to `categories.json`
3. Add loader in `composeApp/.../ui/di/UiModule.kt`:
   ```kotlin
   categoryTasksLoader = { id -> Res.readBytes("files/$id.json").decodeToString() }
   ```
   (the loader already does this dynamically by `id` — no change needed unless you add a new category loader variant)

## Theme

Catppuccin Mocha dark palette. Always dark — no light theme.

- `backgroundPrimary` = `#1E1E2E` (base)
- `glassSurface` = `#181825` (mantle) — card backgrounds
- `glassBorder` = `#313244` (surface0) — card borders
- `crust` = `#11111B` — code block background
- `accentPrimary` = `#A6E3A1` (green)
- Per-category accent colors defined in `categoryStyleFor()` in `Colors.kt`
- Per-language colors defined in `languageColor()` in `Colors.kt`

Fonts: Inter (Regular/Medium/SemiBold) for UI text, JetBrains Mono (Regular/Medium/Bold) for code. TTF files in `composeApp/src/commonMain/composeResources/font/`. Extension property imports required in `Typography.kt`.

## Coding Conventions

- Kotlin only
- Serialization: `@SerialName` for snake_case JSON fields
- UI: Compose Multiplatform with `AppTheme.colors`, `AppTheme.typography`, `AppTheme.shapes`
- DI: Koin — `uiModule` in composeApp; Android uses `AbramyanGoApplication` to start Koin, iOS calls `initKoin()` from Swift `init()`
- No hardcoded task data — all tasks from JSON files
- No SQLDelight — data is read-only, loaded into memory from JSON

## Build & Run

```bash
# Android
./gradlew :androidApp:assembleDebug

# iOS — open in Xcode (builds ComposeApp.framework via Gradle automatically)
open iosApp/iosApp.xcodeproj
```

Xcode requires full Xcode install (not just CLI tools). After install: `sudo xcode-select -s /Applications/Xcode.app/Contents/Developer`.

## Important Files

| File | Purpose |
|------|---------|
| `shared/.../domain/model/Category.kt` | `Category`, `CategoryTask`, `CategoryTasksFileJson` |
| `shared/.../data/repository/CategoryRepositoryImpl.kt` | Parses JSON via injected lambdas |
| `composeApp/.../ui/di/UiModule.kt` | Koin module — wires repository + ViewModels |
| `composeApp/.../ui/App.kt` | NavHost with 3 routes |
| `composeApp/.../ui/theme/Colors.kt` | Catppuccin palette, `categoryStyleFor()`, `languageColor()` |
| `composeApp/.../ui/theme/Typography.kt` | `AppTypography`, `AppShapes`, `Spacing` |
| `composeApp/src/iosMain/.../MainViewController.kt` | `initKoin()` + `MainViewController()` for iOS |
| `iosApp/iosApp/iOSApp.swift` | SwiftUI `@main`, calls `doInitKoin()` |
| `iosApp/iosApp/ContentView.swift` | `UIViewControllerRepresentable` wrapping KMP VC |
| `composeApp/src/commonMain/composeResources/files/` | JSON data files |
