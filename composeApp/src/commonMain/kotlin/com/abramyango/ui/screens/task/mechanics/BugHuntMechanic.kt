package com.abramyango.ui.screens.task.mechanics

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.abramyango.domain.model.Task
import com.abramyango.ui.components.GlassCard
import com.abramyango.ui.components.PrimaryButton
import com.abramyango.ui.theme.AppTheme
import com.abramyango.ui.theme.Spacing

/**
 * Bug Hunt механика - найди и исправь ошибку.
 * Показывает code_with_bug с подсвеченной строкой bug_line_index,
 * предлагает bug_options для выбора правильного исправления.
 */
@Composable
fun BugHuntMechanic(
    task: Task,
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    val data = task.taskData
    val codeWithBug = data.codeWithBug ?: ""
    val bugLineIndex = data.bugLineIndex ?: 0
    val bugOptions = data.bugOptions ?: emptyList()
    val lines = codeWithBug.lines()

    var selectedOption by remember(task.id) { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Инструкция
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glassAlpha = 0.12f,
            cornerRadius = 12.dp,
            contentPadding = Spacing.medium
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "\uD83D\uDC1B", style = typography.headlineMedium)
                Spacer(modifier = Modifier.width(Spacing.small))
                Column {
                    Text(
                        text = "Найди ошибку!",
                        style = typography.titleMedium,
                        color = colors.textPrimary
                    )
                    Text(
                        text = "Выбери правильное исправление для подсвеченной строки",
                        style = typography.bodyMedium,
                        color = colors.textSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Spacing.default))

        // Код с ошибкой
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glassAlpha = 0.08f,
            cornerRadius = 12.dp,
            contentPadding = 0.dp
        ) {
            Column {
                lines.forEachIndexed { index, line ->
                    val isBugLine = index == bugLineIndex

                    BugCodeLine(
                        lineNumber = index + 1,
                        code = line,
                        isBugLine = isBugLine
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Spacing.large))

        // Варианты исправления
        Text(
            text = "Выбери правильное исправление строки ${bugLineIndex + 1}:",
            style = typography.labelMedium,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(Spacing.small))

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            bugOptions.forEach { option ->
                val isSelected = selectedOption == option

                BugOptionItem(
                    text = option,
                    isSelected = isSelected,
                    onClick = { selectedOption = option }
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.extraLarge))

        // Кнопка проверки
        PrimaryButton(
            text = "Исправить! \uD83C\uDFAF",
            onClick = { selectedOption?.let { onSubmit(it) } },
            enabled = selectedOption != null
        )
    }
}

/**
 * Строка кода с подсветкой строки с ошибкой
 */
@Composable
private fun BugCodeLine(
    lineNumber: Int,
    code: String,
    isBugLine: Boolean
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    val backgroundColor by animateColorAsState(
        targetValue = if (isBugLine) {
            colors.accentError.copy(alpha = 0.2f)
        } else {
            Color.Transparent
        }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = Spacing.medium, vertical = Spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = lineNumber.toString().padStart(2, ' '),
            style = typography.codeBlock,
            color = if (isBugLine) colors.accentError else colors.textTertiary,
            modifier = Modifier.width(32.dp)
        )

        Box(
            modifier = Modifier
                .width(1.dp)
                .height(20.dp)
                .background(Color.White.copy(alpha = 0.1f))
        )

        Spacer(modifier = Modifier.width(Spacing.medium))

        Text(
            text = code,
            style = typography.codeBlock,
            color = colors.textPrimary
        )

        if (isBugLine) {
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "\uD83D\uDC1B", style = typography.labelMedium)
        }
    }
}

/**
 * Вариант исправления
 */
@Composable
private fun BugOptionItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            colors.accentPrimary.copy(alpha = 0.2f)
        } else {
            colors.codeBackground
        }
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) {
            colors.accentPrimary
        } else {
            Color.White.copy(alpha = 0.15f)
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(Spacing.medium),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = typography.codeBlock,
            color = colors.textPrimary
        )
    }
}
