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
    database: AbramyanGoDatabase
) : AchievementRepository {
    
    private val queries = database.abramyanGoDatabaseQueries
    
    override fun getAllAchievements(): Flow<List<Achievement>> {
        return kotlinx.coroutines.flow.flowOf(Achievements.ALL)
    }
    
    override fun getAchievementProgress(): Flow<List<AchievementProgress>> {
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
    
    override suspend fun checkAndUnlockAchievements() {
        // Implementation for checking and unlocking achievements
    }
    
    override suspend fun unlockAchievement(achievementId: String) = withContext(Dispatchers.IO) {
        queries.unlockAchievement(
            achievement_id = achievementId,
            unlocked_timestamp = currentTimeMillis()
        )
    }
}
