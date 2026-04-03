package com.abramyango.ui.screens.worlddetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.abramyango.domain.model.Task
import com.abramyango.domain.model.TaskMechanic
import com.abramyango.ui.components.GlassCard
import com.abramyango.ui.components.GlassIconButton
import com.abramyango.ui.theme.AppTheme
import com.abramyango.ui.theme.Spacing

@Composable
fun WorldDetailScreen(
    state: WorldDetailState,
    onIntent: (WorldDetailIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colors.backgroundGradientStart,
                        colors.backgroundGradientEnd
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.default),
                glassAlpha = 0.12f,
                cornerRadius = 16.dp,
                contentPadding = Spacing.medium
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GlassIconButton(onClick = { onIntent(WorldDetailIntent.Back) }) {
                        Text(
                            text = "←",
                            style = typography.titleLarge,
                            color = colors.textPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(Spacing.medium))
                    Text(
                        text = getWorldDisplayName(state.worldId),
                        style = typography.titleMedium,
                        color = colors.textPrimary
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${state.tasks.size} задач",
                        style = typography.labelMedium,
                        color = colors.textSecondary
                    )
                }
            }

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colors.accentPrimary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Spacing.default),
                    contentPadding = PaddingValues(
                        top = Spacing.small,
                        bottom = Spacing.massive
                    ),
                    verticalArrangement = Arrangement.spacedBy(Spacing.small)
                ) {
                    items(items = state.tasks, key = { it.id }) { task ->
                        TaskListItem(
                            task = task,
                            onClick = { onIntent(WorldDetailIntent.SelectTask(task.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskListItem(
    task: Task,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        glassAlpha = 0.1f,
        cornerRadius = 12.dp,
        contentPadding = Spacing.medium
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Order number
            Text(
                text = "${task.order}",
                style = typography.headlineMedium,
                color = colors.accentPrimary,
                modifier = Modifier.width(36.dp)
            )

            Spacer(modifier = Modifier.width(Spacing.small))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.abramyanId ?: task.id,
                    style = typography.titleMedium,
                    color = colors.textPrimary
                )
                Text(
                    text = getMechanicName(task.mechanic),
                    style = typography.bodyMedium,
                    color = colors.textSecondary
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = difficultyStars(task.difficulty),
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
                Text(
                    text = "+${task.xpReward} XP",
                    style = typography.labelMedium,
                    color = colors.accentPrimary
                )
            }

            Spacer(modifier = Modifier.width(Spacing.small))
            Text(
                text = "→",
                style = typography.titleLarge,
                color = colors.textTertiary
            )
        }
    }
}

private fun difficultyStars(difficulty: Int): String {
    val filled = "★".repeat(difficulty.coerceIn(1, 5))
    val empty = "☆".repeat((5 - difficulty).coerceAtLeast(0))
    return filled + empty
}

private fun getMechanicName(mechanic: TaskMechanic): String {
    return when (mechanic) {
        TaskMechanic.DRAG_DROP -> "Собери код"
        TaskMechanic.FILL_BLANKS -> "Заполни пропуски"
        TaskMechanic.BUG_HUNT -> "Найди ошибку"
        TaskMechanic.CODE_TRACE -> "Трассировка"
        TaskMechanic.OUTPUT_PREDICTION -> "Что выведет код?"
        TaskMechanic.REFACTORING -> "Рефакторинг"
    }
}

private fun getWorldDisplayName(worldId: String): String {
    return when (worldId) {
        "valley_of_beginnings" -> "Долина Начинаний"
        "fields_of_truth" -> "Поля Истины"
        "crossroads_of_fate" -> "Развилка Судьбы"
        "time_loop" -> "Петля Времени"
        "data_river" -> "Река Данных"
        "ocean_of_arrays" -> "Океан Массивов"
        "matrix_citadel" -> "Цитадель Матриц"
        "text_forest" -> "Текстовый Лес"
        "architect_archives" -> "Архивы Архитектора"
        else -> worldId
    }
}
