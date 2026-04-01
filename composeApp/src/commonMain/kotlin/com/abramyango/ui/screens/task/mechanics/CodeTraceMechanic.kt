package com.abramyango.ui.screens.task.mechanics

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.abramyango.domain.model.ProgrammingLanguage
import com.abramyango.domain.model.Task
import com.abramyango.ui.components.CodeBlock
import com.abramyango.ui.components.GlassCard
import com.abramyango.ui.components.PrimaryButton
import com.abramyango.ui.theme.AppTheme
import com.abramyango.ui.theme.Spacing

/**
 * Code Trace механика - трассировка выполнения кода
 * Пользователь вводит значения переменных на каждом шаге
 */
@Composable
fun CodeTraceMechanic(
    task: Task,
    language: ProgrammingLanguage,
    onSubmit: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    val code = task.code.getCode(language)
    
    // Шаги трассировки (в реальной версии из данных задачи)
    val traceSteps = remember(task) {
        generateTraceSteps(task)
    }
    
    // Ответы пользователя
    var userAnswers by remember {
        mutableStateOf(List(traceSteps.size) { "" })
    }
    
    // Текущий активный шаг
    var currentStep by remember { mutableStateOf(0) }
    
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
                    text = "🔍",
                    style = typography.headlineMedium
                )
                Spacer(modifier = Modifier.width(Spacing.small))
                Column {
                    Text(
                        text = "Трассировка кода",
                        style = typography.titleMedium,
                        color = colors.textPrimary
                    )
                    Text(
                        text = "Введи значения переменных после каждой строки",
                        style = typography.bodyMedium,
                        color = colors.textSecondary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(Spacing.default))
        
        // Код с подсвеченной текущей строкой
        CodeBlock(
            code = code,
            language = language,
            highlightedLine = traceSteps.getOrNull(currentStep)?.lineNumber,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(Spacing.large))
        
        // Таблица трассировки
        Text(
            text = "Таблица трассировки:",
            style = typography.labelMedium,
            color = colors.textSecondary
        )
        
        Spacer(modifier = Modifier.height(Spacing.small))
        
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glassAlpha = 0.08f,
            cornerRadius = 12.dp,
            contentPadding = Spacing.medium
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.small)
            ) {
                // Заголовок
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Строка",
                        style = typography.labelSmall,
                        color = colors.textTertiary,
                        modifier = Modifier.width(60.dp)
                    )
                    Text(
                        text = "Переменная",
                        style = typography.labelSmall,
                        color = colors.textTertiary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Значение",
                        style = typography.labelSmall,
                        color = colors.textTertiary,
                        modifier = Modifier.width(80.dp),
                        textAlign = TextAlign.Center
                    )
                }
                
                // Шаги
                traceSteps.forEachIndexed { index, step ->
                    val isActive = index == currentStep
                    val isCompleted = userAnswers[index].isNotBlank()
                    
                    TraceStepRow(
                        step = step,
                        answer = userAnswers[index],
                        isActive = isActive,
                        isCompleted = isCompleted,
                        onAnswerChange = { newAnswer ->
                            userAnswers = userAnswers.toMutableList().apply {
                                this[index] = newAnswer
                            }
                        },
                        onFocus = {
                            currentStep = index
                        }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(Spacing.extraLarge))
        
        // Кнопка проверки
        PrimaryButton(
            text = "Проверить",
            onClick = { onSubmit(userAnswers) },
            enabled = userAnswers.all { it.isNotBlank() }
        )
    }
}

/**
 * Строка шага трассировки
 */
@Composable
private fun TraceStepRow(
    step: TraceStep,
    answer: String,
    isActive: Boolean,
    isCompleted: Boolean,
    onAnswerChange: (String) -> Unit,
    onFocus: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    val rowColor by animateColorAsState(
        targetValue = when {
            isActive -> colors.accentPrimary.copy(alpha = 0.15f)
            isCompleted -> colors.accentSuccess.copy(alpha = 0.1f)
            else -> Color.Transparent
        }
    )
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(rowColor)
            .padding(Spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Номер строки
        Text(
            text = step.lineNumber.toString(),
            style = typography.labelMedium,
            color = if (isActive) colors.accentPrimary else colors.textSecondary,
            modifier = Modifier.width(60.dp)
        )
        
        // Переменная
        Text(
            text = step.variable,
            style = typography.codeBlock,
            color = colors.codeVariable,
            modifier = Modifier.weight(1f)
        )
        
        // Поле ввода значения
        Box(
            modifier = Modifier
                .width(80.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(colors.codeBackground)
                .border(
                    width = 1.dp,
                    color = if (isActive) colors.accentPrimary else Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            BasicTextField(
                value = answer,
                onValueChange = onAnswerChange,
                textStyle = typography.codeBlock.copy(
                    color = colors.textPrimary,
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                cursorBrush = SolidColor(colors.accentPrimary),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )
            
            if (answer.isEmpty()) {
                Text(
                    text = "?",
                    style = typography.codeBlock,
                    color = colors.textTertiary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// Вспомогательные классы

data class TraceStep(
    val lineNumber: Int,
    val variable: String,
    val expectedValue: String
)

/**
 * Генерация шагов трассировки
 */
private fun generateTraceSteps(task: Task): List<TraceStep> {
    // В реальной версии это должно браться из данных задачи
    return when {
        task.id.contains("begin_022") -> listOf(
            TraceStep(1, "A", "5"),
            TraceStep(2, "B", "3"),
            TraceStep(3, "temp", "5"),
            TraceStep(4, "A", "3"),
            TraceStep(5, "B", "5")
        )
        task.id.contains("begin_027") -> listOf(
            TraceStep(1, "A", "2"),
            TraceStep(2, "A2", "4"),
            TraceStep(3, "A4", "16"),
            TraceStep(4, "A8", "256")
        )
        task.id.contains("bool_024") -> listOf(
            TraceStep(1, "A", "1"),
            TraceStep(2, "B", "4"),
            TraceStep(3, "C", "3"),
            TraceStep(4, "D", "4"),
            TraceStep(5, "has_roots", "True")
        )
        else -> listOf(
            TraceStep(1, "x", "0"),
            TraceStep(2, "y", "0"),
            TraceStep(3, "result", "0")
        )
    }
}
