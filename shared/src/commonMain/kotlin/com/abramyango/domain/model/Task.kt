package com.abramyango.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Механика решения задачи (3 типа из JSON)
 */
@Serializable
enum class TaskMechanic {
    @SerialName("DragDrop")
    DRAG_DROP,

    @SerialName("BugHunt")
    BUG_HUNT,

    @SerialName("FillBlank")
    FILL_BLANK
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
 * Блок кода для DragDrop механики
 */
@Serializable
data class DragDropBlock(
    val id: Int,
    val code: String
)

/**
 * Данные задачи из JSON (поля заполняются в зависимости от механики)
 */
@Serializable
data class TaskData(
    val language: String = "kotlin",
    val description: String? = null,
    // DragDrop
    val blocks: List<DragDropBlock>? = null,
    @SerialName("correct_order") val correctOrder: List<Int>? = null,
    // BugHunt
    @SerialName("code_with_bug") val codeWithBug: String? = null,
    @SerialName("bug_line_index") val bugLineIndex: Int? = null,
    @SerialName("bug_options") val bugOptions: List<String>? = null,
    @SerialName("correct_option") val correctOption: String? = null,
    // FillBlank (correct_option shared with BugHunt)
    @SerialName("code_template") val codeTemplate: String? = null,
    @SerialName("blank_options") val blankOptions: List<String>? = null
)

/**
 * Обёртка JSON файла с задачами
 */
@Serializable
data class TasksFileJson(
    val tasks: List<Task>,
    @SerialName("world_id") val worldId: Int,
    @SerialName("world_name") val worldName: String
)

/**
 * Задача из задачника Абрамяна
 */
@Serializable
data class Task(
    val id: String,
    @SerialName("abramyan_text") val abramyanText: String = "",
    @SerialName("rpg_context") val rpgContext: String = "",
    val mechanic: TaskMechanic = TaskMechanic.DRAG_DROP,
    @SerialName("task_data") val taskData: TaskData = TaskData(),
    // Поля, устанавливаемые после парсинга JSON
    val worldId: String = "",
    val xpReward: Int = 10,
    val order: Int = 0
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
