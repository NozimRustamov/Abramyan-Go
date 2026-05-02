package com.abramyango.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun AbramyanGoTheme(
    content: @Composable () -> Unit
) {
    val typography = rememberAppTypography()
    CompositionLocalProvider(
        LocalAppColors provides DarkAppColors,
        LocalAppTypography provides typography,
        LocalAppShapes provides DefaultShapes,
        content = content
    )
}

object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current
}
