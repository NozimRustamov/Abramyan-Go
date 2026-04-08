package com.abramyango.ui.di

import abramyango.composeapp.generated.resources.Res
import com.abramyango.data.repository.TaskRepositoryImpl
import com.abramyango.domain.repository.TaskRepository
import com.abramyango.ui.screens.task.TaskViewModel
import com.abramyango.ui.screens.worlddetail.WorldDetailViewModel
import com.abramyango.ui.screens.worldmap.WorldMapViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin модуль для UI layer
 */
@OptIn(ExperimentalResourceApi::class)
val uiModule = module {
    // TaskRepository с загрузкой JSON из compose resources
    single<TaskRepository> {
        TaskRepositoryImpl(get(), get(), jsonLoader = {
            listOf(
                Res.readBytes("files/begin_1-40.json").decodeToString()
            )
            // Сюда можно добавить новые JSON файлы:
            // Res.readBytes("files/integer 1-40.json").decodeToString(),
            // Res.readBytes("files/boolean 1-40.json").decodeToString(),
        })
    }

    viewModel { WorldMapViewModel(get(), get()) }
    viewModel { TaskViewModel(get(), get(), get()) }
    viewModel { WorldDetailViewModel(get()) }
}
