package com.abramyango.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abramyango.ui.theme.AppTheme

/**
 * Glass Card - базовый компонент с эффектом стекла
 * Liquid Glass Design System
 *
 * Цвета берутся из текущей темы (AppTheme.colors.glassSurface / glassHighlight /
 * glassBorder), благодаря чему в светлой и тёмной теме карточки выглядят одинаково
 * читаемо. Параметры [glassAlpha] и [borderAlpha] сохранены для обратной
 * совместимости с местами вызова, но больше не влияют на рендер — интенсивность
 * заливки теперь определяется темой.
 */
@Suppress("UNUSED_PARAMETER")
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    glassAlpha: Float = 0.15f,
    borderAlpha: Float = 0.25f,
    cornerRadius: Dp = 20.dp,
    contentPadding: Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val colors = AppTheme.colors
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .shadow(elevation = 4.dp, shape = shape, clip = false)
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colors.glassHighlight,
                        colors.glassSurface
                    )
                )
            )
            .border(
                width = 1.dp,
                color = colors.glassBorder,
                shape = shape
            )
            .padding(contentPadding),
        content = content
    )
}

/**
 * Glass Card с акцентным цветом (для миров)
 */
@Composable
fun GlassCardAccent(
    accentColor: Color,
    modifier: Modifier = Modifier,
    glassAlpha: Float = 0.12f,
    cornerRadius: Dp = 20.dp,
    contentPadding: Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius)
    
    Box(
        modifier = modifier
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        accentColor.copy(alpha = glassAlpha + 0.08f),
                        accentColor.copy(alpha = glassAlpha)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.4f),
                        accentColor.copy(alpha = 0.2f)
                    )
                ),
                shape = shape
            )
            .padding(contentPadding),
        content = content
    )
}

/**
 * Glass Surface - более лёгкая версия для вложенных элементов:
 * меньшая тень и тонкая граница, та же тематическая заливка.
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    contentPadding: Dp = 12.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val colors = AppTheme.colors
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .shadow(elevation = 1.dp, shape = shape, clip = false)
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colors.glassHighlight,
                        colors.glassSurface
                    )
                )
            )
            .border(
                width = 1.dp,
                color = colors.glassBorder,
                shape = shape
            )
            .padding(contentPadding),
        content = content
    )
}
