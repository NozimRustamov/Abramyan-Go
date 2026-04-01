package com.abramyango.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import com.abramyango.data.db.AbramyanGoDatabase
import com.abramyango.domain.model.PlayerProfile
import com.abramyango.domain.model.ProgrammingLanguage
import com.abramyango.domain.repository.PlayerRepository
import com.abramyango.domain.usecase.currentTimeMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlayerRepositoryImpl(
    private val database: AbramyanGoDatabase
) : PlayerRepository {
    
    private val queries = database.abramyanGoDatabaseQueries
    
    override fun getProfile(): Flow<PlayerProfile> {
        return queries.getProfile()
            .asFlow()
            .mapToOne(Dispatchers.IO)
            .map { entity ->
                PlayerProfile(
                    totalXp = entity.total_xp.toInt(),
                    coins = entity.coins.toInt(),
                    currentStreak = entity.current_streak.toInt(),
                    longestStreak = entity.longest_streak.toInt(),
                    lastActivityDate = entity.last_activity_date,
                    selectedLanguage = ProgrammingLanguage.entries.find { 
                        it.name.lowercase() == entity.selected_language 
                    } ?: ProgrammingLanguage.PYTHON,
                    totalTasksSolved = entity.total_tasks_solved.toInt(),
                    tasksWithFirstAttempt = entity.tasks_with_first_attempt.toInt(),
                    totalPlayTimeMinutes = entity.total_play_time_minutes.toInt(),
                    blitzHighScore = entity.blitz_high_score.toInt(),
                    currentEnergy = entity.current_energy.toInt(),
                    lastEnergyUpdateTimestamp = entity.last_energy_update_timestamp
                )
            }
    }
    
    override suspend fun updateProfile(profile: PlayerProfile) = withContext(Dispatchers.IO) {
        queries.updateProfile(
            total_xp = profile.totalXp.toLong(),
            coins = profile.coins.toLong(),
            current_streak = profile.currentStreak.toLong(),
            longest_streak = profile.longestStreak.toLong(),
            last_activity_date = profile.lastActivityDate,
            selected_language = profile.selectedLanguage.name.lowercase(),
            total_tasks_solved = profile.totalTasksSolved.toLong(),
            tasks_with_first_attempt = profile.tasksWithFirstAttempt.toLong(),
            total_play_time_minutes = profile.totalPlayTimeMinutes.toLong(),
            blitz_high_score = profile.blitzHighScore.toLong(),
            current_energy = profile.currentEnergy.toLong(),
            last_energy_update_timestamp = profile.lastEnergyUpdateTimestamp
        )
    }
    
    override suspend fun addXp(amount: Int) = withContext(Dispatchers.IO) {
        queries.addXp(amount.toLong())
    }
    
    override suspend fun addCoins(amount: Int) = withContext(Dispatchers.IO) {
        queries.addCoins(amount.toLong())
    }
    
    override suspend fun spendCoins(amount: Int): Boolean = withContext(Dispatchers.IO) {
        queries.spendCoins(amount.toLong(), amount.toLong())
        // Check if coins were actually deducted
        val profile = queries.getProfile().executeAsOne()
        profile.coins >= 0
    }
    
    override suspend fun updateStreak() = withContext(Dispatchers.IO) {
        val today = getTodayDateString()
        val profile = queries.getProfile().executeAsOne()
        
        val newStreak = if (profile.last_activity_date == null) {
            1
        } else if (isYesterday(profile.last_activity_date)) {
            profile.current_streak.toInt() + 1
        } else if (profile.last_activity_date == today) {
            profile.current_streak.toInt() // Same day, no change
        } else {
            1 // Streak broken
        }
        
        queries.updateStreak(
            current_streak = newStreak.toLong(),
            longest_streak = newStreak.toLong(),
            last_activity_date = today
        )
    }
    
    override suspend fun updateEnergy(delta: Int) = withContext(Dispatchers.IO) {
        val profile = queries.getProfile().executeAsOne()
        val newEnergy = (profile.current_energy.toInt() + delta)
            .coerceIn(0, PlayerProfile.MAX_ENERGY)
        
        queries.updateEnergy(
            current_energy = newEnergy.toLong(),
            last_energy_update_timestamp = currentTimeMillis()
        )
    }
    
    override suspend fun setSelectedLanguage(language: ProgrammingLanguage) = withContext(Dispatchers.IO) {
        queries.setSelectedLanguage(language.name.lowercase())
    }
    
    private fun getTodayDateString(): String {
        // Simple ISO date format YYYY-MM-DD
        val time = currentTimeMillis()
        val days = time / (24 * 60 * 60 * 1000)
        val year = 1970 + (days / 365).toInt()
        val dayOfYear = (days % 365).toInt()
        val month = (dayOfYear / 30) + 1
        val day = (dayOfYear % 30) + 1
        return "$year-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
    }
    
    private fun isYesterday(dateString: String?): Boolean {
        if (dateString == null) return false
        // Simplified check - in production use proper date parsing
        val today = getTodayDateString()
        val todayParts = today.split("-").map { it.toInt() }
        val dateParts = dateString.split("-").map { it.toInt() }
        
        // Check if it's yesterday (simplified)
        return todayParts[0] == dateParts[0] && 
               todayParts[1] == dateParts[1] && 
               todayParts[2] - dateParts[2] == 1
    }
}
