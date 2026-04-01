package com.abramyango.domain.model

import kotlinx.serialization.Serializable

/**
 * Ранги игрока
 */
@Serializable
enum class PlayerRank(val titleKey: String, val minXp: Int) {
    NOVICE("rank_novice", 0),
    APPRENTICE("rank_apprentice", 500),
    JOURNEYMAN("rank_journeyman", 2_000),
    CODER("rank_coder", 5_000),
    ENGINEER("rank_engineer", 12_000),
    ARCHITECT("rank_architect", 25_000),
    CODE_MASTER("rank_code_master", 50_000),
    LEGEND("rank_legend", 100_000);
    
    companion object {
        fun fromXp(xp: Int): PlayerRank {
            return entries.lastOrNull { xp >= it.minXp } ?: NOVICE
        }
        
        fun getNextRank(current: PlayerRank): PlayerRank? {
            val currentIndex = entries.indexOf(current)
            return if (currentIndex < entries.lastIndex) entries[currentIndex + 1] else null
        }
    }
}

/**
 * Профиль игрока
 */
@Serializable
data class PlayerProfile(
    val totalXp: Int = 0,
    val coins: Int = 100,               // Начальные монеты
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastActivityDate: String? = null, // ISO date
    val selectedLanguage: ProgrammingLanguage = ProgrammingLanguage.PYTHON,
    
    // Статистика
    val totalTasksSolved: Int = 0,
    val tasksWithFirstAttempt: Int = 0,
    val totalPlayTimeMinutes: Int = 0,
    val blitzHighScore: Int = 0,
    
    // Энергия
    val currentEnergy: Int = MAX_ENERGY,
    val lastEnergyUpdateTimestamp: Long = 0
) {
    val rank: PlayerRank
        get() = PlayerRank.fromXp(totalXp)
    
    val nextRank: PlayerRank?
        get() = PlayerRank.getNextRank(rank)
    
    val xpToNextRank: Int?
        get() = nextRank?.minXp?.minus(totalXp)
    
    val rankProgress: Float
        get() {
            val next = nextRank ?: return 1f
            val currentMin = rank.minXp
            val nextMin = next.minXp
            return (totalXp - currentMin).toFloat() / (nextMin - currentMin)
        }
    
    val firstAttemptRate: Float
        get() = if (totalTasksSolved > 0) {
            tasksWithFirstAttempt.toFloat() / totalTasksSolved
        } else 0f
    
    companion object {
        const val MAX_ENERGY = 100
        const val ENERGY_RESTORE_RATE_MS = 144_000L // 4 часа на полное восстановление
    }
}

/**
 * Способности (расходуемые)
 */
@Serializable
enum class Ability(val nameKey: String, val descriptionKey: String, val cost: Int) {
    HINT("ability_hint", "ability_hint_desc", 50),
    REMOVE_WRONG("ability_remove_wrong", "ability_remove_wrong_desc", 30),
    SECOND_CHANCE("ability_second_chance", "ability_second_chance_desc", 40),
    XRAY("ability_xray", "ability_xray_desc", 60)
}

/**
 * Инвентарь способностей игрока
 */
@Serializable
data class AbilityInventory(
    val hints: Int = 0,
    val removeWrong: Int = 0,
    val secondChance: Int = 0,
    val xray: Int = 0
) {
    fun getCount(ability: Ability): Int = when (ability) {
        Ability.HINT -> hints
        Ability.REMOVE_WRONG -> removeWrong
        Ability.SECOND_CHANCE -> secondChance
        Ability.XRAY -> xray
    }
    
    fun use(ability: Ability): AbilityInventory = when (ability) {
        Ability.HINT -> copy(hints = (hints - 1).coerceAtLeast(0))
        Ability.REMOVE_WRONG -> copy(removeWrong = (removeWrong - 1).coerceAtLeast(0))
        Ability.SECOND_CHANCE -> copy(secondChance = (secondChance - 1).coerceAtLeast(0))
        Ability.XRAY -> copy(xray = (xray - 1).coerceAtLeast(0))
    }
    
    fun add(ability: Ability, count: Int = 1): AbilityInventory = when (ability) {
        Ability.HINT -> copy(hints = hints + count)
        Ability.REMOVE_WRONG -> copy(removeWrong = removeWrong + count)
        Ability.SECOND_CHANCE -> copy(secondChance = secondChance + count)
        Ability.XRAY -> copy(xray = xray + count)
    }
}

/**
 * Бонусы за стрик
 */
object StreakBonuses {
    fun getXpMultiplier(streak: Int): Float = when {
        streak >= 30 -> 1.5f
        streak >= 7 -> 1.25f
        streak >= 3 -> 1.1f
        else -> 1f
    }
    
    fun hasSpecialTitle(streak: Int): Boolean = streak >= 30
    
    const val SPECIAL_TITLE_KEY = "streak_unstoppable"
}

/**
 * Комбо (серия правильных ответов)
 */
data class ComboState(
    val count: Int = 0,
    val multiplier: Int = 1
) {
    fun onCorrect(): ComboState {
        val newCount = count + 1
        val newMultiplier = when {
            newCount >= 12 -> 5
            newCount >= 8 -> 4
            newCount >= 5 -> 3
            newCount >= 3 -> 2
            else -> 1
        }
        return ComboState(newCount, newMultiplier)
    }
    
    fun reset(): ComboState = ComboState(0, 1)
    
    companion object {
        const val MAX_MULTIPLIER = 5
    }
}
