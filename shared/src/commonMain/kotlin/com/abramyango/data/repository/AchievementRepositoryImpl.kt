package com.abramyango.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.abramyango.data.db.AbramyanGoDatabase
import com.abramyango.domain.model.Achievement
import com.abramyango.domain.model.AchievementProgress
import com.abramyango.domain.model.Achievements
import com.abramyango.domain.repository.AchievementRepository
import com.abramyango.domain.usecase.currentTimeMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AchievementRepositoryImpl(
    private val database: AbramyanGoDatabase
) : AchievementRepository {
    
    private val queries = database.abramyanGoDatabaseQueries
    
    override fun getAllAchievements(): Flow<List<Achievement>> {
        return kotlinx.coroutines.flow.flowOf(Achievements.ALL)
    }
    
    override fun getAchievementProgress(achievementId: String): Flow<AchievementProgress?> {
        return queries.getAchievementProgress(achievementId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { entity ->
                entity?.let {
                    AchievementProgress(
                        achievementId = it.achievement_id,
                        isUnlocked = it.is_unlocked == 1L,
                        unlockedTimestamp = it.unlocked_timestamp,
                        currentProgress = it.current_progress.toInt(),
                        targetProgress = it.target_progress.toInt()
                    )
                }
            }
    }
    
    override fun getAllProgress(): Flow<List<AchievementProgress>> {
        return queries.getAllAchievementProgress()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities ->
                entities.map { entity ->
                    AchievementProgress(
                        achievementId = entity.achievement_id,
                        isUnlocked = entity.is_unlocked == 1L,
                        unlockedTimestamp = entity.unlocked_timestamp,
                        currentProgress = entity.current_progress.toInt(),
                        targetProgress = entity.target_progress.toInt()
                    )
                }
            }
    }
    
    override suspend fun updateProgress(
        achievementId: String,
        currentProgress: Int,
        targetProgress: Int
    ) = withContext(Dispatchers.IO) {
        val existing = queries.getAchievementProgress(achievementId).executeAsOneOrNull()
        
        val isUnlocked = currentProgress >= targetProgress
        val unlockedTimestamp = if (isUnlocked && existing?.is_unlocked != 1L) {
            currentTimeMillis()
        } else {
            existing?.unlocked_timestamp
        }
        
        queries.insertOrUpdateAchievementProgress(
            achievement_id = achievementId,
            is_unlocked = if (isUnlocked) 1 else 0,
            unlocked_timestamp = unlockedTimestamp,
            current_progress = currentProgress.toLong(),
            target_progress = targetProgress.toLong()
        )
    }
    
    override suspend fun unlockAchievement(achievementId: String) = withContext(Dispatchers.IO) {
        queries.unlockAchievement(
            achievement_id = achievementId,
            unlocked_timestamp = currentTimeMillis()
        )
    }
}
