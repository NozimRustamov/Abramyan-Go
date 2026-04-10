package com.abramyango.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Liquid Glass Design System Colors
 * Вдохновлено Apple iOS 26 Liquid Glass
 */
@Immutable
data class AppColors(
    // Background layer
    val backgroundGradientStart: Color,
    val backgroundGradientEnd: Color,
    
    // Solid layer
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textOnAccent: Color,
    
    // Accent colors
    val accentPrimary: Color,
    val accentError: Color,

    // Code syntax highlighting
    val codeBackground: Color,
)

/**
 * Тёмная тема (основная)
 */
val DarkAppColors = AppColors(
    // Background - глубокий тёмный градиент
    backgroundGradientStart = Color(0xFF0A0A0F),
    backgroundGradientEnd = Color(0xFF1A1A2E),
    
    // Solid - текст
    textPrimary = Color(0xFFF5F5F7),
    textSecondary = Color(0xB3F5F5F7),        // 70% white
    textTertiary = Color(0x80F5F5F7),          // 50% white
    textOnAccent = Color(0xFFFFFFFF),
    
    // Accent
    accentPrimary = Color(0xFF6366F1),        // Indigo
    accentError = Color(0xFFEF4444),          // Red (soft)
    
    // Code highlighting (Dracula-inspired)
    codeBackground = Color(0xFF1E1E2E),

)

/**
 * Светлая тема
 */
val LightAppColors = AppColors(
    // Background - светлый градиент
    backgroundGradientStart = Color(0xFFF8F9FA),
    backgroundGradientEnd = Color(0xFFE9ECEF),

    
    // Solid - текст
    textPrimary = Color(0xFF1A1A2E),
    textSecondary = Color(0xB31A1A2E),
    textTertiary = Color(0x801A1A2E),
    textOnAccent = Color(0xFFFFFFFF),
    
    // Accent (те же, но чуть насыщеннее)
    accentPrimary = Color(0xFF4F46E5),
    accentError = Color(0xFFDC2626),
    
    // Code highlighting (Light theme)
    codeBackground = Color(0xFFF5F5F5),
)

val LocalAppColors = staticCompositionLocalOf { DarkAppColors }
