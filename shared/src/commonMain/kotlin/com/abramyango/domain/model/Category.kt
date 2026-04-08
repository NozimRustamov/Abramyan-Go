package com.abramyango.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    val name: String
)

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
