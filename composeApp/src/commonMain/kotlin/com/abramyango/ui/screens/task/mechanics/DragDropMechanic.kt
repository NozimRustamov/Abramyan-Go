package com.abramyango.ui.screens.task.mechanics

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.abramyango.domain.model.ProgrammingLanguage
import com.abramyango.domain.model.Task
import com.abramyango.ui.components.GlassCard
import com.abramyango.ui.components.PrimaryButton
import com.abramyango.ui.theme.AppTheme
import com.abramyango.ui.theme.Spacing

/**
 * Drag & Drop механика - собери код из блоков
 */
@Composable
fun DragDropMechanic(
    task: Task,
    language: ProgrammingLanguage,
    onSubmit: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    // Получаем блоки кода (строки)
    val code = task.code.getCode(language)
    val correctLines = code.lines().filter { it.isNotBlank() }
    
    // Перемешиваем для задания + добавляем обманки
    var shuffledBlocks by remember {
        mutableStateOf(correctLines.shuffled())
    }
    
    // Текущий порядок, выбранный пользователем
    var userOrder by remember { mutableStateOf<List<String>>(emptyList()) }
    
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
        
        // Блоки для выбора
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            shuffledBlocks.filter { it !in userOrder }.forEach { block ->
                CodeBlockItem(
                    code = block,
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
                        code = block,
                        isSelected = true,
                        lineNumber = index + 1,
                        onClick = {
                            // Удалить блок при нажатии
                            userOrder = userOrder - block
                        }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(Spacing.extraLarge))
        
        // Кнопка проверки
        PrimaryButton(
            text = "Проверить",
            onClick = { onSubmit(userOrder) },
            enabled = userOrder.size == correctLines.size
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
                        .padding(Spacing.small)
                        .let { mod ->
                            mod
                        }
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
            .let { mod ->
                mod.pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { },
                        onDrag = { _, _ -> },
                        onDragEnd = { },
                        onDragCancel = { }
                    )
                }
            }
            .padding(Spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Номер строки для выбранных
        if (lineNumber != null) {
            Text(
                text = "$lineNumber",
                style = typography.labelMedium,
                color = colors.textTertiary,
                modifier = Modifier.width(24.dp)
            )
        }
        
        // Код
        Text(
            text = code.trim(),
            style = typography.codeBlock,
            color = colors.textPrimary,
            modifier = Modifier.weight(1f)
        )
        
        // Иконка действия
        Text(
            text = if (isSelected) "✕" else "+",
            style = typography.labelMedium,
            color = if (isSelected) colors.accentError else colors.accentSuccess
        )
    }
}
