package com.abramyango.domain.model

import kotlinx.serialization.Serializable

/**
 * Достижение (бейдж)
 */
@Serializable
data class Achievement(
    val id: String,
    val nameKey: String,
    val descriptionKey: String,
    val iconName: String,
    val category: AchievementCategory,
    val requirement: AchievementRequirement,
    val isSecret: Boolean = false       // Скрытые достижения
)

@Serializable
enum class AchievementCategory {
    PROGRESS,       // Прогресс в игре
    MASTERY,        // Мастерство
    STREAK,         // Стрики
    SPECIAL         // Особые
}

/**
 * Требование для получения достижения
 */
@Serializable
sealed class AchievementRequirement {
    @Serializable
    data class TasksCompleted(val count: Int) : AchievementRequirement()
    
    @Serializable
    data class WorldCompleted(val worldId: String) : AchievementRequirement()
    
    @Serializable
    data class AllWorldsCompleted(val dummy: Boolean = true) : AchievementRequirement()
    
    @Serializable
    data class BossDefeated(val worldId: String) : AchievementRequirement()
    
    @Serializable
    data class AllBossesDefeated(val dummy: Boolean = true) : AchievementRequirement()
    
    @Serializable
    data class StreakDays(val days: Int) : AchievementRequirement()
    
    @Serializable
    data class MechanicMastery(val mechanic: TaskMechanic, val count: Int) : AchievementRequirement()
    
    @Serializable
    data class FirstAttemptCount(val count: Int) : AchievementRequirement()
    
    @Serializable
    data class BlitzScore(val score: Int) : AchievementRequirement()
    
    @Serializable
    data class RankReached(val rank: PlayerRank) : AchievementRequirement()
    
    @Serializable
    data class LanguagesUsed(val count: Int) : AchievementRequirement()
    
    @Serializable
    data class ComboReached(val multiplier: Int) : AchievementRequirement()
}

/**
 * Прогресс достижения игрока
 */
@Serializable
data class AchievementProgress(
    val achievementId: String,
    val isUnlocked: Boolean = false,
    val unlockedTimestamp: Long? = null,
    val currentProgress: Int = 0,
    val targetProgress: Int = 1
) {
    val progressPercent: Float
        get() = if (targetProgress > 0) {
            (currentProgress.toFloat() / targetProgress).coerceAtMost(1f)
        } else 0f
}

/**
 * Предустановленные достижения
 */
object Achievements {
    val FIRST_BLOOD = Achievement(
        id = "first_blood",
        nameKey = "achievement_first_blood",
        descriptionKey = "achievement_first_blood_desc",
        iconName = "ic_achievement_first",
        category = AchievementCategory.PROGRESS,
        requirement = AchievementRequirement.TasksCompleted(1)
    )
    
    val FLAWLESS = Achievement(
        id = "flawless",
        nameKey = "achievement_flawless",
        descriptionKey = "achievement_flawless_desc",
        iconName = "ic_achievement_flawless",
        category = AchievementCategory.MASTERY,
        requirement = AchievementRequirement.FirstAttemptCount(10)
    )
    
    val MARATHONER = Achievement(
        id = "marathoner",
        nameKey = "achievement_marathoner",
        descriptionKey = "achievement_marathoner_desc",
        iconName = "ic_achievement_marathon",
        category = AchievementCategory.STREAK,
        requirement = AchievementRequirement.StreakDays(30)
    )
    
    val BUG_HUNTER = Achievement(
        id = "bug_hunter",
        nameKey = "achievement_bug_hunter",
        descriptionKey = "achievement_bug_hunter_desc",
        iconName = "ic_achievement_bug",
        category = AchievementCategory.MASTERY,
        requirement = AchievementRequirement.MechanicMastery(TaskMechanic.BUG_HUNT, 50)
    )
    
    val SPEEDSTER = Achievement(
        id = "speedster",
        nameKey = "achievement_speedster",
        descriptionKey = "achievement_speedster_desc",
        iconName = "ic_achievement_speed",
        category = AchievementCategory.SPECIAL,
        requirement = AchievementRequirement.BlitzScore(10)
    )
    
    val BOSS_KILLER = Achievement(
        id = "boss_killer",
        nameKey = "achievement_boss_killer",
        descriptionKey = "achievement_boss_killer_desc",
        iconName = "ic_achievement_boss",
        category = AchievementCategory.PROGRESS,
        requirement = AchievementRequirement.AllBossesDefeated()
    )
    
    val POLYGLOT = Achievement(
        id = "polyglot",
        nameKey = "achievement_polyglot",
        descriptionKey = "achievement_polyglot_desc",
        iconName = "ic_achievement_polyglot",
        category = AchievementCategory.SPECIAL,
        requirement = AchievementRequirement.LanguagesUsed(4)
    )
    
    val CODE_ARCHITECT = Achievement(
        id = "code_architect",
        nameKey = "achievement_code_architect",
        descriptionKey = "achievement_code_architect_desc",
        iconName = "ic_achievement_architect",
        category = AchievementCategory.MASTERY,
        requirement = AchievementRequirement.MechanicMastery(TaskMechanic.REFACTORING, 20)
    )
    
    val COMBO_MASTER = Achievement(
        id = "combo_master",
        nameKey = "achievement_combo_master",
        descriptionKey = "achievement_combo_master_desc",
        iconName = "ic_achievement_combo",
        category = AchievementCategory.MASTERY,
        requirement = AchievementRequirement.ComboReached(5)
    )
    
    val LEGEND = Achievement(
        id = "legend",
        nameKey = "achievement_legend",
        descriptionKey = "achievement_legend_desc",
        iconName = "ic_achievement_legend",
        category = AchievementCategory.SPECIAL,
        requirement = AchievementRequirement.RankReached(PlayerRank.LEGEND),
        isSecret = true
    )
    
    val ALL = listOf(
        FIRST_BLOOD, FLAWLESS, MARATHONER, BUG_HUNTER, SPEEDSTER,
        BOSS_KILLER, POLYGLOT, CODE_ARCHITECT, COMBO_MASTER, LEGEND
    )
}
