package com.abramyango.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive

/**
 * Категория задач (отображается на главном экране)
 * id может быть строкой или числом в JSON
 */
@Serializable
data class Category(
    val id: JsonPrimitive,
    val name: String
) {
    val idString: String get() = id.content
}

/**
 * Задача с описанием и решениями на разных языках
 * solutions — это map: {"python": "code...", "javascript": "code...", ...}
 */
@Serializable
data class CategoryTask(
    val id: String,
    val question: String,
    val solutions: Map<String, String> = emptyMap()
)

/**
 * Обёртка JSON файла с задачами категории
 */
@Serializable
data class CategoryTasksFileJson(
    val category: String,
    val id: String,
    val tasks: List<CategoryTask>
)
