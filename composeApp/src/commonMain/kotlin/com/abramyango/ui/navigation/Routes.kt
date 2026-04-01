package com.abramyango.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Типобезопасные маршруты навигации
 */
@Serializable
sealed class Route {
    @Serializable
    data object WorldMap : Route()
    
    @Serializable
    data class WorldDetail(val worldId: String) : Route()
    
    @Serializable
    data class Task(val worldId: String, val taskId: String) : Route()
    
    @Serializable
    data class Boss(val worldId: String) : Route()
    
    @Serializable
    data object Profile : Route()
    
    @Serializable
    data object Settings : Route()
    
    @Serializable
    data object BlitzMode : Route()
    
    @Serializable
    data class Lesson(val worldId: String) : Route()
    
    @Serializable
    data object Onboarding : Route()
}
