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
import com.abramyango.domain.model.ProgrammingLanguage
import com.abramyango.domain.model.Task
import com.abramyango.ui.components.GlassCard
import com.abramyango.ui.components.PrimaryButton
import com.abramyango.ui.theme.AppTheme
import com.abramyango.ui.theme.Spacing

/**
 * Bug Hunt механика - найди строку с ошибкой
 */
@Composable
fun BugHuntMechanic(
    task: Task,
    language: ProgrammingLanguage,
    onSubmit: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    val code = task.code.getCode(language)
    val lines = code.lines()
    
    // Выбранная пользователем строка
    var selectedLine by remember { mutableStateOf<Int?>(null) }
    
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
                    text = "🐛",
                    style = typography.headlineMedium
                )
                Spacer(modifier = Modifier.width(Spacing.small))
                Column {
                    Text(
                        text = "Найди ошибку!",
                        style = typography.titleMedium,
                        color = colors.textPrimary
                    )
                    Text(
                        text = "Нажми на строку с багом",
                        style = typography.bodyMedium,
                        color = colors.textSecondary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(Spacing.default))
        
        // Код с номерами строк
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glassAlpha = 0.08f,
            cornerRadius = 12.dp,
            contentPadding = 0.dp
        ) {
            Column {
                lines.forEachIndexed { index, line ->
                    val lineNumber = index + 1
                    val isSelected = selectedLine == lineNumber
                    
                    CodeLineItem(
                        lineNumber = lineNumber,
                        code = line,
                        isSelected = isSelected,
                        onClick = {
                            selectedLine = lineNumber
                        }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(Spacing.large))
        
        // Подсказка
        if (selectedLine != null) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                glassAlpha = 0.1f,
                cornerRadius = 8.dp,
                contentPadding = Spacing.small
            ) {
                Text(
                    text = "Выбрана строка $selectedLine",
                    style = typography.labelMedium,
                    color = colors.accentWarning
                )
            }
            
            Spacer(modifier = Modifier.height(Spacing.default))
        }
        
        // Кнопка проверки
        PrimaryButton(
            text = "Это баг! 🎯",
            onClick = { selectedLine?.let { onSubmit(it) } },
            enabled = selectedLine != null
        )
    }
}

/**
 * Строка кода с возможностью выбора
 */
@Composable
private fun CodeLineItem(
    lineNumber: Int,
    code: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            colors.accentError.copy(alpha = 0.2f)
        } else {
            Color.Transparent
        }
    )
    
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) {
            colors.accentError.copy(alpha = 0.5f)
        } else {
            Color.Transparent
        }
    )
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = Spacing.medium, vertical = Spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Номер строки
        Text(
            text = lineNumber.toString().padStart(2, ' '),
            style = typography.codeBlock,
            color = if (isSelected) colors.accentError else colors.textTertiary,
            modifier = Modifier.width(32.dp)
        )
        
        // Разделитель
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(20.dp)
                .background(Color.White.copy(alpha = 0.1f))
        )
        
        Spacer(modifier = Modifier.width(Spacing.medium))
        
        // Код
        Text(
            text = code,
            style = typography.codeBlock,
            color = colors.textPrimary
        )
        
        // Индикатор выбора
        if (isSelected) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "🐛",
                style = typography.labelMedium
            )
        }
    }
}
