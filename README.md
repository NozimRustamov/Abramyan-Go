# 🎮 Абрамян Go

> Gamified RPG-приложение для изучения программирования на основе задачника М.Э. Абрамяна "1000 задач по программированию"

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose-1.7.0-4285F4?logo=jetpackcompose)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20iOS-green)]()
[![License](https://img.shields.io/badge/License-MIT-blue)]()

<p align="center">
  <img src="docs/preview.png" alt="Абрамян Go Preview" width="300"/>
</p>

## 📖 О проекте

**Абрамян Go** превращает классический задачник по программированию в увлекательное RPG-приключение. Вместо написания кода пользователь решает задачи через интерактивные механики: собирает код из блоков, заполняет пропуски, ищет ошибки и трассирует выполнение программ.

### ✨ Особенности

- 🌍 **9 тематических миров** — от базовых переменных до рекурсии
- 🎯 **6 игровых механик** — разнообразные способы взаимодействия с кодом
- 🐍 **4 языка программирования** — Python, JavaScript, Kotlin, C#
- 🔥 **Система прогрессии** — XP, ранги, стрики, комбо
- 💎 **Liquid Glass дизайн** — современный glassmorphism интерфейс
- 📱 **Кроссплатформенность** — Android и iOS из единой кодовой базы

## 🗺️ Миры

| # | Мир | Темы |
|---|-----|------|
| 1 | 🌱 Долина Начинаний | Begin, Integer |
| 2 | ⚖️ Поля Истины | Boolean |
| 3 | 🔀 Развилка Судьбы | If, Case |
| 4 | 🔄 Петля Времени | For, While |
| 5 | 🌊 Река Данных | Series |
| 6 | 🌊 Океан Массивов | Array |
| 7 | 🏰 Цитадель Матриц | Matrix |
| 8 | 🌲 Текстовый Лес | String |
| 9 | 📚 Архивы Архитектора | Proc, File, Recursion |

## 🎮 Механики

| Механика | Описание |
|----------|----------|
| **Drag & Drop** | Собери код из перемешанных блоков |
| **Fill Blanks** | Заполни пропуски в коде |
| **Bug Hunt** | Найди строку с ошибкой |
| **Code Trace** | Трассируй выполнение программы |
| **Output Prediction** | Предскажи вывод программы |
| **Refactoring** | Выбери лучший вариант кода |

## 🏗️ Архитектура

```
┌─────────────────────────────────────────────────────────┐
│                      UI Layer                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │   Screens   │  │  Components │  │   Theme     │     │
│  │  (Compose)  │  │ (Glass UI)  │  │(Liquid Glass)│    │
│  └─────────────┘  └─────────────┘  └─────────────┘     │
│                         │                               │
│  ┌─────────────────────────────────────────────────┐   │
│  │              ViewModels (MVI)                    │   │
│  │         State / Intent / SideEffect              │   │
│  └─────────────────────────────────────────────────┘   │
├─────────────────────────────────────────────────────────┤
│                    Domain Layer                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │   Models    │  │  Use Cases  │  │ Repositories│     │
│  │             │  │             │  │ (interfaces)│     │
│  └─────────────┘  └─────────────┘  └─────────────┘     │
├─────────────────────────────────────────────────────────┤
│                     Data Layer                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │  SQLDelight │  │    JSON     │  │ Repositories│     │
│  │   Database  │  │   Assets    │  │   (impl)    │     │
│  └─────────────┘  └─────────────┘  └─────────────┘     │
└─────────────────────────────────────────────────────────┘
```

## 🛠️ Технологии

| Категория | Технологии |
|-----------|------------|
| **UI** | Compose Multiplatform, Material 3 |
| **Architecture** | MVI, Clean Architecture |
| **DI** | Koin |
| **Database** | SQLDelight |
| **Navigation** | Compose Navigation (Type-safe routes) |
| **Async** | Kotlin Coroutines, Flow |

## 📁 Структура проекта

```
abramyan-go/
├── shared/                    # KMP Shared Module
│   └── src/
│       ├── commonMain/        # Общий код
│       │   ├── kotlin/
│       │   │   ├── core/di/   # Koin модули
│       │   │   ├── domain/    # Модели, репозитории, use cases
│       │   │   └── data/      # БД, JSON, реализации
│       │   └── sqldelight/    # Схема базы данных
│       ├── androidMain/       # Android-специфичный код
│       └── iosMain/           # iOS-специфичный код
│
├── composeApp/                # Compose Multiplatform UI
│   └── src/commonMain/
│       └── kotlin/ui/
│           ├── theme/         # Liquid Glass дизайн-система
│           ├── components/    # Переиспользуемые компоненты
│           ├── screens/       # Экраны приложения
│           └── navigation/    # Маршруты навигации
│
├── androidApp/                # Android Entry Point
└── iosApp/                    # iOS Entry Point
```

## 🚀 Запуск

### Требования

- JDK 17+
- Android Studio Hedgehog+ / Fleet
- Xcode 15+ (для iOS)

### Android

```bash
./gradlew :androidApp:assembleDebug
```

### iOS

```bash
cd iosApp
pod install
open iosApp.xcworkspace
```

## 📊 MVP Scope

**Миры:** Долина Начинаний + Поля Истины  
**Механики:** Drag & Drop, Fill Blanks, Bug Hunt  
**Язык:** Python  
**Задачи:** 23 задачи из Begin и Boolean серий

## 🎨 Дизайн-система

Liquid Glass — современный glassmorphism стиль, вдохновлённый Apple iOS:

- Полупрозрачные поверхности с размытием
- Тонкие градиентные границы
- Мягкие тени и блики
- Тёмная тема (AMOLED-friendly) по умолчанию

## 📈 Система прогрессии

| Ранг | XP | Название |
|------|-----|----------|
| 1 | 0 | Новичок |
| 2 | 500 | Ученик |
| 3 | 2,000 | Подмастерье |
| 4 | 5,000 | Кодер |
| 5 | 12,000 | Инженер |
| 6 | 25,000 | Архитектор |
| 7 | 50,000 | Мастер Кода |
| 8 | 100,000 | Легенда |

## 📝 Лицензия

MIT License — см. [LICENSE](LICENSE)

## 🙏 Благодарности

- **М.Э. Абрамян** — за великолепный задачник "1000 задач по программированию"
- **JetBrains** — за Kotlin и Compose Multiplatform
- **Apple** — за вдохновение дизайном Liquid Glass

---

<p align="center">
  Made with ❤️ for learners
</p>
