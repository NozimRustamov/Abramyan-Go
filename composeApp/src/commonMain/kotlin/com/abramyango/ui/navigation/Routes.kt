package com.abramyango.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    data object Categories : Route()

    @Serializable
    data class CategoryTaskList(val categoryId: String, val categoryName: String) : Route()

    @Serializable
    data class TaskDetail(val categoryId: String, val taskIndex: Int) : Route()

    @Serializable
    data object Profile : Route()

    @Serializable
    data object Settings : Route()
}
