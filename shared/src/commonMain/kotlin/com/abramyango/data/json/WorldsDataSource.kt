package com.abramyango.data.json

import com.abramyango.domain.model.World
import com.abramyango.domain.model.WorldTheme
import com.abramyango.domain.model.Worlds

/**
 * Источник данных для миров
 * Миры соответствуют главам задачника Абрамяна
 */
class WorldsDataSource {
    
    private val worlds: List<World> = listOf(
        // Мир 1: Долина Начинаний (Begin + Integer)
        World(
            id = Worlds.VALLEY_OF_BEGINNINGS,
            nameKey = "world_valley_name",
            descriptionKey = "world_valley_desc",
            theme = WorldTheme(
                primaryColorHex = "#4CAF50",
                secondaryColorHex = "#81C784",
                gradientStartHex = "#1B5E20",
                gradientEndHex = "#2E7D32",
                iconName = "ic_world_valley",
                backgroundImageName = "bg_valley"
            ),
            topics = listOf("Begin", "Integer"),
            totalTasks = 70, // Begin1-40 + Integer1-30
            bossId = "boss_valley",
            order = 1,
            storyContextKey = "world_valley_story"
        ),
        
        // Мир 2: Поля Истины (Boolean)
        World(
            id = Worlds.FIELDS_OF_TRUTH,
            nameKey = "world_fields_name",
            descriptionKey = "world_fields_desc",
            theme = WorldTheme(
                primaryColorHex = "#2196F3",
                secondaryColorHex = "#64B5F6",
                gradientStartHex = "#0D47A1",
                gradientEndHex = "#1565C0",
                iconName = "ic_world_fields",
                backgroundImageName = "bg_fields"
            ),
            topics = listOf("Boolean"),
            totalTasks = 40, // Boolean1-40
            bossId = "boss_fields",
            order = 2,
            storyContextKey = "world_fields_story"
        ),
        
        // Мир 3: Развилка Судьбы (If + Case)
        World(
            id = Worlds.CROSSROADS_OF_FATE,
            nameKey = "world_crossroads_name",
            descriptionKey = "world_crossroads_desc",
            theme = WorldTheme(
                primaryColorHex = "#9C27B0",
                secondaryColorHex = "#BA68C8",
                gradientStartHex = "#4A148C",
                gradientEndHex = "#6A1B9A",
                iconName = "ic_world_crossroads",
                backgroundImageName = "bg_crossroads"
            ),
            topics = listOf("If", "Case"),
            totalTasks = 50, // If1-30 + Case1-20
            bossId = "boss_crossroads",
            order = 3,
            storyContextKey = "world_crossroads_story"
        ),
        
        // Мир 4: Петля Времени (For + While)
        World(
            id = Worlds.TIME_LOOP,
            nameKey = "world_timeloop_name",
            descriptionKey = "world_timeloop_desc",
            theme = WorldTheme(
                primaryColorHex = "#FF9800",
                secondaryColorHex = "#FFB74D",
                gradientStartHex = "#E65100",
                gradientEndHex = "#F57C00",
                iconName = "ic_world_timeloop",
                backgroundImageName = "bg_timeloop"
            ),
            topics = listOf("For", "While"),
            totalTasks = 70, // For1-40 + While1-30
            bossId = "boss_timeloop",
            order = 4,
            storyContextKey = "world_timeloop_story"
        ),
        
        // Мир 5: Река Данных (Series)
        World(
            id = Worlds.DATA_RIVER,
            nameKey = "world_river_name",
            descriptionKey = "world_river_desc",
            theme = WorldTheme(
                primaryColorHex = "#00BCD4",
                secondaryColorHex = "#4DD0E1",
                gradientStartHex = "#006064",
                gradientEndHex = "#00838F",
                iconName = "ic_world_river",
                backgroundImageName = "bg_river"
            ),
            topics = listOf("Series"),
            totalTasks = 40, // Series1-40
            bossId = "boss_river",
            order = 5,
            storyContextKey = "world_river_story"
        ),
        
        // Мир 6: Океан Массивов (Array) - будет в части 2 задачника
        World(
            id = Worlds.OCEAN_OF_ARRAYS,
            nameKey = "world_ocean_name",
            descriptionKey = "world_ocean_desc",
            theme = WorldTheme(
                primaryColorHex = "#3F51B5",
                secondaryColorHex = "#7986CB",
                gradientStartHex = "#1A237E",
                gradientEndHex = "#283593",
                iconName = "ic_world_ocean",
                backgroundImageName = "bg_ocean"
            ),
            topics = listOf("Array"),
            totalTasks = 60,
            bossId = "boss_ocean",
            order = 6,
            storyContextKey = "world_ocean_story"
        ),
        
        // Мир 7: Цитадель Матриц (Matrix)
        World(
            id = Worlds.MATRIX_CITADEL,
            nameKey = "world_citadel_name",
            descriptionKey = "world_citadel_desc",
            theme = WorldTheme(
                primaryColorHex = "#607D8B",
                secondaryColorHex = "#90A4AE",
                gradientStartHex = "#37474F",
                gradientEndHex = "#455A64",
                iconName = "ic_world_citadel",
                backgroundImageName = "bg_citadel"
            ),
            topics = listOf("Matrix"),
            totalTasks = 50,
            bossId = "boss_citadel",
            order = 7,
            storyContextKey = "world_citadel_story"
        ),
        
        // Мир 8: Текстовый Лес (String)
        World(
            id = Worlds.TEXT_FOREST,
            nameKey = "world_forest_name",
            descriptionKey = "world_forest_desc",
            theme = WorldTheme(
                primaryColorHex = "#795548",
                secondaryColorHex = "#A1887F",
                gradientStartHex = "#3E2723",
                gradientEndHex = "#4E342E",
                iconName = "ic_world_forest",
                backgroundImageName = "bg_forest"
            ),
            topics = listOf("String"),
            totalTasks = 50,
            bossId = "boss_forest",
            order = 8,
            storyContextKey = "world_forest_story"
        ),
        
        // Мир 9: Архивы Архитектора (Proc + File + Recursion)
        World(
            id = Worlds.ARCHITECT_ARCHIVES,
            nameKey = "world_archives_name",
            descriptionKey = "world_archives_desc",
            theme = WorldTheme(
                primaryColorHex = "#F44336",
                secondaryColorHex = "#E57373",
                gradientStartHex = "#B71C1C",
                gradientEndHex = "#C62828",
                iconName = "ic_world_archives",
                backgroundImageName = "bg_archives"
            ),
            topics = listOf("Proc", "File", "Recursion"),
            totalTasks = 80, // Proc1-60 + более
            bossId = "boss_archives",
            order = 9,
            storyContextKey = "world_archives_story"
        )
    )
    
    fun getAllWorlds(): List<World> = worlds
    
    fun getWorld(worldId: String): World? = worlds.find { it.id == worldId }
    
    fun getWorldByOrder(order: Int): World? = worlds.find { it.order == order }
    
    fun getNextWorld(currentWorldId: String): World? {
        val current = getWorld(currentWorldId) ?: return null
        return getWorldByOrder(current.order + 1)
    }
}
