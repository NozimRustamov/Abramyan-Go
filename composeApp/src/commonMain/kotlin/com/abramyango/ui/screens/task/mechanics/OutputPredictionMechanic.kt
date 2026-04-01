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
 * Output Prediction механика - что выведет код?
 */
@Composable
fun OutputPredictionMechanic(
    task: Task,
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    // Генерируем варианты ответов (правильный + неправильные)
    val options = remember(task) {
        generateOutputOptions(task)
    }
    
    var selectedOption by remember { mutableStateOf<String?>(null) }
    
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🖥️",
                    style = typography.headlineMedium
                )
                Spacer(modifier = Modifier.width(Spacing.small))
                Column {
                    Text(
                        text = "Что выведет программа?",
                        style = typography.titleMedium,
                        color = colors.textPrimary
                    )
                    Text(
                        text = "Выбери правильный вывод",
                        style = typography.bodyMedium,
                        color = colors.textSecondary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(Spacing.large))
        
        // Варианты ответов
        Text(
            text = "Варианты вывода:",
            style = typography.labelMedium,
            color = colors.textSecondary
        )
        
        Spacer(modifier = Modifier.height(Spacing.small))
        
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            options.forEachIndexed { index, option ->
                val letter = ('A' + index).toString()
                val isSelected = selectedOption == option
                
                OutputOptionItem(
                    letter = letter,
                    output = option,
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
 * Вариант вывода
 */
@Composable
private fun OutputOptionItem(
    letter: String,
    output: String,
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
            Color.White.copy(alpha = 0.1f)
        }
    )
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(Spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Буква варианта
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (isSelected) colors.accentPrimary
                    else Color.White.copy(alpha = 0.1f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = letter,
                style = typography.labelLarge,
                color = if (isSelected) colors.textOnAccent else colors.textPrimary
            )
        }
        
        Spacer(modifier = Modifier.width(Spacing.medium))
        
        // Вывод (как в консоли)
        Column {
            Text(
                text = ">>>",
                style = typography.labelSmall,
                color = colors.textTertiary
            )
            Text(
                text = output,
                style = typography.codeBlock,
                color = colors.textPrimary
            )
        }
    }
}

/**
 * Генерация вариантов вывода
 */
private fun generateOutputOptions(task: Task): List<String> {
    // В реальной версии это должно браться из данных задачи
    // Пока генерируем на основе ID задачи
    return when {
        task.id.contains("begin_003") -> listOf("15 16", "15 8", "8 15", "15")
        task.id.contains("int_006") -> listOf("5 7", "7 5", "57", "12")
        task.id.contains("bool_001") -> listOf("False", "True", "0", "-5")
        task.id.contains("bool_006") -> listOf("True", "False", "1", "0")
        task.id.contains("bool_033") -> listOf("False", "True", "7", "Error")
        else -> listOf("15 16", "16 15", "15", "Error").shuffled()
    }
}
