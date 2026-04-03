package com.abramyango.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Механика решения задачи
 */
@Serializable
enum class TaskMechanic {
    @SerialName("drag_drop")
    DRAG_DROP,              // Сборка кода из блоков
    
    @SerialName("fill_blanks")
    FILL_BLANKS,            // Заполни пропуски
    
    @SerialName("bug_hunt")
    BUG_HUNT,               // Найди ошибку
    
    @SerialName("code_trace")
    CODE_TRACE,             // Трассировка
    
    @SerialName("output_prediction")
    OUTPUT_PREDICTION,      // Что выведет код?
    
    @SerialName("refactoring")
    REFACTORING             // Мини-рефакторинг
}

/**
 * Поддерживаемые языки программирования
 */
@Serializable
enum class ProgrammingLanguage(val displayName: String, val colorHex: String) {
    @SerialName("python")
    PYTHON("Python", "#3776AB"),
    
    @SerialName("javascript")
    JAVASCRIPT("JavaScript", "#F7DF1E"),
    
    @SerialName("kotlin")
    KOTLIN("Kotlin", "#7F52FF"),
    
    @SerialName("csharp")
    CSHARP("C#", "#239120")
}

/**
 * Код задачи на разных языках
 */
@Serializable
data class MultiLanguageCode(
    val python: String,
    val javascript: String? = null,
    val kotlin: String? = null,
    val csharp: String? = null
) {
    fun getCode(language: ProgrammingLanguage): String {
        return when (language) {
            ProgrammingLanguage.PYTHON -> python
            ProgrammingLanguage.JAVASCRIPT -> javascript ?: python
            ProgrammingLanguage.KOTLIN -> kotlin ?: python
            ProgrammingLanguage.CSHARP -> csharp ?: python
        }
    }
}

/**
 * Задача из задачника Абрамяна
 */
@Serializable
data class Task(
    val id: String,
    val worldId: String,
    val mechanic: TaskMechanic,
    val difficulty: Int,            // 1-5
    val code: MultiLanguageCode,
    val storyContextKey: String,    // Ключ локализации для сюжетного контекста
    val xpReward: Int,
    val order: Int,                 // Порядок в мире
    val abramyanId: String? = null, // Оригинальный ID из задачника (Begin1, Integer5...)
    val fillBlanksData: FillBlanksData? = null
)

/**
 * Данные для механики Drag & Drop
 */
@Serializable
data class DragDropData(
    val correctOrder: List<String>,     // Правильный порядок блоков
    val distractors: List<String> = emptyList()  // Блоки-обманки
)

/**
 * Данные для механики Fill Blanks
 */
@Serializable
data class FillBlanksData(
    val template: String,               // Код с плейсхолдерами {0}, {1}...
    val blanks: List<BlankOption>       // Варианты для каждого пропуска
)

@Serializable
data class BlankOption(
    val blankIndex: Int,
    val correctAnswer: String,
    val options: List<String>           // Включая правильный ответ
)

/**
 * Данные для механики Bug Hunt
 */
@Serializable
data class BugHuntData(
    val buggyCode: String,
    val bugLineIndex: Int,              // Индекс строки с ошибкой (0-based)
    val bugExplanationKey: String,      // Ключ локализации для объяснения
    val fixedCode: String
)

/**
 * Данные для механики Code Trace
 */
@Serializable
data class CodeTraceData(
    val steps: List<TraceStep>
)

@Serializable
data class TraceStep(
    val lineIndex: Int,
    val variableName: String,
    val expectedValue: String,
    val options: List<String> = emptyList()  // Варианты ответа
)

/**
 * Данные для механики Output Prediction
 */
@Serializable
data class OutputPredictionData(
    val correctOutput: String,
    val options: List<String>           // Включая правильный ответ
)

/**
 * Данные для механики Refactoring
 */
@Serializable
data class RefactoringData(
    val optionA: String,
    val optionB: String,
    val correctOption: String,          // "A" или "B"
    val explanationKey: String
)

/**
 * Результат решения задачи
 */
@Serializable
data class TaskResult(
    val taskId: String,
    val isCorrect: Boolean,
    val attemptNumber: Int,
    val timeSpentSeconds: Int,
    val xpEarned: Int,
    val comboMultiplier: Int,
    val timestamp: Long
)
