package com.abramyango.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Category(val id: String, val name: String)

@Serializable
data class CategoryTask(
    val id: String,
    val question: String,
    val solutions: Map<String, String> = emptyMap()
)

@Serializable
data class CategoryTasksFileJson(
    val category: String = "",
    val id: String = "",
    val tasks: List<CategoryTask>
)

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
    suspend fun getTasksForCategory(categoryId: String): List<CategoryTask>
}

class CategoryRepositoryImpl(
    private val categoriesLoader: suspend () -> String,
    private val categoryTasksLoader: suspend (String) -> String
) : CategoryRepository {

    private val json = Json { ignoreUnknownKeys = true }
    private var cachedCategories: List<Category>? = null

    override suspend fun getCategories(): List<Category> {
        cachedCategories?.let { return it }
        return json.decodeFromString<List<Category>>(categoriesLoader())
            .also { cachedCategories = it }
    }

    override suspend fun getTasksForCategory(categoryId: String): List<CategoryTask> =
        json.decodeFromString<CategoryTasksFileJson>(categoryTasksLoader(categoryId)).tasks
}
