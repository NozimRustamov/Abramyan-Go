package com.abramyango.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.abramyango.data.db.AbramyanGoDatabase
import com.abramyango.data.json.WorldsDataSource
import com.abramyango.domain.model.World
import com.abramyango.domain.model.WorldProgress
import com.abramyango.domain.repository.WorldRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class WorldRepositoryImpl(
    private val database: AbramyanGoDatabase,
    private val worldsDataSource: WorldsDataSource
) : WorldRepository {
    
    private val queries = database.abramyanGoDatabaseQueries
    
    override fun getAllWorlds(): Flow<List<World>> {
        return flowOf(worldsDataSource.getAllWorlds())
    }
    
    override fun getWorld(worldId: String): Flow<World?> {
        return flowOf(worldsDataSource.getWorld(worldId))
    }
    
    override fun getWorldProgress(worldId: String): Flow<WorldProgress> {
        return queries.getWorldProgress(worldId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { entity ->
                entity?.let {
                    WorldProgress(
                        worldId = it.world_id,
                        completedTasks = it.completed_tasks.toInt(),
                        totalTasks = it.total_tasks.toInt(),
                        isUnlocked = it.is_unlocked == 1L,
                        isBossDefeated = it.is_boss_defeated == 1L,
                        bestStreak = it.best_streak.toInt()
                    )
                } ?: WorldProgress(
                    worldId = worldId,
                    completedTasks = 0,
                    totalTasks = worldsDataSource.getWorld(worldId)?.totalTasks ?: 0,
                    isUnlocked = worldId == "valley_of_beginnings", // First world unlocked by default
                    isBossDefeated = false
                )
            }
    }
    
    override fun getAllWorldProgress(): Flow<List<WorldProgress>> {
        return queries.getAllWorldProgress()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                val worlds = worldsDataSource.getAllWorlds()
                worlds.map { world ->
                    val entity = entities.find { it.world_id == world.id }
                    entity?.let {
                        WorldProgress(
                            worldId = it.world_id,
                            completedTasks = it.completed_tasks.toInt(),
                            totalTasks = it.total_tasks.toInt(),
                            isUnlocked = it.is_unlocked == 1L,
                            isBossDefeated = it.is_boss_defeated == 1L,
                            bestStreak = it.best_streak.toInt()
                        )
                    } ?: WorldProgress(
                        worldId = world.id,
                        completedTasks = 0,
                        totalTasks = world.totalTasks,
                        isUnlocked = world.order == 1,
                        isBossDefeated = false
                    )
                }
            }
    }
    
    override suspend fun unlockWorld(worldId: String) = withContext(Dispatchers.IO) {
        val world = worldsDataSource.getWorld(worldId) ?: return@withContext
        queries.insertWorldProgress(
            world_id = worldId,
            completed_tasks = 0,
            total_tasks = world.totalTasks.toLong(),
            is_unlocked = 1,
            is_boss_defeated = 0,
            best_streak = 0
        )
    }
    
    override suspend fun updateWorldProgress(progress: WorldProgress) = withContext(Dispatchers.IO) {
        queries.insertWorldProgress(
            world_id = progress.worldId,
            completed_tasks = progress.completedTasks.toLong(),
            total_tasks = progress.totalTasks.toLong(),
            is_unlocked = if (progress.isUnlocked) 1 else 0,
            is_boss_defeated = if (progress.isBossDefeated) 1 else 0,
            best_streak = progress.bestStreak.toLong()
        )
    }
}
