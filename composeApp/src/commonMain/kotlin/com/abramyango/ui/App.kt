package com.abramyango.ui

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.abramyango.ui.navigation.Route
import com.abramyango.ui.screens.task.TaskScreen
import com.abramyango.ui.screens.task.TaskViewModel
import com.abramyango.ui.screens.task.TaskIntent
import com.abramyango.ui.screens.worldmap.WorldMapScreen
import com.abramyango.ui.screens.worldmap.WorldMapViewModel
import com.abramyango.ui.screens.worldmap.WorldMapIntent
import com.abramyango.ui.screens.worldmap.WorldMapSideEffect
import com.abramyango.ui.screens.task.TaskSideEffect
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
                // TODO: WorldDetailScreen
                WorldDetailPlaceholder(
                    worldId = route.worldId,
                    onTaskClick = { taskId ->
                        navController.navigate(Route.Task(route.worldId, taskId))
                    },
                    onBack = { navController.popBackStack() }
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
private fun WorldDetailPlaceholder(
    worldId: String,
    onTaskClick: (String) -> Unit,
    onBack: () -> Unit
) {
    // TODO: реализовать WorldDetailScreen
    androidx.compose.foundation.layout.Column(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        androidx.compose.material3.Text("World: $worldId")
        androidx.compose.foundation.layout.Spacer(
            modifier = androidx.compose.ui.Modifier.height(16.dp)
        )
        com.abramyango.ui.components.PrimaryButton(
            text = "Первая задача",
            onClick = { onTaskClick("valley_begin_001") }
        )
        androidx.compose.foundation.layout.Spacer(
            modifier = androidx.compose.ui.Modifier.height(16.dp)
        )
        com.abramyango.ui.components.SecondaryButton(
            text = "Назад",
            onClick = onBack
        )
    }
}

@Composable
private fun ProfilePlaceholder(onBack: () -> Unit) {
    androidx.compose.foundation.layout.Column(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        androidx.compose.material3.Text("Профиль")
        androidx.compose.foundation.layout.Spacer(
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
    androidx.compose.foundation.layout.Column(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        androidx.compose.material3.Text("Настройки")
        androidx.compose.foundation.layout.Spacer(
            modifier = androidx.compose.ui.Modifier.height(16.dp)
        )
        com.abramyango.ui.components.SecondaryButton(
            text = "Назад",
            onClick = onBack
        )
    }
}

private val androidx.compose.ui.Modifier.Companion.fillMaxSize: androidx.compose.ui.Modifier
    get() = this.then(androidx.compose.foundation.layout.fillMaxSize())

private val Int.dp: androidx.compose.ui.unit.Dp
    get() = androidx.compose.ui.unit.dp.times(this.toFloat())
