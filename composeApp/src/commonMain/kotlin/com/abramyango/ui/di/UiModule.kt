package com.abramyango.ui.di

import abramyango.composeapp.generated.resources.Res
import com.abramyango.data.repository.CategoryRepositoryImpl
import com.abramyango.data.repository.TaskRepositoryImpl
import com.abramyango.domain.repository.CategoryRepository
import com.abramyango.domain.repository.TaskRepository
import com.abramyango.ui.screens.categories.CategoriesViewModel
import com.abramyango.ui.screens.categorytasklist.CategoryTaskListViewModel
import com.abramyango.ui.screens.task.TaskViewModel
import com.abramyango.ui.screens.taskdetail.TaskDetailViewModel
import com.abramyango.ui.screens.worlddetail.WorldDetailViewModel
import com.abramyango.ui.screens.worldmap.WorldMapViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@OptIn(ExperimentalResourceApi::class)
val uiModule = module {
    // CategoryRepository with dynamic JSON loading from compose resources
    single<CategoryRepository> {
        CategoryRepositoryImpl(
            categoriesLoader = {
                Res.readBytes("files/categories.json").decodeToString()
            },
            categoryTasksLoader = { id ->
                Res.readBytes("files/$id.json").decodeToString()
            }
        )
    }

    // TaskRepository with JSON resource loading (legacy)
    single<TaskRepository> {
        TaskRepositoryImpl(get(), get(), jsonLoader = {
            listOf(
                Res.readBytes("files/begin_1-40.json").decodeToString()
            )
        })
    }

    // New category-based ViewModels
    viewModel { CategoriesViewModel(get()) }
    viewModel { CategoryTaskListViewModel(get()) }
    viewModel { TaskDetailViewModel(get()) }

    // Legacy ViewModels
    viewModel { WorldMapViewModel(get(), get()) }
    viewModel { TaskViewModel(get(), get(), get()) }
    viewModel { WorldDetailViewModel(get()) }
}
