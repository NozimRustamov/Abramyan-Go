# CLAUDE.md

## Project Overview

**Abramyan-Go** is a Kotlin Multiplatform (KMP) gamified programming education app based on M.E. Abramyan's "1000 Tasks in Programming" textbook. Players solve coding tasks through RPG-themed mechanics.

## Tech Stack

- **Kotlin Multiplatform** (KMP) with **Compose Multiplatform** 1.7.1
- **Kotlin** 2.0.21, **AGP** 8.13.2
- **kotlinx.serialization** 1.7.3 for JSON
- **Koin** 4.0.0 for DI
- **SQLDelight** 2.0.2 for local DB
- **Navigation Compose** 2.8.0-alpha10
- **Coroutines** 1.9.0

## Module Structure

```
shared/          — Domain models, repositories, use cases, DI, DB, JSON data sources
composeApp/      — Compose Multiplatform UI (screens, mechanics, components, theme)
androidApp/      — Android app entry point
iosApp/          — iOS app entry point
```

### Key Packages

- `shared/.../domain/model/` — Task, Player, World, Achievement models
- `shared/.../domain/repository/` — Repository interfaces
- `shared/.../data/json/` — TasksDataSource (parses JSON), WorldsDataSource
- `shared/.../data/repository/` — Repository implementations (SQLDelight + JSON)
- `shared/.../core/di/` — Koin `sharedModule` (DB, data sources, repos, use cases)
- `composeApp/.../ui/di/` — Koin `uiModule` (ViewModels, TaskRepository with JSON loader)
- `composeApp/.../ui/screens/task/` — TaskScreen, TaskViewModel, mechanics/
- `composeApp/.../ui/screens/task/mechanics/` — DragDropMechanic, BugHuntMechanic, FillBlankMechanic
- `composeApp/.../ui/components/` — Reusable UI (GlassCard, CodeBlock, PrimaryButton, etc.)
- `composeApp/.../ui/theme/` — AppTheme, colors, typography, Spacing

## Architecture

- **MVI pattern**: State, Intent, SideEffect per screen
- **ViewModels** extend `ViewModel` with `StateFlow<State>` + `SharedFlow<SideEffect>`
- **Repository pattern**: interfaces in `domain/repository/`, implementations in `data/repository/`
- **JSON as single source of truth** for task data

## Task Data System

Tasks are loaded from JSON files in `composeApp/src/commonMain/composeResources/files/`.

### JSON file format

```json
{
  "tasks": [
    {
      "id": "Begin1",
      "abramyan_text": "Begin 1. Описание задачи...",
      "rpg_context": "NPC: «Реплика персонажа»",
      "mechanic": "DragDrop",       // DragDrop | BugHunt | FillBlank
      "task_data": { ... }          // depends on mechanic
    }
  ],
  "world_id": 1,
  "world_name": "Долина Начинаний"
}
```

### Mechanic-specific task_data

**DragDrop**: `blocks` (list of `{id, code}`), `correct_order` (list of block IDs)
**BugHunt**: `code_with_bug`, `bug_line_index`, `bug_options`, `correct_option`
**FillBlank**: `code_template` (with `___`), `blank_options`, `correct_option`

All task_data objects include `language: "kotlin"`.

### Adding new task files

1. Create JSON file (e.g., `integer 1-40.json`) following the format above
2. Place in `composeApp/src/commonMain/composeResources/files/`
3. Add loading line in `composeApp/.../ui/di/UiModule.kt`:
   ```kotlin
   Res.readBytes("files/integer 1-40.json").decodeToString()
   ```
4. `world_id` in JSON maps to world constants in `shared/.../domain/model/World.kt` (`Worlds` object)

## Task Mechanic Enum

Only 3 mechanics: `DragDrop`, `BugHunt`, `FillBlank`. SerialName values match JSON exactly.

## Coding Conventions

- Language: **Kotlin** only (no Python/JS/C# in task code)
- Serialization: use `@SerialName` for JSON field names (snake_case in JSON, camelCase in Kotlin)
- UI: Compose Multiplatform with custom `AppTheme`, `GlassCard`, `GlassSurface` components
- DI: Koin — `sharedModule` in shared, `uiModule` in composeApp, `platformModule` as expect/actual
- DB: SQLDelight with `.sq` files in `shared/src/commonMain/sqldelight/`
- No hardcoded task data — all tasks come from JSON files

## Build & Run

```bash
# Android
./gradlew :androidApp:assembleDebug

# Desktop (if configured)
./gradlew :composeApp:run
```

## Important Files

| File | Purpose |
|------|---------|
| `shared/.../domain/model/Task.kt` | Task, TaskData, TaskMechanic, TasksFileJson models |
| `shared/.../data/json/TasksDataSource.kt` | Parses JSON, stores tasks in memory |
| `shared/.../data/repository/TaskRepositoryImpl.kt` | Lazy JSON loading via jsonLoader lambda |
| `composeApp/.../ui/di/UiModule.kt` | Creates TaskRepository with compose resource loader |
| `composeApp/.../ui/screens/task/TaskViewModel.kt` | MVI ViewModel, checkAnswer logic for all mechanics |
| `composeApp/.../ui/screens/task/TaskScreen.kt` | Task UI: title, rpg context, mechanic zone |
| `composeApp/.../ui/screens/task/mechanics/` | DragDrop, BugHunt, FillBlank composables |
| `composeApp/src/commonMain/composeResources/files/` | JSON task files (begin 1-40.json, etc.) |
