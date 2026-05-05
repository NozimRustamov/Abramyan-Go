package tj.abramyan.go.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun AbramyanGoTheme(
    content: @Composable () -> Unit
) {
    val fonts = rememberAppFonts()
    val typography = rememberAppTypography(fonts)
    CompositionLocalProvider(
        LocalAppColors provides DarkAppColors,
        LocalAppFonts provides fonts,
        LocalAppTypography provides typography,
        LocalAppShapes provides DefaultShapes,
        content = content
    )
}

object AppTheme {
    val colors: AppColors
        @Composable @ReadOnlyComposable
        get() = LocalAppColors.current

    val fonts: AppFonts
        @Composable @ReadOnlyComposable
        get() = LocalAppFonts.current

    val typography: AppTypography
        @Composable @ReadOnlyComposable
        get() = LocalAppTypography.current

    val shapes: AppShapes
        @Composable @ReadOnlyComposable
        get() = LocalAppShapes.current
}
