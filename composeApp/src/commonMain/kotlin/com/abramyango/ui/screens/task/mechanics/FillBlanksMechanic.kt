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
 * FillBlank механика - заполни пропуск в коде.
 * Показывает code_template с ___, предлагает blank_options для выбора.
 * Правильный ответ в correct_option.
 */
@Composable
fun FillBlankMechanic(
    task: Task,
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    val data = task.taskData
    val codeTemplate = data.codeTemplate ?: ""
    val blankOptions = data.blankOptions ?: emptyList()

    var selectedOption by remember(task.id) { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Заполни пропуск:",
            style = typography.titleMedium,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(Spacing.default))

        // Код с пропуском
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glassAlpha = 0.1f,
            cornerRadius = 12.dp,
            contentPadding = Spacing.medium
        ) {
            CodeWithBlank(
                codeTemplate = codeTemplate,
                selectedOption = selectedOption
            )
        }

        Spacer(modifier = Modifier.height(Spacing.large))

        // Варианты
        Text(
            text = "Выбери значение для пропуска:",
            style = typography.labelMedium,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(Spacing.small))

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            blankOptions.forEach { option ->
                val isSelected = selectedOption == option

                BlankOptionItem(
                    text = option,
                    isSelected = isSelected,
                    onClick = { selectedOption = option }
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.extraLarge))

        // Кнопка проверки
        PrimaryButton(
            text = "Проверить",
            onClick = { selectedOption?.let { onSubmit(it) } },
            enabled = selectedOption != null
        )
    }
}

/**
 * Отображение кода с подсвеченным пропуском
 */
@Composable
private fun CodeWithBlank(
    codeTemplate: String,
    selectedOption: String?
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    val lines = codeTemplate.lines()

    Column {
        lines.forEachIndexed { index, line ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Номер строки
                Text(
                    text = (index + 1).toString().padStart(2, ' '),
                    style = typography.codeBlock,
                    color = colors.textTertiary,
                    modifier = Modifier.width(32.dp)
                )

                Spacer(modifier = Modifier.width(Spacing.small))

                // Если строка содержит ___, разбиваем на части
                if (line.contains("___")) {
                    val parts = line.split("___")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        parts.forEachIndexed { partIndex, part ->
                            Text(
                                text = part,
                                style = typography.codeBlock,
                                color = colors.textPrimary
                            )
                            // Вставляем слот между частями (кроме последней)
                            if (partIndex < parts.size - 1) {
                                BlankSlot(answer = selectedOption)
                            }
                        }
                    }
                } else {
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

/**
 * Слот для пропуска
 */
@Composable
private fun BlankSlot(
    answer: String?
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    val backgroundColor by animateColorAsState(
        targetValue = if (answer != null) {
            colors.accentSuccess.copy(alpha = 0.2f)
        } else {
            colors.codeBackground
        }
    )

    val borderColor by animateColorAsState(
        targetValue = if (answer != null) {
            colors.accentSuccess.copy(alpha = 0.5f)
        } else {
            Color.White.copy(alpha = 0.3f)
        }
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = answer ?: "___",
            style = typography.codeBlock,
            color = if (answer != null) colors.accentSuccess else colors.textTertiary
        )
    }
}

/**
 * Вариант ответа
 */
@Composable
private fun BlankOptionItem(
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
