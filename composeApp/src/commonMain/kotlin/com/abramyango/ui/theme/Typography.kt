package com.abramyango.ui.theme

import androidx.compose.foundation.shape.CircleShape
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
    val displayLarge: TextStyle,      // Заголовки миров
    val displayMedium: TextStyle,     // Большие числа (XP, комбо)
    val headlineLarge: TextStyle,     // Заголовки экранов
    val headlineMedium: TextStyle,    // Подзаголовки
    val titleLarge: TextStyle,        // Названия задач
    val titleMedium: TextStyle,       // Названия секций
    val bodyLarge: TextStyle,         // Основной текст
    val bodyMedium: TextStyle,        // Вторичный текст
    val labelLarge: TextStyle,        // Кнопки
    val labelMedium: TextStyle,       // Бейджи
    val labelSmall: TextStyle,        // Мелкие подписи
    val codeBlock: TextStyle          // Monospace для кода
)

/**
 * Базовая типографика
 */
val DefaultTypography = AppTypography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 56.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
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
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
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
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
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
    val large = 20.dp
    val extraLarge = 24.dp
    val huge = 32.dp
    val massive = 48.dp
}

/**
 * Размеры иконок
 */
object IconSize {
    val small = 16.dp
    val medium = 24.dp
    val large = 32.dp
    val extraLarge = 48.dp
    val huge = 64.dp
}

val LocalAppTypography = staticCompositionLocalOf { DefaultTypography }
val LocalAppShapes = staticCompositionLocalOf { DefaultShapes }
