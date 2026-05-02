# Абрамян Go

Мобильное приложение для просмотра задач и решений из задачника М.Э. Абрамяна «1000 задач по программированию». Kotlin Multiplatform — работает на Android и iOS из единой кодовой базы.

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose-1.7.1-4285F4?logo=jetpackcompose)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20iOS-green)]()

## Что делает приложение

Три экрана:

1. **Категории** — список разделов задачника (Begin, Integer, Boolean, If, Case, For, While, Series, Proc)
2. **Задачи категории** — список задач выбранного раздела с кратким условием
3. **Детали задачи** — полное условие и решения на нескольких языках (Java, C#, JavaScript, Python) с возможностью раскрыть/скрыть каждое

## Стек

| | |
|---|---|
| UI | Compose Multiplatform, Material 3 |
| Архитектура | MVI (State / Intent / SideEffect) |
| DI | Koin 4.0 |
| Навигация | Navigation Compose, type-safe routes |
| Данные | JSON-файлы в Compose Resources |
| Async | Kotlin Coroutines, Flow |

## Структура

```
shared/          — модели данных, репозитории, парсинг JSON
composeApp/      — весь UI (экраны, компоненты, тема)
androidApp/      — точка входа Android
iosApp/          — точка входа iOS (Xcode project)
```

Данные хранятся в JSON-файлах внутри `composeApp/src/commonMain/composeResources/files/`. Один файл на категорию (`begin_1-40.json`, `integer_1-30.json` и т.д.).

## Дизайн

Тёмная тема на базе палитры Catppuccin Mocha. Карточки с тонкой границей, монospaced шрифт JetBrains Mono для кода, Inter для текста.

## Запуск

**Требования:** JDK 17+, Android Studio, Xcode 15+ (для iOS)

```bash
# Android
./gradlew :androidApp:assembleDebug

# iOS — открыть в Xcode
open iosApp/iosApp.xcodeproj
```

При билде iOS Xcode автоматически запускает Gradle для сборки `ComposeApp.framework`.

## Добавление задач

1. Положить JSON-файл в `composeApp/src/commonMain/composeResources/files/`
2. Добавить запись в `categories.json`
3. Добавить загрузку файла в `composeApp/.../ui/di/UiModule.kt`

Формат файла задач:

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
