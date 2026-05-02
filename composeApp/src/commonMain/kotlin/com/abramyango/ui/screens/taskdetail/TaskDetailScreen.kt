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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abramyango.ui.theme.AppTheme
import com.abramyango.ui.theme.CatColors
import com.abramyango.ui.theme.categoryStyleFor
import com.abramyango.ui.theme.languageColor
import com.abramyango.ui.theme.languageFilename
import com.abramyango.ui.theme.languageLabel

@Composable
fun TaskDetailScreen(
    state: TaskDetailState,
    onIntent: (TaskDetailIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val catStyle = categoryStyleFor(state.categoryId)
    val categoryKey = state.categoryId.substringBefore('_')

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.backgroundPrimary)
    ) {
        // Topbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 14.dp, end = 14.dp, top = 14.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(onClick = { onIntent(TaskDetailIntent.Back) })

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = state.task?.id ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.padding(top = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "~/",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = colors.textTertiary
                    )
                    Text(
                        text = "$categoryKey/",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = catStyle.accentColor
                    )
                    if (state.task != null) {
                        Text(
                            text = state.task.id,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = colors.textTertiary
                        )
                    }
                }
            }
        }
        HorizontalDivider(color = colors.surface0, thickness = 1.dp)

        when {
            state.isLoading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colors.accentPrimary)
            }

            state.task == null -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.error ?: "Задача не найдена",
                    fontSize = 14.sp,
                    color = colors.accentError
                )
            }

            else -> {
                val task = state.task
                val solutionEntries = task.solutions.entries
                    .sortedWith(compareBy { languageSortOrder(it.key) })

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 12.dp, end = 12.dp, top = 14.dp, bottom = 20.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Task description block
                    item {
                        TaskDescriptionBlock(question = task.question)
                    }

                    // Solutions header
                    item {
                        Row(
                            modifier = Modifier.padding(bottom = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "//",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = colors.accentPrimary
                            )
                            Text(
                                text = "Решения",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = colors.textPrimary
                            )
                            Text(
                                text = "— выберите язык",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                color = colors.textTertiary
                            )
                        }
                    }

                    // Solution blocks
                    items(
                        items = solutionEntries.toList(),
                        key = { it.key }
                    ) { (language, code) ->
                        SolutionBlock(
                            language = language,
                            code = code,
                            isExpanded = language in state.expandedSolutions,
                            onToggle = { onIntent(TaskDetailIntent.ToggleSolution(language)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskDescriptionBlock(question: String) {
    val colors = AppTheme.colors
    val shape = RoundedCornerShape(12.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.glassSurface)
            .border(width = 1.dp, color = colors.glassBorder, shape = shape)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "/*",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = colors.accentPrimary
            )
            Text(
                text = "Условие задачи",
                fontSize = 11.sp,
                color = colors.textTertiary
            )
            Text(
                text = "*/",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = colors.accentPrimary
            )
        }
        Text(
            text = question,
            fontSize = 13.sp,
            color = colors.textPrimary,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun SolutionBlock(
    language: String,
    code: String,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val colors = AppTheme.colors
    val langColor = languageColor(language)
    val borderColor = if (isExpanded) langColor.copy(alpha = 0.27f) else colors.glassBorder
    val shape = RoundedCornerShape(12.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.glassSurface)
            .border(width = 1.dp, color = borderColor, shape = shape)
    ) {
        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(vertical = 11.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Language badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(langColor.copy(alpha = 0.13f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = languageLabel(language),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = langColor
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Решение",
                fontSize = 13.sp,
                color = colors.textSecondary,
                modifier = Modifier.weight(1f)
            )

            // Toggle button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isExpanded) langColor.copy(alpha = 0.2f) else colors.surface0
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (isExpanded) "скрыть ∧" else "смотреть ∨",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = if (isExpanded) langColor else colors.textTertiary
                )
            }
        }

        // Expanded code block
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider(color = colors.surface0, thickness = 1.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.crust)
                        .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 14.dp)
                ) {
                // macOS traffic light dots + filename
                Row(
                    modifier = Modifier.padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(CatColors.pink.copy(alpha = 0.7f)))
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(CatColors.yellow.copy(alpha = 0.7f)))
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(CatColors.green.copy(alpha = 0.7f)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = languageFilename(language),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = colors.textTertiary
                    )
                }

                // Code
                Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    Text(
                        text = code,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        lineHeight = 20.sp,
                        color = colors.textPrimary,
                        softWrap = false
                    )
                }
                }
            }
        }
    }
}

@Composable
private fun BackButton(onClick: () -> Unit) {
    val colors = AppTheme.colors
    val shape = RoundedCornerShape(10.dp)

    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(shape)
            .background(colors.surface0)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "‹",
            fontFamily = FontFamily.Monospace,
            fontSize = 20.sp,
            color = colors.textSecondary
        )
    }
}

private fun languageSortOrder(language: String): Int =
    when (language.lowercase()) {
        "java"       -> 0
        "csharp"     -> 1
        "javascript" -> 2
        "python"     -> 3
        else         -> 4
    }
