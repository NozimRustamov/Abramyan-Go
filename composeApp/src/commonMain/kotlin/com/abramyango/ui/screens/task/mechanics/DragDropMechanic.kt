package com.abramyango.ui.screens.task.mechanics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.abramyango.domain.model.DragDropBlock
import com.abramyango.domain.model.Task
import com.abramyango.ui.components.GlassCard
import com.abramyango.ui.components.PrimaryButton
import com.abramyango.ui.theme.AppTheme
import com.abramyango.ui.theme.Spacing

/**
 * Drag & Drop механика - собери код из блоков в правильном порядке.
 * Данные берутся из task.taskData.blocks и task.taskData.correctOrder.
 */
@Composable
fun DragDropMechanic(
    task: Task,
    onSubmit: (List<Int>) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    val blocks = task.taskData.blocks ?: emptyList()

    // Блоки в перемешанном порядке для показа
    val shuffledBlocks by remember(task.id) {
        mutableStateOf(blocks.shuffled())
    }

    // Текущий порядок, выбранный пользователем (список блоков)
    var userOrder by remember(task.id) { mutableStateOf<List<DragDropBlock>>(emptyList()) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Расставь блоки в правильном порядке:",
            style = typography.titleMedium,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(Spacing.default))

        // Доступные блоки
        Text(
            text = "Доступные блоки:",
            style = typography.labelMedium,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(Spacing.small))

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            val selectedIds = userOrder.map { it.id }.toSet()
            shuffledBlocks.filter { it.id !in selectedIds }.forEach { block ->
                CodeBlockItem(
                    code = block.code,
                    isSelected = false,
                    onClick = {
                        userOrder = userOrder + block
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.large))

        // Выбранный порядок
        Text(
            text = "Твой код:",
            style = typography.labelMedium,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(Spacing.small))

        if (userOrder.isEmpty()) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                glassAlpha = 0.08f,
                cornerRadius = 12.dp,
                contentPadding = Spacing.default
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Нажми на блоки выше, чтобы добавить их сюда",
                        style = typography.bodyMedium,
                        color = colors.textTertiary
                    )
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.small)
            ) {
                userOrder.forEachIndexed { index, block ->
                    CodeBlockItem(
                        code = block.code,
                        isSelected = true,
                        lineNumber = index + 1,
                        onClick = {
                            userOrder = userOrder.filter { it.id != block.id }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Spacing.extraLarge))

        // Кнопка проверки
        PrimaryButton(
            text = "Проверить",
            onClick = { onSubmit(userOrder.map { it.id }) },
            enabled = userOrder.size == blocks.size
        )

        // Кнопка сброса
        if (userOrder.isNotEmpty()) {
            Spacer(modifier = Modifier.height(Spacing.small))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Сбросить",
                    style = typography.labelMedium,
                    color = colors.textTertiary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { userOrder = emptyList() }
                        .padding(Spacing.small)
                )
            }
        }
    }
}

/**
 * Отдельный блок кода
 */
@Composable
private fun CodeBlockItem(
    code: String,
    isSelected: Boolean,
    lineNumber: Int? = null,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    val backgroundColor = if (isSelected) {
        colors.accentPrimary.copy(alpha = 0.15f)
    } else {
        colors.codeBackground
    }

    val borderColor = if (isSelected) {
        colors.accentPrimary.copy(alpha = 0.4f)
    } else {
        Color.White.copy(alpha = 0.1f)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(Spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (lineNumber != null) {
            Text(
                text = "$lineNumber",
                style = typography.labelMedium,
                color = colors.textTertiary,
                modifier = Modifier.width(24.dp)
            )
        }

        Text(
            text = code.trim(),
            style = typography.codeBlock,
            color = colors.textPrimary,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = if (isSelected) "\u2715" else "+",
            style = typography.labelMedium,
            color = if (isSelected) colors.accentError else colors.accentSuccess
        )
    }
}
