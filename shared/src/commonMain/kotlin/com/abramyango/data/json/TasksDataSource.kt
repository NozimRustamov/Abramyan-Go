package com.abramyango.data.json

import com.abramyango.domain.model.Task
import com.abramyango.domain.model.TasksFileJson
import com.abramyango.domain.model.Worlds
import kotlinx.serialization.json.Json

/**
 * Источник данных для задач.
 * Загружает задачи из JSON файлов (begin_1-40.json, integer 1-40.json и т.д.)
 */
class TasksDataSource {

    private val json = Json { ignoreUnknownKeys = true }
    private val tasks = mutableListOf<Task>()

    /**
     * Загружает задачи из JSON строки.
     * Можно вызывать несколько раз для разных файлов (begin, integer, boolean...).
     */
    fun loadFromJson(jsonString: String) {
        val file = json.decodeFromString<TasksFileJson>(jsonString)
        val worldId = mapWorldId(file.worldId)
        val parsed = file.tasks.mapIndexed { index, task ->
            task.copy(
                worldId = worldId,
                order = index + 1,
                xpReward = 10 + (index / 10) * 5
            )
        }
        tasks.removeAll { it.worldId == worldId }
        tasks.addAll(parsed)
    }

    fun getTasksForWorld(worldId: String): List<Task> {
        return tasks.filter { it.worldId == worldId }.sortedBy { it.order }
    }

    fun getTask(taskId: String): Task? {
        return tasks.find { it.id == taskId }
    }

    fun getAllTasks(): List<Task> = tasks.toList()

    fun isLoaded(): Boolean = tasks.isNotEmpty()

    private fun mapWorldId(id: Int): String = when (id) {
        1 -> Worlds.VALLEY_OF_BEGINNINGS
        2 -> Worlds.FIELDS_OF_TRUTH
        3 -> Worlds.CROSSROADS_OF_FATE
        4 -> Worlds.TIME_LOOP
        5 -> Worlds.DATA_RIVER
        6 -> Worlds.OCEAN_OF_ARRAYS
        7 -> Worlds.MATRIX_CITADEL
        8 -> Worlds.TEXT_FOREST
        9 -> Worlds.ARCHITECT_ARCHIVES
        else -> "world_$id"
    }
}
