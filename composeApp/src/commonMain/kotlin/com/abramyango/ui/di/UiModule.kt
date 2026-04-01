package com.abramyango.ui.di

import com.abramyango.ui.screens.task.TaskViewModel
import com.abramyango.ui.screens.worldmap.WorldMapViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin модуль для UI layer
 */
val uiModule = module {
    viewModel { WorldMapViewModel(get(), get()) }
    viewModel { TaskViewModel(get(), get(), get()) }
}
