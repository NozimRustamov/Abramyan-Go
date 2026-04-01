package com.abramyango.domain.usecase

import com.abramyango.domain.model.*
import com.abramyango.domain.repository.*
import kotlinx.coroutines.flow.first

/**
 * Use case для решения задачи
 */
class SolveTaskUseCase(
    private val playerRepository: PlayerRepository,
    private val taskRepository: TaskRepository,
    private val worldRepository: WorldRepository,
    private val achievementRepository: AchievementRepository
) {
    /**
     * Обрабатывает результат решения задачи
     * @return Награда за решение
     */
    suspend fun execute(
        taskId: String,
        isCorrect: Boolean,
        attemptNumber: Int,
        timeSpentSeconds: Int,
        comboState: ComboState
    ): TaskReward {
        val profile = playerRepository.getProfile().first()
        val task = taskRepository.getTask(taskId).first()
            ?: throw IllegalArgumentException("Task not found: $taskId")
        
        if (!isCorrect) {
            // Сохраняем неудачную попытку
            val result = TaskResult(
                taskId = taskId,
                isCorrect = false,
                attemptNumber = attemptNumber,
                timeSpentSeconds = timeSpentSeconds,
                xpEarned = 0,
                comboMultiplier = 1,
                timestamp = currentTimeMillis()
            )
            taskRepository.saveTaskResult(result)
            
            return TaskReward(
                xpEarned = 0,
                coinsEarned = 0,
                newComboState = comboState.reset(),
                streakBonus = 1f,
                isFirstAttempt = false
            )
        }
        
        // Расчёт награды за правильный ответ
        val newComboState = comboState.onCorrect()
        val streakBonus = StreakBonuses.getXpMultiplier(profile.currentStreak)
        val isFirstAttempt = attemptNumber == 1
        
        var xpEarned = task.xpReward
        
        // Бонус за первую попытку
        if (isFirstAttempt) {
            xpEarned = (xpEarned * 1.5f).toInt()
        }
        
        // Комбо множитель
        xpEarned *= newComboState.multiplier
        
        // Стрик бонус
        xpEarned = (xpEarned * streakBonus).toInt()
        
        // Монеты = 10% от XP
        val coinsEarned = (xpEarned * 0.1f).toInt().coerceAtLeast(1)
        
        // Сохраняем результат
        val result = TaskResult(
            taskId = taskId,
            isCorrect = true,
            attemptNumber = attemptNumber,
            timeSpentSeconds = timeSpentSeconds,
            xpEarned = xpEarned,
            comboMultiplier = newComboState.multiplier,
            timestamp = currentTimeMillis()
        )
        taskRepository.saveTaskResult(result)
        
        // Обновляем профиль
        playerRepository.addXp(xpEarned)
        playerRepository.addCoins(coinsEarned)
        
        // Обновляем прогресс мира
        val completedTasks = taskRepository.getCompletedTaskIds(task.worldId)
        val worldProgress = worldRepository.getWorldProgress(task.worldId).first()
        worldRepository.updateWorldProgress(
            worldProgress.copy(completedTasks = completedTasks.size)
        )
        
        // Проверяем достижения
        achievementRepository.checkAndUnlockAchievements()
        
        return TaskReward(
            xpEarned = xpEarned,
            coinsEarned = coinsEarned,
            newComboState = newComboState,
            streakBonus = streakBonus,
            isFirstAttempt = isFirstAttempt
        )
    }
}

/**
 * Награда за решение задачи
 */
data class TaskReward(
    val xpEarned: Int,
    val coinsEarned: Int,
    val newComboState: ComboState,
    val streakBonus: Float,
    val isFirstAttempt: Boolean
)

/**
 * Use case для получения прогресса мира
 */
class GetWorldProgressUseCase(
    private val worldRepository: WorldRepository,
    private val taskRepository: TaskRepository
) {
    suspend fun execute(worldId: String): WorldProgressDetails {
        val world = worldRepository.getWorld(worldId).first()
            ?: throw IllegalArgumentException("World not found: $worldId")
        
        val progress = worldRepository.getWorldProgress(worldId).first()
        val tasks = taskRepository.getTasksForWorld(worldId).first()
        val completedTaskIds = taskRepository.getCompletedTaskIds(worldId)
        
        return WorldProgressDetails(
            world = world,
            progress = progress,
            tasks = tasks,
            completedTaskIds = completedTaskIds.toSet()
        )
    }
}

data class WorldProgressDetails(
    val world: World,
    val progress: WorldProgress,
    val tasks: List<Task>,
    val completedTaskIds: Set<String>
)

/**
 * Use case для использования способности
 */
class UseAbilityUseCase(
    private val abilityRepository: AbilityRepository,
    private val playerRepository: PlayerRepository
) {
    suspend fun execute(ability: Ability): AbilityResult {
        val success = abilityRepository.useAbility(ability)
        return if (success) {
            AbilityResult.Success
        } else {
            AbilityResult.NotEnough
        }
    }
}

sealed class AbilityResult {
    data object Success : AbilityResult()
    data object NotEnough : AbilityResult()
}

/**
 * Use case для покупки способности
 */
class PurchaseAbilityUseCase(
    private val abilityRepository: AbilityRepository,
    private val playerRepository: PlayerRepository
) {
    suspend fun execute(ability: Ability): PurchaseResult {
        val profile = playerRepository.getProfile().first()
        
        return if (profile.coins >= ability.cost) {
            val success = playerRepository.spendCoins(ability.cost)
            if (success) {
                abilityRepository.purchaseAbility(ability)
                PurchaseResult.Success
            } else {
                PurchaseResult.InsufficientFunds
            }
        } else {
            PurchaseResult.InsufficientFunds
        }
    }
}

sealed class PurchaseResult {
    data object Success : PurchaseResult()
    data object InsufficientFunds : PurchaseResult()
}

// Platform-specific implementation needed
expect fun currentTimeMillis(): Long
