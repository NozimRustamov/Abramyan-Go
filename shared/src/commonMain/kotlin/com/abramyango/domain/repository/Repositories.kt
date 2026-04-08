package com.abramyango.domain.repository

import com.abramyango.domain.model.Category
import com.abramyango.domain.model.CategoryTask

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
    suspend fun getTasksForCategory(categoryId: String): List<CategoryTask>
}
