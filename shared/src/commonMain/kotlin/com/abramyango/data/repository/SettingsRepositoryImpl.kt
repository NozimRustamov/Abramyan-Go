package com.abramyango.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import com.abramyango.data.db.AbramyanGoDatabase
import com.abramyango.domain.repository.AppTheme
import com.abramyango.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

data class AppSettings(
    val theme: AppTheme,
    val soundEnabled: Boolean,
    val notificationsEnabled: Boolean,
    val interfaceLanguage: String
)

class SettingsRepositoryImpl(
    private val database: AbramyanGoDatabase
) : SettingsRepository {
    
    private val queries = database.abramyanGoDatabaseQueries
    
    override fun getTheme(): Flow<AppTheme> {
        return queries.getSettings()
            .asFlow()
            .mapToOne(Dispatchers.IO)
            .map { entity ->
                when (entity.theme) {
                    "light" -> AppTheme.LIGHT
                    "dark" -> AppTheme.DARK
                    else -> AppTheme.SYSTEM
                }
            }
    }
    
    override suspend fun setTheme(theme: AppTheme) = withContext(Dispatchers.IO) {
        val themeString = when (theme) {
            AppTheme.LIGHT -> "light"
            AppTheme.DARK -> "dark"
            AppTheme.SYSTEM -> "system"
        }
        queries.updateTheme(themeString)
    }
    
    override fun getSoundEnabled(): Flow<Boolean> {
        return queries.getSettings()
            .asFlow()
            .mapToOne(Dispatchers.IO)
            .map { entity -> entity.sound_enabled == 1L }
    }
    
    override suspend fun setSoundEnabled(enabled: Boolean) = withContext(Dispatchers.IO) {
        queries.updateSoundEnabled(if (enabled) 1 else 0)
    }
    
    override fun getNotificationsEnabled(): Flow<Boolean> {
        return queries.getSettings()
            .asFlow()
            .mapToOne(Dispatchers.IO)
            .map { entity -> entity.notifications_enabled == 1L }
    }
    
    override suspend fun setNotificationsEnabled(enabled: Boolean) = withContext(Dispatchers.IO) {
        queries.updateNotificationsEnabled(if (enabled) 1 else 0)
    }
    
    override fun getInterfaceLanguage(): Flow<String> {
        return queries.getSettings()
            .asFlow()
            .mapToOne(Dispatchers.IO)
            .map { entity -> entity.interface_language }
    }
    
    override suspend fun setInterfaceLanguage(language: String) = withContext(Dispatchers.IO) {
        queries.updateInterfaceLanguage(language)
    }
}
