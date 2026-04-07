package com.abramyango.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.abramyango.data.db.AbramyanGoDatabase
import com.abramyango.data.json.TasksDataSource
import com.abramyango.domain.model.Task
import com.abramyango.domain.model.TaskResult
import com.abramyango.domain.repository.TaskProgress
import com.abramyango.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class TaskRepositoryImpl(
    private val database: AbramyanGoDatabase,
    private val tasksDataSource: TasksDataSource,
    private val jsonLoader: (suspend () -> List<String>)? = null
) : TaskRepository {

    private val queries = database.abramyanGoDatabaseQueries
    private val loadMutex = Mutex()
    private var loaded = false

    private suspend fun ensureLoaded() {
        if (loaded) return
        loadMutex.withLock {
            if (!loaded && jsonLoader != null) {
                val jsonStrings = jsonLoader.invoke()
                jsonStrings.forEach { tasksDataSource.loadFromJson(it) }
                loaded = true
            }
        }
    }

    override fun getTasksForWorld(worldId: String): Flow<List<Task>> = flow {
        ensureLoaded()
        emit(tasksDataSource.getTasksForWorld(worldId))
    }

    override fun getTask(taskId: String): Flow<Task?> = flow {
        ensureLoaded()
        emit(tasksDataSource.getTask(taskId))
    }

    override fun getTaskProgress(taskId: String): Flow<TaskProgress?> {
        return queries.getTaskProgress(taskId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { entity ->
                entity?.let {
                    TaskProgress(
                        taskId = it.task_id,
                        isCompleted = it.is_completed == 1L,
                        bestAttempt = it.best_attempt.toInt(),
                        lastAttemptTimestamp = it.last_attempt_timestamp
                    )
                }
            }
    }

    override suspend fun saveTaskResult(result: TaskResult) = withContext(Dispatchers.IO) {
        queries.insertTaskResult(
            task_id = result.taskId,
            is_correct = if (result.isCorrect) 1 else 0,
            attempt_number = result.attemptNumber.toLong(),
            time_spent_seconds = result.timeSpentSeconds.toLong(),
            xp_earned = result.xpEarned.toLong(),
            combo_multiplier = result.comboMultiplier.toLong(),
            timestamp = result.timestamp
        )

        if (result.isCorrect) {
            val existing = queries.getTaskProgress(result.taskId).executeAsOneOrNull()
            val bestAttempt = existing?.best_attempt?.toInt()?.coerceAtMost(result.attemptNumber)
                ?: result.attemptNumber

            queries.insertOrUpdateTaskProgress(
                task_id = result.taskId,
                is_completed = 1,
                best_attempt = bestAttempt.toLong(),
                last_attempt_timestamp = result.timestamp
            )

            queries.incrementTasksSolved()
            if (result.attemptNumber == 1) {
                queries.incrementFirstAttempt()
            }
        }
    }

    override suspend fun getCompletedTaskIds(worldId: String): List<String> = withContext(Dispatchers.IO) {
        val prefix = worldId
        queries.getCompletedTasksForWorld(prefix)
            .executeAsList()
    }
}
