package com.abramyango.domain.repository

import com.abramyango.domain.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий игрока
 */
interface PlayerRepository {
    fun getProfile(): Flow<PlayerProfile>
    suspend fun updateProfile(profile: PlayerProfile)
    suspend fun addXp(amount: Int)
    suspend fun addCoins(amount: Int)
    suspend fun spendCoins(amount: Int): Boolean
    suspend fun updateStreak()
    suspend fun updateEnergy(delta: Int)
    suspend fun setSelectedLanguage(language: ProgrammingLanguage)
}

/**
 * Репозиторий миров
 */
interface WorldRepository {
    fun getAllWorlds(): Flow<List<World>>
    fun getWorld(worldId: String): Flow<World?>
    fun getWorldProgress(worldId: String): Flow<WorldProgress>
    fun getAllWorldProgress(): Flow<List<WorldProgress>>
    suspend fun unlockWorld(worldId: String)
    suspend fun updateWorldProgress(progress: WorldProgress)
}

/**
 * Репозиторий задач
 */
interface TaskRepository {
    fun getTasksForWorld(worldId: String): Flow<List<Task>>
    fun getTask(taskId: String): Flow<Task?>
    fun getTaskProgress(taskId: String): Flow<TaskProgress?>
    suspend fun saveTaskResult(result: TaskResult)
    suspend fun getCompletedTaskIds(worldId: String): List<String>
}

/**
 * Прогресс задачи
 */
data class TaskProgress(
    val taskId: String,
    val isCompleted: Boolean,
    val bestAttempt: Int,
    val lastAttemptTimestamp: Long
)

/**
 * Репозиторий способностей
 */
interface AbilityRepository {
    fun getInventory(): Flow<AbilityInventory>
    suspend fun useAbility(ability: Ability): Boolean
    suspend fun purchaseAbility(ability: Ability): Boolean
}

/**
 * Репозиторий достижений
 */
interface AchievementRepository {
    fun getAllAchievements(): Flow<List<Achievement>>
    fun getAchievementProgress(): Flow<List<AchievementProgress>>
    suspend fun checkAndUnlockAchievements()
    suspend fun unlockAchievement(achievementId: String)
}

/**
 * Репозиторий настроек
 */
interface SettingsRepository {
    fun getTheme(): Flow<AppTheme>
    fun getSoundEnabled(): Flow<Boolean>
    fun getNotificationsEnabled(): Flow<Boolean>
    fun getInterfaceLanguage(): Flow<String>
    
    suspend fun setTheme(theme: AppTheme)
    suspend fun setSoundEnabled(enabled: Boolean)
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun setInterfaceLanguage(language: String)
}

enum class AppTheme {
    DARK, LIGHT, SYSTEM
}
