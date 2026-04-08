package com.abramyango.data.repository

import com.abramyango.domain.model.CategoriesFileJson
import com.abramyango.domain.model.Category
import com.abramyango.domain.model.CategoryTask
import com.abramyango.domain.model.CategoryTasksFileJson
import com.abramyango.domain.repository.CategoryRepository
import kotlinx.serialization.json.Json

class CategoryRepositoryImpl(
    private val categoriesLoader: suspend () -> String,
    private val categoryTasksLoader: suspend (String) -> String
) : CategoryRepository {

    private val json = Json { ignoreUnknownKeys = true }
    private var cachedCategories: List<Category>? = null

    override suspend fun getCategories(): List<Category> {
        cachedCategories?.let { return it }
        val jsonString = categoriesLoader()
        val file = json.decodeFromString<CategoriesFileJson>(jsonString)
        cachedCategories = file.categories
        return file.categories
    }

    override suspend fun getTasksForCategory(categoryId: String): List<CategoryTask> {
        val jsonString = categoryTasksLoader(categoryId)
        val file = json.decodeFromString<CategoryTasksFileJson>(jsonString)
        return file.tasks
    }
}
