package tj.abramyan.go.ui.theme

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
import tj.abramyan.go.shared.resources.Res
import tj.abramyan.go.shared.resources.Inter_Medium
import tj.abramyan.go.shared.resources.Inter_Regular
import tj.abramyan.go.shared.resources.Inter_SemiBold
import tj.abramyan.go.shared.resources.JetBrainsMono_Medium
import tj.abramyan.go.shared.resources.JetBrainsMono_Regular
import org.jetbrains.compose.resources.Font

@Immutable
data class AppFonts(
    val sans: FontFamily,
    val mono: FontFamily,
)

@Composable
fun rememberAppFonts(): AppFonts = AppFonts(
    sans = FontFamily(
        Font(Res.font.Inter_Regular, FontWeight.Normal),
        Font(Res.font.Inter_Medium, FontWeight.Medium),
        Font(Res.font.Inter_SemiBold, FontWeight.SemiBold),
    ),
    mono = FontFamily(
        Font(Res.font.JetBrainsMono_Regular, FontWeight.Normal),
        Font(Res.font.JetBrainsMono_Medium, FontWeight.Medium),
    ),
)

@Immutable
data class AppTypography(
    val headlineMedium: TextStyle,
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val bodyLarge: TextStyle,
    val labelLarge: TextStyle,
    val labelMedium: TextStyle,
    val codeBlock: TextStyle,
)

@Composable
fun rememberAppTypography(fonts: AppFonts): AppTypography = AppTypography(
    headlineMedium = TextStyle(
        fontFamily = fonts.sans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.3).sp
    ),
    titleLarge = TextStyle(
        fontFamily = fonts.sans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.2).sp
    ),
    titleMedium = TextStyle(
        fontFamily = fonts.sans,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.1).sp
    ),
    bodyLarge = TextStyle(
        fontFamily = fonts.sans,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),
    labelLarge = TextStyle(
        fontFamily = fonts.sans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
    labelMedium = TextStyle(
        fontFamily = fonts.sans,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.1.sp
    ),
    codeBlock = TextStyle(
        fontFamily = fonts.mono,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),
)

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
    val pill: Shape,
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
    pill       = RoundedCornerShape(50),
)

val LocalAppFonts = staticCompositionLocalOf<AppFonts> {
    error("AppFonts not provided — wrap content in AbramyanGoTheme")
}
val LocalAppTypography = staticCompositionLocalOf<AppTypography> {
    error("AppTypography not provided — wrap content in AbramyanGoTheme")
}
val LocalAppShapes = staticCompositionLocalOf { DefaultShapes }
