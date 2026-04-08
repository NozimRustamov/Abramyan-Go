package com.abramyango.domain.model

import kotlinx.serialization.Serializable

/**
 * Категория задач (отображается на главном экране)
 */
@Serializable
data class Category(
    val id: String,
    val name: String
)

/**
 * Обёртка JSON файла с категориями
 */
@Serializable
data class CategoriesFileJson(
    val categories: List<Category>
)

/**
 * Решение задачи на конкретном языке программирования
 */
@Serializable
data class Solution(
    val language: String,
    val code: String
)

/**
 * Задача с описанием и решениями (формат для просмотра)
 */
@Serializable
data class CategoryTask(
    val id: String,
    val title: String,
    val description: String,
    val solutions: List<Solution> = emptyList()
)

/**
 * Обёртка JSON файла с задачами категории
 */
@Serializable
data class CategoryTasksFileJson(
    val tasks: List<CategoryTask>
)
