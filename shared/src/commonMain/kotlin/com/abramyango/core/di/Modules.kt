package com.abramyango.core.di

import com.abramyango.data.db.AbramyanGoDatabase
import com.abramyango.data.db.DatabaseDriverFactory
import com.abramyango.data.json.TasksDataSource
import com.abramyango.data.json.WorldsDataSource
import com.abramyango.data.repository.*
import com.abramyango.domain.repository.*
import com.abramyango.domain.usecase.*
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Общий модуль зависимостей для всех платформ
 */
val sharedModule = module {
    // Database
    single { get<DatabaseDriverFactory>().createDriver() }
    single { AbramyanGoDatabase(get()) }
    
    // Data Sources
    single { WorldsDataSource() }
    single { TasksDataSource() }
    
    // Repositories
    single<PlayerRepository> { PlayerRepositoryImpl(get()) }
    single<WorldRepository> { WorldRepositoryImpl(get(), get()) }
    // TaskRepository is created in uiModule (composeApp) to support JSON resource loading
    // single<TaskRepository> { TaskRepositoryImpl(get(), get()) }
    single<AbilityRepository> { AbilityRepositoryImpl(get()) }
    single<AchievementRepository> { AchievementRepositoryImpl(get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    
    // Use Cases
    factory { SolveTaskUseCase(get(), get(), get(), get()) }
    factory { GetWorldProgressUseCase(get(), get()) }
    factory { UseAbilityUseCase(get(), get()) }
    factory { PurchaseAbilityUseCase(get(), get()) }
}

/**
 * Платформо-специфичный модуль (будет определён в androidMain/iosMain)
 */
expect val platformModule: Module
