package tj.abramyan.go.ui

import tj.abramyan.go.shared.resources.Res
import tj.abramyan.go.data.CategoryRepository
import tj.abramyan.go.data.CategoryRepositoryImpl
import tj.abramyan.go.ui.screens.categories.CategoriesViewModel
import tj.abramyan.go.ui.screens.categorytasklist.CategoryTaskListViewModel
import tj.abramyan.go.ui.screens.taskdetail.TaskDetailViewModel
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
