package com.abramyango.domain.model

import kotlinx.serialization.Serializable

/**
 * Мир в игре - отдельная глава с темой, сюжетом и боссом
 */
@Serializable
data class World(
    val id: String,
    val nameKey: String,        // Ключ локализации для названия
    val descriptionKey: String, // Ключ локализации для описания
    val theme: WorldTheme,
    val topics: List<String>,   // Темы из задачника (Begin, Integer, Boolean...)
    val totalTasks: Int,
    val bossId: String,
    val order: Int,             // Порядок разблокировки
    val storyContextKey: String // Ключ для сюжетного контекста
)

/**
 * Визуальная тема мира
 */
@Serializable
data class WorldTheme(
    val primaryColorHex: String,
    val secondaryColorHex: String,
    val gradientStartHex: String,
    val gradientEndHex: String,
    val iconName: String,
    val backgroundImageName: String? = null
)

/**
 * Прогресс игрока в мире
 */
@Serializable
data class WorldProgress(
    val worldId: String,
    val completedTasks: Int,
    val totalTasks: Int,
    val isUnlocked: Boolean,
    val isBossDefeated: Boolean,
    val bestStreak: Int = 0
) {
    val progressPercent: Float
        get() = if (totalTasks > 0) completedTasks.toFloat() / totalTasks else 0f
    
    val isCompleted: Boolean
        get() = completedTasks >= totalTasks && isBossDefeated
}

/**
 * Доступные миры в MVP
 */
object Worlds {
    const val VALLEY_OF_BEGINNINGS = "valley_of_beginnings"
    const val FIELDS_OF_TRUTH = "fields_of_truth"
    const val CROSSROADS_OF_FATE = "crossroads_of_fate"
    const val TIME_LOOP = "time_loop"
    const val DATA_RIVER = "data_river"
    const val OCEAN_OF_ARRAYS = "ocean_of_arrays"
    const val MATRIX_CITADEL = "matrix_citadel"
    const val TEXT_FOREST = "text_forest"
    const val ARCHITECT_ARCHIVES = "architect_archives"
}
