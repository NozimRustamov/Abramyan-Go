package com.abramyango.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Типографика приложения
 */
@Immutable
data class AppTypography(
    val headlineMedium: TextStyle,    // Подзаголовки
    val titleLarge: TextStyle,        // Названия задач
    val titleMedium: TextStyle,       // Названия секций
    val bodyLarge: TextStyle,         // Основной текст
    val labelLarge: TextStyle,        // Кнопки
    val labelMedium: TextStyle,       // Бейджи
    val codeBlock: TextStyle          // Monospace для кода
)

/**
 * Базовая типографика
 */
val DefaultTypography = AppTypography(
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    codeBlock = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    )
)

/**
 * Формы компонентов
 */
@Immutable
data class AppShapes(
    val extraSmall: Shape,
    val small: Shape,
    val medium: Shape,
    val large: Shape,
    val extraLarge: Shape,
    val glassCard: Shape,
    val button: Shape,
    val codeBlock: Shape,
    val badge: Shape,
    val pill: Shape
)

val DefaultShapes = AppShapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp),
    glassCard = RoundedCornerShape(20.dp),
    button = RoundedCornerShape(16.dp),
    codeBlock = RoundedCornerShape(12.dp),
    badge = RoundedCornerShape(8.dp),
    pill = RoundedCornerShape(50)
)

/**
 * Отступы и размеры
 */
object Spacing {
    val extraSmall = 4.dp
    val small = 8.dp
    val medium = 12.dp
    val default = 16.dp
    val massive = 48.dp
}


val LocalAppTypography = staticCompositionLocalOf { DefaultTypography }
val LocalAppShapes = staticCompositionLocalOf { DefaultShapes }
