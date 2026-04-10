package com.abramyango.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.abramyango.ui.navigation.Route
import com.abramyango.ui.screens.categories.CategoriesScreen
import com.abramyango.ui.screens.categories.CategoriesSideEffect
import com.abramyango.ui.screens.categories.CategoriesViewModel
import com.abramyango.ui.screens.categorytasklist.CategoryTaskListIntent
import com.abramyango.ui.screens.categorytasklist.CategoryTaskListScreen
import com.abramyango.ui.screens.categorytasklist.CategoryTaskListSideEffect
import com.abramyango.ui.screens.categorytasklist.CategoryTaskListViewModel
import com.abramyango.ui.screens.taskdetail.TaskDetailIntent
import com.abramyango.ui.screens.taskdetail.TaskDetailScreen
import com.abramyango.ui.screens.taskdetail.TaskDetailSideEffect
import com.abramyango.ui.screens.taskdetail.TaskDetailViewModel
import com.abramyango.ui.theme.AbramyanGoTheme
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    AbramyanGoTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Route.Categories
        ) {
            // Categories list (start screen)
            composable<Route.Categories> {
                val viewModel: CategoriesViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.sideEffect.collectLatest { effect ->
                        when (effect) {
                            is CategoriesSideEffect.NavigateToTaskList -> {
                                navController.navigate(
                                    Route.CategoryTaskList(
                                        effect.categoryId,
                                        effect.categoryName
                                    )
                                )
                            }
                        }
                    }
                }

                CategoriesScreen(
                    state = state,
                    onIntent = viewModel::processIntent
                )
            }

            // Task list for a category
            composable<Route.CategoryTaskList> { backStackEntry ->
                val route = backStackEntry.toRoute<Route.CategoryTaskList>()
                val viewModel: CategoryTaskListViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()

                LaunchedEffect(route.categoryId) {
                    viewModel.processIntent(
                        CategoryTaskListIntent.LoadTasks(route.categoryId, route.categoryName)
                    )
                }

                LaunchedEffect(Unit) {
                    viewModel.sideEffect.collectLatest { effect ->
                        when (effect) {
                            is CategoryTaskListSideEffect.NavigateToTaskDetail -> {
                                navController.navigate(
                                    Route.TaskDetail(effect.categoryId, effect.taskIndex)
                                )
                            }
                            is CategoryTaskListSideEffect.NavigateBack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }

                CategoryTaskListScreen(
                    state = state,
                    onIntent = viewModel::processIntent
                )
            }

            // Task detail with solutions
            composable<Route.TaskDetail> { backStackEntry ->
                val route = backStackEntry.toRoute<Route.TaskDetail>()
                val viewModel: TaskDetailViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()

                LaunchedEffect(route.categoryId, route.taskIndex) {
                    viewModel.processIntent(
                        TaskDetailIntent.LoadTask(route.categoryId, route.taskIndex)
                    )
                }

                LaunchedEffect(Unit) {
                    viewModel.sideEffect.collectLatest { effect ->
                        when (effect) {
                            is TaskDetailSideEffect.NavigateBack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }

                TaskDetailScreen(
                    state = state,
                    onIntent = viewModel::processIntent
                )
            }

        }
    }
}
