package com.abramyango.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.abramyango.ui.navigation.Route
import com.abramyango.ui.screens.task.TaskIntent
import com.abramyango.ui.screens.task.TaskScreen
import com.abramyango.ui.screens.task.TaskSideEffect
import com.abramyango.ui.screens.task.TaskViewModel
import com.abramyango.ui.screens.worlddetail.WorldDetailIntent
import com.abramyango.ui.screens.worlddetail.WorldDetailScreen
import com.abramyango.ui.screens.worlddetail.WorldDetailSideEffect
import com.abramyango.ui.screens.worlddetail.WorldDetailViewModel
import com.abramyango.ui.screens.worldmap.WorldMapScreen
import com.abramyango.ui.screens.worldmap.WorldMapSideEffect
import com.abramyango.ui.screens.worldmap.WorldMapViewModel
import com.abramyango.ui.theme.AbramyanGoTheme
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

/**
 * Главная точка входа в Compose UI
 */
@Composable
fun App() {
    AbramyanGoTheme {
        val navController = rememberNavController()
        
        NavHost(
            navController = navController,
            startDestination = Route.WorldMap
        ) {
            // Карта миров
            composable<Route.WorldMap> {
                val viewModel: WorldMapViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()
                
                LaunchedEffect(Unit) {
                    viewModel.sideEffect.collectLatest { effect ->
                        when (effect) {
                            is WorldMapSideEffect.NavigateToWorld -> {
                                navController.navigate(Route.WorldDetail(effect.worldId))
                            }
                            is WorldMapSideEffect.NavigateToProfile -> {
                                navController.navigate(Route.Profile)
                            }
                            is WorldMapSideEffect.NavigateToSettings -> {
                                navController.navigate(Route.Settings)
                            }
                            is WorldMapSideEffect.ShowError -> {
                                // TODO: показать snackbar
                            }
                        }
                    }
                }
                
                WorldMapScreen(
                    state = state,
                    onIntent = viewModel::processIntent
                )
            }
            
            // Детали мира
            composable<Route.WorldDetail> { backStackEntry ->
                val route = backStackEntry.toRoute<Route.WorldDetail>()
                val viewModel: WorldDetailViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()

                LaunchedEffect(route.worldId) {
                    viewModel.processIntent(WorldDetailIntent.LoadTasks(route.worldId))
                }

                LaunchedEffect(Unit) {
                    viewModel.sideEffect.collectLatest { effect ->
                        when (effect) {
                            is WorldDetailSideEffect.NavigateToTask -> {
                                navController.navigate(Route.Task(effect.worldId, effect.taskId))
                            }
                            is WorldDetailSideEffect.NavigateBack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }

                WorldDetailScreen(
                    state = state,
                    onIntent = viewModel::processIntent
                )
            }
            
            // Экран задачи
            composable<Route.Task> { backStackEntry ->
                val route = backStackEntry.toRoute<Route.Task>()
                val viewModel: TaskViewModel = koinViewModel()
                val state by viewModel.state.collectAsState()
                
                LaunchedEffect(route.taskId) {
                    viewModel.processIntent(TaskIntent.LoadTask(route.taskId))
                }
                
                LaunchedEffect(Unit) {
                    viewModel.sideEffect.collectLatest { effect ->
                        when (effect) {
                            is TaskSideEffect.NavigateBack -> {
                                navController.popBackStack()
                            }
                            is TaskSideEffect.NavigateToNextTask -> {
                                navController.navigate(Route.Task(route.worldId, effect.taskId)) {
                                    popUpTo(Route.WorldDetail(route.worldId))
                                }
                            }
                            is TaskSideEffect.ShowReward -> {
                                // Анимация награды показывается в TaskScreen
                            }
                            is TaskSideEffect.PlaySound -> {
                                // TODO: воспроизвести звук
                            }
                            is TaskSideEffect.TriggerHaptic -> {
                                // TODO: haptic feedback
                            }
                            is TaskSideEffect.ShowError -> {
                                // TODO: показать ошибку
                            }
                        }
                    }
                }
                
                TaskScreen(
                    state = state,
                    onIntent = viewModel::processIntent
                )
            }
            
            // Профиль
            composable<Route.Profile> {
                // TODO: ProfileScreen
                ProfilePlaceholder(
                    onBack = { navController.popBackStack() }
                )
            }
            
            // Настройки
            composable<Route.Settings> {
                // TODO: SettingsScreen
                SettingsPlaceholder(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

// Временные заглушки для экранов
@Composable
private fun ProfilePlaceholder(onBack: () -> Unit) {
    Column(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text("Профиль")
        Spacer(
            modifier = androidx.compose.ui.Modifier.height(16.dp)
        )
        com.abramyango.ui.components.SecondaryButton(
            text = "Назад",
            onClick = onBack
        )
    }
}

@Composable
private fun SettingsPlaceholder(onBack: () -> Unit) {
    Column(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text("Настройки")
        Spacer(
            modifier = androidx.compose.ui.Modifier.height(16.dp)
        )
        com.abramyango.ui.components.SecondaryButton(
            text = "Назад",
            onClick = onBack
        )
    }
}
