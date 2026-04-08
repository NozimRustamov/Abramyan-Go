package com.abramyango.ui.screens.categories

import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.abramyango.domain.model.Category
import com.abramyango.ui.components.GlassCard
import com.abramyango.ui.theme.AppTheme
import com.abramyango.ui.theme.Spacing

@Composable
fun CategoriesScreen(
    state: CategoriesState,
    onIntent: (CategoriesIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colors.backgroundGradientStart,
                        colors.backgroundGradientEnd
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.default),
                glassAlpha = 0.12f,
                cornerRadius = 16.dp,
                contentPadding = Spacing.medium
            ) {
                Text(
                    text = "Abramyan Go",
                    style = typography.headlineMedium,
                    color = colors.textPrimary
                )
            }

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colors.accentPrimary)
                }
            } else if (state.error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = state.error,
                        style = typography.bodyLarge,
                        color = colors.accentError
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Spacing.default),
                    contentPadding = PaddingValues(
                        top = Spacing.small,
                        bottom = Spacing.massive
                    ),
                    verticalArrangement = Arrangement.spacedBy(Spacing.small)
                ) {
                    itemsIndexed(
                        items = state.categories,
                        key = { _, category -> category.id }
                    ) { index, category ->
                        CategoryListItem(
                            index = index + 1,
                            category = category,
                            onClick = {
                                onIntent(
                                    CategoriesIntent.SelectCategory(
                                        category.id,
                                        category.name
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryListItem(
    index: Int,
    category: Category,
    onClick: () -> Unit
) {
    val colors = AppTheme.colors
    val typography = AppTheme.typography

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        glassAlpha = 0.1f,
        cornerRadius = 12.dp,
        contentPadding = Spacing.medium
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$index",
                style = typography.headlineMedium,
                color = colors.accentPrimary,
                modifier = Modifier.width(36.dp)
            )

            Spacer(modifier = Modifier.width(Spacing.small))

            Text(
                text = category.name,
                style = typography.titleMedium,
                color = colors.textPrimary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "->",
                style = typography.titleLarge,
                color = colors.textTertiary
            )
        }
    }
}
