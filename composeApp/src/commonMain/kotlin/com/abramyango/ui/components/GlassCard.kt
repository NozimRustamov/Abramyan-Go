package com.abramyango.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abramyango.ui.theme.AppTheme

/**
 * Glass Card - базовый компонент с эффектом стекла
 * Liquid Glass Design System
 */
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
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = glassAlpha + 0.05f),
                        Color.White.copy(alpha = glassAlpha)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = borderAlpha + 0.1f),
                        Color.White.copy(alpha = borderAlpha)
                    )
                ),
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
 * Glass Surface - более прозрачная версия для вложенных элементов
 */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    contentPadding: Dp = 12.dp,
    content: @Composable BoxScope.() -> Unit
) {
    GlassCard(
        modifier = modifier,
        glassAlpha = 0.08f,
        borderAlpha = 0.15f,
        cornerRadius = cornerRadius,
        contentPadding = contentPadding,
        content = content
    )
}
