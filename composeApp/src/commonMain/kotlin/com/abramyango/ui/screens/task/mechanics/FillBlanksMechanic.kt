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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.abramyango.domain.model.ProgrammingLanguage
import com.abramyango.domain.model.Task
import com.abramyango.ui.components.GlassCard
import com.abramyango.ui.components.GlassSurface
import com.abramyango.ui.components.PrimaryButton
import com.abramyango.ui.theme.AppTheme
import com.abramyango.ui.theme.Spacing

/**
 * Fill Blanks механика - заполни пропуски в коде
 */
@Composable
fun FillBlanksMechanic(
    task: Task,
    language: ProgrammingLanguage,
    onSubmit: (Map<Int, String>) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    // Парсим код с пропусками (___) 
    val code = task.code.getCode(language)
    val blanks = parseBlanks(code)
    
    // Ответы пользователя
    var userAnswers by remember { mutableStateOf<Map<Int, String>>(emptyMap()) }
    
    // Текущий активный пропуск
    var activeBlankIndex by remember { mutableStateOf(0) }
    
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Заполни пропуски:",
            style = typography.titleMedium,
            color = colors.textPrimary
        )
        
        Spacer(modifier = Modifier.height(Spacing.default))
        
        // Код с пропусками
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            glassAlpha = 0.1f,
            cornerRadius = 12.dp,
            contentPadding = Spacing.medium
        ) {
            CodeWithBlanks(
                code = code,
                blanks = blanks,
                userAnswers = userAnswers,
                activeBlankIndex = activeBlankIndex,
                onBlankClick = { index ->
                    activeBlankIndex = index
                }
            )
        }
        
        Spacer(modifier = Modifier.height(Spacing.large))
        
        // Варианты ответов для текущего пропуска
        if (activeBlankIndex < blanks.size) {
            Text(
                text = "Пропуск ${activeBlankIndex + 1}:",
                style = typography.labelMedium,
                color = colors.textSecondary
            )
            
            Spacer(modifier = Modifier.height(Spacing.small))
            
            // Варианты (сгенерированные или из данных задачи)
            val options = getOptionsForBlank(blanks[activeBlankIndex], language)
            
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.small)
            ) {
                options.forEach { option ->
                    val isSelected = userAnswers[activeBlankIndex] == option
                    
                    OptionItem(
                        text = option,
                        isSelected = isSelected,
                        onClick = {
                            userAnswers = userAnswers + (activeBlankIndex to option)
                            // Переходим к следующему пропуску
                            if (activeBlankIndex < blanks.size - 1) {
                                activeBlankIndex++
                            }
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
            enabled = userAnswers.size == blanks.size
        )
    }
}

/**
 * Код с интерактивными пропусками
 */
@Composable
private fun CodeWithBlanks(
    code: String,
    blanks: List<BlankInfo>,
    userAnswers: Map<Int, String>,
    activeBlankIndex: Int,
    onBlankClick: (Int) -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    // Разбиваем код на части
    val parts = mutableListOf<CodePart>()
    var lastEnd = 0
    
    blanks.forEachIndexed { index, blank ->
        // Текст до пропуска
        if (blank.start > lastEnd) {
            parts.add(CodePart.Text(code.substring(lastEnd, blank.start)))
        }
        // Пропуск
        parts.add(CodePart.Blank(index, userAnswers[index]))
        lastEnd = blank.end
    }
    // Текст после последнего пропуска
    if (lastEnd < code.length) {
        parts.add(CodePart.Text(code.substring(lastEnd)))
    }
    
    // Отображаем
    Column {
        val lines = buildCodeLines(parts)
        lines.forEach { lineParts ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                lineParts.forEach { part ->
                    when (part) {
                        is CodePart.Text -> {
                            Text(
                                text = part.text,
                                style = typography.codeBlock,
                                color = colors.textPrimary
                            )
                        }
                        is CodePart.Blank -> {
                            BlankSlot(
                                index = part.index,
                                answer = part.answer,
                                isActive = part.index == activeBlankIndex,
                                onClick = { onBlankClick(part.index) }
                            )
                        }
                    }
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
    index: Int,
    answer: String?,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isActive -> colors.accentPrimary.copy(alpha = 0.3f)
            answer != null -> colors.accentSuccess.copy(alpha = 0.2f)
            else -> colors.codeBackground
        }
    )
    
    val borderColor by animateColorAsState(
        targetValue = when {
            isActive -> colors.accentPrimary
            answer != null -> colors.accentSuccess.copy(alpha = 0.5f)
            else -> Color.White.copy(alpha = 0.3f)
        }
    )
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .clickable { onClick() }
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
private fun OptionItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography
    
    val backgroundColor = if (isSelected) {
        colors.accentPrimary.copy(alpha = 0.2f)
    } else {
        colors.codeBackground
    }
    
    val borderColor = if (isSelected) {
        colors.accentPrimary
    } else {
        Color.White.copy(alpha = 0.15f)
    }
    
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

// Вспомогательные классы и функции

private data class BlankInfo(
    val start: Int,
    val end: Int,
    val placeholder: String
)

private sealed class CodePart {
    data class Text(val text: String) : CodePart()
    data class Blank(val index: Int, val answer: String?) : CodePart()
}

private fun parseBlanks(code: String): List<BlankInfo> {
    val blanks = mutableListOf<BlankInfo>()
    val pattern = "___"
    var start = 0
    
    while (true) {
        val index = code.indexOf(pattern, start)
        if (index == -1) break
        
        blanks.add(BlankInfo(index, index + pattern.length, pattern))
        start = index + pattern.length
    }
    
    return blanks
}

private fun buildCodeLines(parts: List<CodePart>): List<List<CodePart>> {
    // Упрощённая версия - возвращаем все части в одной "строке"
    // В реальной версии нужно разбивать по \n
    return listOf(parts)
}

private fun getOptionsForBlank(blank: BlankInfo, language: ProgrammingLanguage): List<String> {
    // Генерируем варианты в зависимости от контекста
    // В реальной версии это должно браться из данных задачи
    return when (language) {
        ProgrammingLanguage.PYTHON -> listOf("4", "2", "3", "pi", "+", "*", "/")
        ProgrammingLanguage.JAVASCRIPT -> listOf("4", "2", "3", "Math.PI", "+", "*", "/")
        ProgrammingLanguage.KOTLIN -> listOf("4", "2", "3", "PI", "+", "*", "/")
        ProgrammingLanguage.CSHARP -> listOf("4", "2", "3", "Math.PI", "+", "*", "/")
    }.shuffled().take(4)
}
