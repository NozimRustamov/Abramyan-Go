package com.abramyango.ui.screens.categories

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abramyango.data.Category
import com.abramyango.ui.theme.AppTheme
import com.abramyango.ui.theme.categoryStyleFor

@Composable
fun CategoriesScreen(
    state: CategoriesState,
    onIntent: (CategoriesIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.backgroundPrimary)
    ) {
        // Topbar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = ">",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = colors.accentPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Абрамян",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = colors.textPrimary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "1000 задач",
                    fontSize = 12.sp,
                    color = colors.textTertiary
                )
            }
            Row(
                modifier = Modifier.padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "~/",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = colors.textTertiary
                )
                Text(
                    text = "categories",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = colors.accentPrimary
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
                    items = state.categories,
                    key = { _, cat -> cat.id }
                ) { index, category ->
                    CategoryRow(
                        lineNumber = index + 1,
                        category = category,
                        onClick = {
                            onIntent(CategoriesIntent.SelectCategory(category.id, category.name))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryRow(
    lineNumber: Int,
    category: Category,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors
    val style = categoryStyleFor(category.id)
    val shape = RoundedCornerShape(12.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.glassSurface)
            .border(width = 1.dp, color = colors.glassBorder, shape = shape)
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Line number
        Text(
            text = lineNumber.toString().padStart(2, '0'),
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            color = colors.textTertiary,
            textAlign = TextAlign.End,
            modifier = Modifier.width(16.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Category icon
        Text(
            text = style.icon,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = style.accentColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Label + description
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = category.name,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = colors.textPrimary
            )
            Text(
                text = categoryDescription(category.id),
                fontSize = 11.sp,
                color = colors.textTertiary
            )
        }

        // Chevron
        Text(
            text = "›",
            fontFamily = FontFamily.Monospace,
            fontSize = 18.sp,
            color = colors.surface1
        )
    }
}

private fun categoryDescription(categoryId: String): String =
    when (categoryId.substringBefore('_').lowercase()) {
        "begin"   -> "Первые программы"
        "integer" -> "Целые числа"
        "boolean" -> "Логические значения"
        "if"      -> "Условные операторы"
        "case"    -> "Оператор выбора"
        "for"     -> "Цикл с параметром"
        "while"   -> "Цикл с условием"
        "series"  -> "Числовые ряды"
        "proc"    -> "Процедуры и функции"
        else      -> ""
    }
