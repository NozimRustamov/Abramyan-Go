package com.abramyango.ui.screens.task

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.abramyango.domain.model.Task
import com.abramyango.domain.model.TaskMechanic
import com.abramyango.ui.components.ComboIndicator
import com.abramyango.ui.components.GlassCard
import com.abramyango.ui.components.GlassIconButton
import com.abramyango.ui.components.GlassSurface
import com.abramyango.ui.components.PrimaryButton
import com.abramyango.ui.components.SecondaryButton
import com.abramyango.ui.screens.task.mechanics.BugHuntMechanic
import com.abramyango.ui.screens.task.mechanics.DragDropMechanic
import com.abramyango.ui.screens.task.mechanics.FillBlankMechanic
import com.abramyango.ui.theme.AppTheme
import com.abramyango.ui.theme.Spacing

/**
 * Экран задачи - основной геймплей
 */
@Composable
fun TaskScreen(
    state: TaskState,
    onIntent: (TaskIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

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
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = colors.accentPrimary
            )
        } else if (state.task != null) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Top bar
                TaskTopBar(
                    task = state.task,
                    comboState = state.comboState,
                    onBack = { onIntent(TaskIntent.ExitTask) }
                )

                // Main content
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(Spacing.default)
                ) {
                    when (state.taskPhase) {
                        TaskPhase.SOLVING, TaskPhase.CHECKING -> {
                            TaskContent(
                                task = state.task,
                                onSubmit = { answer ->
                                    onIntent(TaskIntent.SubmitAnswer(answer))
                                },
                                onHint = { onIntent(TaskIntent.UseHint) }
                            )
                        }

                        TaskPhase.CORRECT -> {
                            CorrectAnswerOverlay(
                                reward = state.lastReward,
                                onNext = { onIntent(TaskIntent.NextTask) }
                            )
                        }

                        TaskPhase.INCORRECT -> {
                            IncorrectAnswerOverlay(
                                onRetry = { onIntent(TaskIntent.RetryTask) }
                            )
                        }

                        TaskPhase.COMPLETED -> {
                            // Задача завершена
                        }
                    }
                }
            }
        }
    }
}

/**
 * Top bar задачи: ID + механика + комбо
 */
@Composable
private fun TaskTopBar(
    task: Task,
    comboState: com.abramyango.domain.model.ComboState,
    onBack: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

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
            GlassIconButton(onClick = onBack) {
                Text(
                    text = "\u2190",
                    style = typography.titleLarge,
                    color = colors.textPrimary
                )
            }

            Spacer(modifier = Modifier.width(Spacing.medium))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = getMechanicName(task.mechanic),
                    style = typography.labelMedium,
                    color = colors.textSecondary
                )
                Text(
                    text = task.id,
                    style = typography.titleMedium,
                    color = colors.textPrimary
                )
            }

            if (comboState.count > 0) {
                ComboIndicator(
                    comboCount = comboState.count,
                    multiplier = comboState.multiplier
                )
            }

            Spacer(modifier = Modifier.width(Spacing.small))

            GlassSurface(
                cornerRadius = 8.dp,
                contentPadding = 8.dp
            ) {
                Text(
                    text = "+${task.xpReward} XP",
                    style = typography.labelMedium,
                    color = colors.accentPrimary
                )
            }
        }
    }
}

/**
 * Основной контент задачи: заголовок, rpg-контекст, механика
 */
@Composable
private fun TaskContent(
    task: Task,
    onSubmit: (UserAnswer) -> Unit,
    onHint: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Заголовок: abramyan_text
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glassAlpha = 0.15f,
                cornerRadius = 16.dp,
                contentPadding = Spacing.medium
            ) {
                Text(
                    text = task.abramyanText,
                    style = typography.titleMedium,
                    color = colors.textPrimary
                )
            }
            Spacer(modifier = Modifier.height(Spacing.default))
        }

        // RPG-контекст
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glassAlpha = 0.1f,
                cornerRadius = 16.dp,
                contentPadding = Spacing.medium
            ) {
                Row {
                    Text(
                        text = "\uD83D\uDCAC",
                        style = typography.titleLarge
                    )
                    Spacer(modifier = Modifier.width(Spacing.small))
                    Text(
                        text = task.rpgContext,
                        style = typography.bodyLarge,
                        color = colors.textPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(Spacing.large))
        }

        // Описание задачи (если есть)
        val description = task.taskData.description
        if (description != null) {
            item {
                Text(
                    text = description,
                    style = typography.bodyMedium,
                    color = colors.textSecondary
                )
                Spacer(modifier = Modifier.height(Spacing.default))
            }
        }

        // Рабочая зона: механика
        item {
            when (task.mechanic) {
                TaskMechanic.DRAG_DROP -> {
                    DragDropMechanic(
                        task = task,
                        onSubmit = { blockIds ->
                            onSubmit(UserAnswer.DragDropAnswer(blockIds))
                        }
                    )
                }

                TaskMechanic.BUG_HUNT -> {
                    BugHuntMechanic(
                        task = task,
                        onSubmit = { option ->
                            onSubmit(UserAnswer.BugHuntAnswer(option))
                        }
                    )
                }

                TaskMechanic.FILL_BLANK -> {
                    FillBlankMechanic(
                        task = task,
                        onSubmit = { option ->
                            onSubmit(UserAnswer.FillBlankAnswer(option))
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.extraLarge))
        }

        // Hint button
        item {
            Spacer(modifier = Modifier.height(Spacing.extraLarge))
            SecondaryButton(
                text = "\uD83D\uDCA1 Подсказка",
                onClick = onHint
            )
            Spacer(modifier = Modifier.height(Spacing.default))
        }
    }
}

/**
 * Оверлей правильного ответа
 */
@Composable
private fun CorrectAnswerOverlay(
    reward: com.abramyango.domain.usecase.TaskReward?,
    onNext: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 300f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .scale(scale),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "\u2705", style = typography.displayLarge)

        Spacer(modifier = Modifier.height(Spacing.default))

        Text(
            text = "Правильно!",
            style = typography.headlineLarge,
            color = colors.accentSuccess
        )

        if (reward != null) {
            Spacer(modifier = Modifier.height(Spacing.large))

            GlassCard(
                glassAlpha = 0.15f,
                cornerRadius = 16.dp,
                contentPadding = Spacing.default
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "+${reward.xpEarned} XP",
                        style = typography.headlineMedium,
                        color = colors.xpBar
                    )

                    if (reward.coinsEarned > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "+${reward.coinsEarned} \uD83E\uDE99",
                            style = typography.titleMedium,
                            color = colors.textSecondary
                        )
                    }

                    if (reward.isFirstAttempt) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "С первой попытки! \uD83C\uDFAF",
                            style = typography.labelMedium,
                            color = colors.accentSuccess
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(Spacing.extraLarge))

        PrimaryButton(
            text = "Продолжить",
            onClick = onNext,
            modifier = Modifier.padding(horizontal = Spacing.huge)
        )
    }
}

/**
 * Оверлей неправильного ответа
 */
@Composable
private fun IncorrectAnswerOverlay(
    onRetry: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "\u274C", style = typography.displayLarge)

        Spacer(modifier = Modifier.height(Spacing.default))

        Text(
            text = "Попробуй ещё раз",
            style = typography.headlineLarge,
            color = colors.accentError
        )

        Text(
            text = "Ошибки не тратят энергию",
            style = typography.bodyMedium,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(Spacing.extraLarge))

        PrimaryButton(
            text = "Ещё раз",
            onClick = onRetry,
            modifier = Modifier.padding(horizontal = Spacing.huge)
        )
    }
}

private fun getMechanicName(mechanic: TaskMechanic): String {
    return when (mechanic) {
        TaskMechanic.DRAG_DROP -> "Собери код"
        TaskMechanic.BUG_HUNT -> "Найди ошибку"
        TaskMechanic.FILL_BLANK -> "Заполни пропуск"
    }
}
