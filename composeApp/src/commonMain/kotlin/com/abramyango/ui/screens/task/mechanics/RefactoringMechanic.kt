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
 * Refactoring механика - выбери лучший вариант кода
 */
@Composable
fun RefactoringMechanic(
    task: Task,
    language: ProgrammingLanguage,
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    // Варианты рефакторинга
    val options = remember(task) {
        generateRefactoringOptions(task, language)
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
                    text = "✨",
                    style = typography.headlineMedium
                )
                Spacer(modifier = Modifier.width(Spacing.small))
                Column {
                    Text(
                        text = "Рефакторинг",
                        style = typography.titleMedium,
                        color = colors.textPrimary
                    )
                    Text(
                        text = "Какой вариант кода лучше?",
                        style = typography.bodyMedium,
                        color = colors.textSecondary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(Spacing.default))
        
        // Критерии оценки
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glassAlpha = 0.08f,
            cornerRadius = 8.dp,
            contentPadding = Spacing.small
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.medium)
            ) {
                CriteriaChip(text = "📖 Читаемость")
                CriteriaChip(text = "⚡ Эффективность")
                CriteriaChip(text = "🎯 Краткость")
            }
        }
        
        Spacer(modifier = Modifier.height(Spacing.large))
        
        // Варианты кода
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.medium)
        ) {
            options.forEachIndexed { index, option ->
                val letter = ('A' + index).toString()
                val isSelected = selectedOption == letter
                
                RefactoringOptionCard(
                    letter = letter,
                    code = option,
                    isSelected = isSelected,
                    onClick = { selectedOption = letter }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(Spacing.extraLarge))
        
        // Кнопка проверки
        PrimaryButton(
            text = "Выбрать лучший",
            onClick = { selectedOption?.let { onSubmit(it) } },
            enabled = selectedOption != null
        )
    }
}

/**
 * Чип критерия
 */
@Composable
private fun CriteriaChip(text: String) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    Text(
        text = text,
        style = typography.labelSmall,
        color = colors.textSecondary
    )
}

/**
 * Карточка варианта рефакторинга
 */
@Composable
private fun RefactoringOptionCard(
    letter: String,
    code: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            colors.accentPrimary.copy(alpha = 0.15f)
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
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        // Заголовок
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.05f))
                .padding(horizontal = Spacing.medium, vertical = Spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        if (isSelected) colors.accentPrimary
                        else Color.White.copy(alpha = 0.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter,
                    style = typography.labelMedium,
                    color = if (isSelected) colors.textOnAccent else colors.textPrimary
                )
            }
            
            Spacer(modifier = Modifier.width(Spacing.small))
            
            Text(
                text = "Вариант $letter",
                style = typography.labelMedium,
                color = colors.textSecondary
            )
            
            if (isSelected) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "✓",
                    style = typography.titleMedium,
                    color = colors.accentPrimary
                )
            }
        }
        
        // Код
        Text(
            text = code,
            style = typography.codeBlock,
            color = colors.textPrimary,
            modifier = Modifier.padding(Spacing.medium)
        )
    }
}

/**
 * Генерация вариантов рефакторинга
 */
private fun generateRefactoringOptions(task: Task, language: ProgrammingLanguage): List<String> {
    // В реальной версии это должно браться из данных задачи
    return when (language) {
        ProgrammingLanguage.PYTHON -> listOf(
            """
# Вариант A
x = a
a = b  
b = x
            """.trimIndent(),
            """
# Вариант B
a, b = b, a
            """.trimIndent(),
            """
# Вариант C
a = a + b
b = a - b
a = a - b
            """.trimIndent()
        )
        ProgrammingLanguage.KOTLIN -> listOf(
            """
// Вариант A
val temp = a
a = b
b = temp
            """.trimIndent(),
            """
// Вариант B
a = b.also { b = a }
            """.trimIndent(),
            """
// Вариант C
a = a xor b
b = a xor b
a = a xor b
            """.trimIndent()
        )
        ProgrammingLanguage.JAVASCRIPT -> listOf(
            """
// Вариант A
let temp = a;
a = b;
b = temp;
            """.trimIndent(),
            """
// Вариант B
[a, b] = [b, a];
            """.trimIndent(),
            """
// Вариант C
a = a + b;
b = a - b;
a = a - b;
            """.trimIndent()
        )
        ProgrammingLanguage.CSHARP -> listOf(
            """
// Вариант A
int temp = a;
a = b;
b = temp;
            """.trimIndent(),
            """
// Вариант B
(a, b) = (b, a);
            """.trimIndent(),
            """
// Вариант C
a ^= b;
b ^= a;
a ^= b;
            """.trimIndent()
        )
    }
}
