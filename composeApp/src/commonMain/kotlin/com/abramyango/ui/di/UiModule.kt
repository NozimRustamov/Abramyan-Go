package com.abramyango.ui.di

import abramyango.composeapp.generated.resources.Res
import com.abramyango.data.repository.CategoryRepositoryImpl
import com.abramyango.domain.repository.CategoryRepository
import com.abramyango.ui.screens.categories.CategoriesViewModel
import com.abramyango.ui.screens.categorytasklist.CategoryTaskListViewModel
import com.abramyango.ui.screens.taskdetail.TaskDetailViewModel
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

    // ViewModels
    viewModel { CategoriesViewModel(get()) }
    viewModel { CategoryTaskListViewModel(get()) }
    viewModel { TaskDetailViewModel(get()) }
}
