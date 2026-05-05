package tj.abramyan.go.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import tj.abramyan.go.ui.screens.categories.CategoriesScreen
import tj.abramyan.go.ui.screens.categories.CategoriesSideEffect
import tj.abramyan.go.ui.screens.categories.CategoriesViewModel
import tj.abramyan.go.ui.screens.categorytasklist.CategoryTaskListIntent
import tj.abramyan.go.ui.screens.categorytasklist.CategoryTaskListScreen
import tj.abramyan.go.ui.screens.categorytasklist.CategoryTaskListSideEffect
import tj.abramyan.go.ui.screens.categorytasklist.CategoryTaskListViewModel
import tj.abramyan.go.ui.screens.taskdetail.TaskDetailIntent
import tj.abramyan.go.ui.screens.taskdetail.TaskDetailScreen
import tj.abramyan.go.ui.screens.taskdetail.TaskDetailSideEffect
import tj.abramyan.go.ui.screens.taskdetail.TaskDetailViewModel
import tj.abramyan.go.ui.theme.AbramyanGoTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable sealed class Route {
    @Serializable data object Categories : Route()
    @Serializable data class CategoryTaskList(val categoryId: String, val categoryName: String) : Route()
    @Serializable data class TaskDetail(val categoryId: String, val taskIndex: Int) : Route()
}

@Composable
fun App() {
    AbramyanGoTheme {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = Route.Categories) {
            composable<Route.Categories> {
                val viewModel: CategoriesViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()
                LaunchedEffect(Unit) {
                    viewModel.sideEffect.collectLatest { effect ->
                        when (effect) {
                            is CategoriesSideEffect.NavigateToTaskList ->
                                navController.navigate(Route.CategoryTaskList(effect.categoryId, effect.categoryName))
                        }
                    }
                }
                CategoriesScreen(state = state, onIntent = viewModel::processIntent)
            }

            composable<Route.CategoryTaskList> { backStackEntry ->
                val route = backStackEntry.toRoute<Route.CategoryTaskList>()
                val viewModel: CategoryTaskListViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()
                LaunchedEffect(route.categoryId) {
                    viewModel.processIntent(CategoryTaskListIntent.LoadTasks(route.categoryId, route.categoryName))
                }
                LaunchedEffect(Unit) {
                    viewModel.sideEffect.collectLatest { effect ->
                        when (effect) {
                            is CategoryTaskListSideEffect.NavigateToTaskDetail ->
                                navController.navigate(Route.TaskDetail(effect.categoryId, effect.taskIndex))
                            is CategoryTaskListSideEffect.NavigateBack ->
                                navController.popBackStack()
                        }
                    }
                }
                CategoryTaskListScreen(state = state, onIntent = viewModel::processIntent)
            }

            composable<Route.TaskDetail> { backStackEntry ->
                val route = backStackEntry.toRoute<Route.TaskDetail>()
                val viewModel: TaskDetailViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()
                LaunchedEffect(route.categoryId, route.taskIndex) {
                    viewModel.processIntent(TaskDetailIntent.LoadTask(route.categoryId, route.taskIndex))
                }
                LaunchedEffect(Unit) {
                    viewModel.sideEffect.collectLatest { effect ->
                        when (effect) {
                            is TaskDetailSideEffect.NavigateBack -> navController.popBackStack()
                        }
                    }
                }
                TaskDetailScreen(state = state, onIntent = viewModel::processIntent)
            }
        }
    }
}
