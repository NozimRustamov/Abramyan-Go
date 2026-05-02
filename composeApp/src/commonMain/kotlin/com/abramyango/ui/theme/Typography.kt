package com.abramyango.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import abramyango.composeapp.generated.resources.Inter_Medium
import abramyango.composeapp.generated.resources.Inter_Regular
import abramyango.composeapp.generated.resources.Inter_SemiBold
import abramyango.composeapp.generated.resources.JetBrainsMono_Bold
import abramyango.composeapp.generated.resources.JetBrainsMono_Medium
import abramyango.composeapp.generated.resources.JetBrainsMono_Regular
import abramyango.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

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
    val codeBlock: TextStyle          // JetBrains Mono для кода
)

/**
 * Inter + JetBrains Mono — JetBrains style typography
 */
@Composable
fun rememberAppTypography(): AppTypography {
    val interFamily = FontFamily(
        Font(Res.font.Inter_Regular, FontWeight.Normal),
        Font(Res.font.Inter_Medium, FontWeight.Medium),
        Font(Res.font.Inter_SemiBold, FontWeight.SemiBold),
    )
    val monoFamily = FontFamily(
        Font(Res.font.JetBrainsMono_Regular, FontWeight.Normal),
        Font(Res.font.JetBrainsMono_Medium, FontWeight.Medium),
        Font(Res.font.JetBrainsMono_Bold, FontWeight.Bold),
    )
    return AppTypography(
        headlineMedium = TextStyle(
            fontFamily = interFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = (-0.3).sp
        ),
        titleLarge = TextStyle(
            fontFamily = interFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 28.sp,
            letterSpacing = (-0.2).sp
        ),
        titleMedium = TextStyle(
            fontFamily = interFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 17.sp,
            lineHeight = 24.sp,
            letterSpacing = (-0.1).sp
        ),
        bodyLarge = TextStyle(
            fontFamily = interFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            letterSpacing = 0.sp
        ),
        labelLarge = TextStyle(
            fontFamily = interFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp
        ),
        labelMedium = TextStyle(
            fontFamily = interFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.1.sp
        ),
        codeBlock = TextStyle(
            fontFamily = monoFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp
        )
    )
}

/**
 * Fallback (только для staticCompositionLocalOf — не используется в UI напрямую)
 */
val DefaultTypography = AppTypography(
    headlineMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 24.sp, lineHeight = 32.sp, letterSpacing = (-0.3).sp),
    titleLarge    = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 28.sp, letterSpacing = (-0.2).sp),
    titleMedium   = TextStyle(fontWeight = FontWeight.Medium,   fontSize = 17.sp, lineHeight = 24.sp, letterSpacing = (-0.1).sp),
    bodyLarge     = TextStyle(fontWeight = FontWeight.Normal,   fontSize = 15.sp, lineHeight = 22.sp, letterSpacing = 0.sp),
    labelLarge    = TextStyle(fontWeight = FontWeight.Medium,   fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.sp),
    labelMedium   = TextStyle(fontWeight = FontWeight.Medium,   fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.1.sp),
    codeBlock     = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 20.sp, letterSpacing = 0.sp)
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
    small      = RoundedCornerShape(8.dp),
    medium     = RoundedCornerShape(12.dp),
    large      = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp),
    glassCard  = RoundedCornerShape(10.dp),
    button     = RoundedCornerShape(8.dp),
    codeBlock  = RoundedCornerShape(6.dp),
    badge      = RoundedCornerShape(4.dp),
    pill       = RoundedCornerShape(50)
)

val LocalAppTypography = staticCompositionLocalOf { DefaultTypography }
val LocalAppShapes     = staticCompositionLocalOf { DefaultShapes }
