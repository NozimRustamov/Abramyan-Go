package com.abramyango.ui.screens.categorytasklist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abramyango.data.CategoryTask
import com.abramyango.ui.theme.AppTheme
import com.abramyango.ui.theme.categoryStyleFor

@Composable
fun CategoryTaskListScreen(
    state: CategoryTaskListState,
    onIntent: (CategoryTaskListIntent) -> Unit,
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
            BackButton(onClick = { onIntent(CategoryTaskListIntent.Back) })

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = catStyle.icon,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = catStyle.accentColor
                    )
                    Text(
                        text = state.categoryName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = colors.textPrimary
                    )
                }
                Row(
                    modifier = Modifier.padding(top = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "~/categories/",
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
                }
            }

            // Task count badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.surface0)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${state.tasks.size} задач",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = colors.textTertiary
                )
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

            state.error != null -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = state.error, fontSize = 14.sp, color = colors.accentError)
            }

            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 12.dp, end = 12.dp, top = 10.dp, bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                itemsIndexed(
                    items = state.tasks,
                    key = { _, task -> task.id }
                ) { index, task ->
                    TaskRow(
                        index = index,
                        task = task,
                        categoryAccent = catStyle.accentColor,
                        onClick = { onIntent(CategoryTaskListIntent.SelectTask(index)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskRow(
    index: Int,
    task: CategoryTask,
    categoryAccent: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors
    val shape = RoundedCornerShape(12.dp)
    val taskNum = "${index + 1}".padStart(2, '0')

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.glassSurface)
            .border(width = 1.dp, color = colors.glassBorder, shape = shape)
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = taskNum,
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            color = colors.textTertiary,
            modifier = Modifier.padding(top = 2.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.id,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = colors.textPrimary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = task.question,
                fontSize = 12.sp,
                color = colors.textSecondary,
                lineHeight = 18.sp,
                maxLines = 2
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "›",
            fontFamily = FontFamily.Monospace,
            fontSize = 18.sp,
            color = colors.surface1,
            modifier = Modifier.padding(top = 3.dp)
        )
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
