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
    val backgroundPrimary: Color,
    val backgroundGradientStart: Color,
    val backgroundGradientEnd: Color,
    
    // Glass layer
    val glassSurface: Color,
    val glassBorder: Color,
    val glassHighlight: Color,
    
    // Solid layer
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textOnAccent: Color,
    
    // Accent colors
    val accentPrimary: Color,
    val accentSuccess: Color,
    val accentError: Color,
    val accentWarning: Color,
    val accentCombo: Color,
    
    // Code syntax highlighting
    val codeBackground: Color,
    val codeKeyword: Color,
    val codeString: Color,
    val codeNumber: Color,
    val codeComment: Color,
    val codeFunction: Color,
    val codeVariable: Color,
    
    // World-specific accent
    val worldAccent: Color,
    
    // Energy & Progress
    val energyFull: Color,
    val energyLow: Color,
    val xpBar: Color,
    val streakFlame: Color
)

/**
 * Тёмная тема (основная)
 */
val DarkAppColors = AppColors(
    // Background - глубокий тёмный градиент
    backgroundPrimary = Color(0xFF0A0A0F),
    backgroundGradientStart = Color(0xFF0A0A0F),
    backgroundGradientEnd = Color(0xFF1A1A2E),
    
    // Glass - полупрозрачные элементы
    glassSurface = Color(0x26FFFFFF),        // 15% white
    glassBorder = Color(0x40FFFFFF),          // 25% white
    glassHighlight = Color(0x4DFFFFFF),       // 30% white
    
    // Solid - текст
    textPrimary = Color(0xFFF5F5F7),
    textSecondary = Color(0xB3F5F5F7),        // 70% white
    textTertiary = Color(0x80F5F5F7),          // 50% white
    textOnAccent = Color(0xFFFFFFFF),
    
    // Accent
    accentPrimary = Color(0xFF6366F1),        // Indigo
    accentSuccess = Color(0xFF10B981),        // Emerald
    accentError = Color(0xFFEF4444),          // Red (soft)
    accentWarning = Color(0xFFF59E0B),        // Amber
    accentCombo = Color(0xFFFF6B35),          // Orange fire
    
    // Code highlighting (Dracula-inspired)
    codeBackground = Color(0xFF1E1E2E),
    codeKeyword = Color(0xFFFF79C6),          // Pink
    codeString = Color(0xFFF1FA8C),           // Yellow
    codeNumber = Color(0xFFBD93F9),           // Purple
    codeComment = Color(0xFF6272A4),          // Gray-blue
    codeFunction = Color(0xFF50FA7B),         // Green
    codeVariable = Color(0xFF8BE9FD),         // Cyan
    
    // World accent (default, changes per world)
    worldAccent = Color(0xFF4CAF50),
    
    // Energy & Progress
    energyFull = Color(0xFF22C55E),
    energyLow = Color(0xFFEF4444),
    xpBar = Color(0xFF8B5CF6),                // Violet
    streakFlame = Color(0xFFFF6B35)           // Orange
)

/**
 * Светлая тема
 */
val LightAppColors = AppColors(
    // Background - светлый градиент (чуть темнее, чтобы белые карточки выделялись)
    backgroundPrimary = Color(0xFFEEF2F7),
    backgroundGradientStart = Color(0xFFEEF2F7),
    backgroundGradientEnd = Color(0xFFDCE3EC),

    // Glass - почти solid белые карточки для контраста с фоном
    glassSurface = Color(0xF2FFFFFF),         // ~95% white
    glassBorder = Color(0x1F000000),          // ~12% black outline
    glassHighlight = Color(0xFFFFFFFF),       // pure white top stop
    
    // Solid - текст
    textPrimary = Color(0xFF1A1A2E),
    textSecondary = Color(0xB31A1A2E),
    textTertiary = Color(0x801A1A2E),
    textOnAccent = Color(0xFFFFFFFF),
    
    // Accent (те же, но чуть насыщеннее)
    accentPrimary = Color(0xFF4F46E5),
    accentSuccess = Color(0xFF059669),
    accentError = Color(0xFFDC2626),
    accentWarning = Color(0xFFD97706),
    accentCombo = Color(0xFFEA580C),
    
    // Code highlighting (Light theme)
    codeBackground = Color(0xFFF5F5F5),
    codeKeyword = Color(0xFFD73A49),          // Red
    codeString = Color(0xFF22863A),           // Green
    codeNumber = Color(0xFF005CC5),           // Blue
    codeComment = Color(0xFF6A737D),          // Gray
    codeFunction = Color(0xFF6F42C1),         // Purple
    codeVariable = Color(0xFF005CC5),         // Blue
    
    // World accent
    worldAccent = Color(0xFF4CAF50),
    
    // Energy & Progress
    energyFull = Color(0xFF16A34A),
    energyLow = Color(0xFFDC2626),
    xpBar = Color(0xFF7C3AED),
    streakFlame = Color(0xFFEA580C)
)

/**
 * Цвета для миров
 */
object WorldColors {
    val ValleyOfBeginnings = Color(0xFF4CAF50)      // Зелёный
    val FieldsOfTruth = Color(0xFF2196F3)           // Синий
    val CrossroadsOfFate = Color(0xFF9C27B0)        // Фиолетовый
    val TimeLoop = Color(0xFFFF9800)                 // Оранжевый
    val DataRiver = Color(0xFF00BCD4)               // Циан
    val OceanOfArrays = Color(0xFF3F51B5)           // Индиго
    val MatrixCitadel = Color(0xFF607D8B)           // Серо-синий
    val TextForest = Color(0xFF795548)              // Коричневый
    val ArchitectArchives = Color(0xFFF44336)       // Красный
}

val LocalAppColors = staticCompositionLocalOf { DarkAppColors }
