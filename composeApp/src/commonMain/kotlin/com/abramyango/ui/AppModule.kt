package com.abramyango.ui

import abramyango.composeapp.generated.resources.Res
import com.abramyango.data.CategoryRepository
import com.abramyango.data.CategoryRepositoryImpl
import com.abramyango.ui.screens.categories.CategoriesViewModel
import com.abramyango.ui.screens.categorytasklist.CategoryTaskListViewModel
import com.abramyango.ui.screens.taskdetail.TaskDetailViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@OptIn(ExperimentalResourceApi::class)
val appModule = module {
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

    viewModel { CategoriesViewModel(get()) }
    viewModel { CategoryTaskListViewModel(get()) }
    viewModel { TaskDetailViewModel(get()) }
}
