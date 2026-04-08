package com.abramyango.ui.screens.taskdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.abramyango.domain.model.Solution
import com.abramyango.ui.components.GlassCard
import com.abramyango.ui.components.GlassIconButton
import com.abramyango.ui.theme.AppTheme
import com.abramyango.ui.theme.Spacing

@Composable
fun TaskDetailScreen(
    state: TaskDetailState,
    onIntent: (TaskDetailIntent) -> Unit,
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
                    GlassIconButton(onClick = { onIntent(TaskDetailIntent.Back) }) {
                        Text(
                            text = "<-",
                            style = typography.titleLarge,
                            color = colors.textPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(Spacing.medium))
                    Text(
                        text = state.task?.title ?: "",
                        style = typography.titleMedium,
                        color = colors.textPrimary
                    )
                }
            }

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colors.accentPrimary)
                }
            } else if (state.task == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = state.error ?: "Task not found",
                        style = typography.bodyLarge,
                        color = colors.accentError
                    )
                }
            } else {
                val task = state.task
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Spacing.default),
                    contentPadding = PaddingValues(
                        top = Spacing.small,
                        bottom = Spacing.massive
                    ),
                    verticalArrangement = Arrangement.spacedBy(Spacing.medium)
                ) {
                    // Task description
                    item {
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            glassAlpha = 0.1f,
                            cornerRadius = 16.dp,
                            contentPadding = Spacing.default
                        ) {
                            Text(
                                text = task.description,
                                style = typography.bodyLarge,
                                color = colors.textPrimary
                            )
                        }
                    }

                    // Solutions header
                    item {
                        Text(
                            text = "Solutions",
                            style = typography.titleMedium,
                            color = colors.textSecondary,
                            modifier = Modifier.padding(top = Spacing.small)
                        )
                    }

                    // Collapsible solution buttons
                    items(
                        items = task.solutions,
                        key = { it.language }
                    ) { solution ->
                        SolutionItem(
                            solution = solution,
                            isExpanded = solution.language in state.expandedSolutions,
                            onToggle = {
                                onIntent(TaskDetailIntent.ToggleSolution(solution.language))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SolutionItem(
    solution: Solution,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    val shape = RoundedCornerShape(12.dp)

    Column(modifier = Modifier.fillMaxWidth()) {
        // Language button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (isExpanded) {
                            listOf(
                                colors.accentPrimary.copy(alpha = 0.3f),
                                colors.accentPrimary.copy(alpha = 0.15f)
                            )
                        } else {
                            listOf(
                                Color.White.copy(alpha = 0.15f),
                                Color.White.copy(alpha = 0.08f)
                            )
                        }
                    )
                )
                .border(
                    width = 1.dp,
                    color = if (isExpanded) {
                        colors.accentPrimary.copy(alpha = 0.4f)
                    } else {
                        Color.White.copy(alpha = 0.2f)
                    },
                    shape = shape
                )
                .clickable { onToggle() }
                .padding(horizontal = Spacing.default, vertical = Spacing.medium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = solution.language,
                    style = typography.titleMedium,
                    color = if (isExpanded) colors.accentPrimary else colors.textPrimary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = if (isExpanded) "v" else ">",
                    style = typography.titleMedium,
                    color = colors.textTertiary
                )
            }
        }

        // Collapsible code block
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Spacing.extraSmall)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.codeBackground)
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    // Line numbers
                    val lines = solution.code.lines()
                    Column(
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        lines.forEachIndexed { index, _ ->
                            Text(
                                text = (index + 1).toString().padStart(2, ' '),
                                style = typography.codeBlock,
                                color = colors.textTertiary
                            )
                        }
                    }

                    // Code content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        lines.forEach { line ->
                            Text(
                                text = line,
                                style = typography.codeBlock,
                                color = colors.textPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
