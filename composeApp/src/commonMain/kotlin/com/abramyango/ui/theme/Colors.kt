package com.abramyango.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColors(
    val backgroundPrimary: Color,
    val backgroundGradientStart: Color,
    val backgroundGradientEnd: Color,

    val glassSurface: Color,   // mantle — card background
    val glassBorder: Color,    // surface0 — borders
    val glassHighlight: Color,

    val surface0: Color,
    val surface1: Color,
    val crust: Color,

    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textOnAccent: Color,

    val accentPrimary: Color,
    val accentError: Color,

    val codeBackground: Color,
)

// Catppuccin Mocha — always dark
val DarkAppColors = AppColors(
    backgroundPrimary       = Color(0xFF1E1E2E),
    backgroundGradientStart = Color(0xFF1E1E2E),
    backgroundGradientEnd   = Color(0xFF11111B),

    glassSurface   = Color(0xFF181825),
    glassBorder    = Color(0xFF313244),
    glassHighlight = Color(0xFF313244),

    surface0 = Color(0xFF313244),
    surface1 = Color(0xFF45475A),
    crust    = Color(0xFF11111B),

    textPrimary   = Color(0xFFCDD6F4),
    textSecondary = Color(0xFFA6ADC8),
    textTertiary  = Color(0xFF6C7086),
    textOnAccent  = Color(0xFFFFFFFF),

    accentPrimary = Color(0xFFA6E3A1),
    accentError   = Color(0xFFF38BA8),

    codeBackground = Color(0xFF11111B),
)

object CatColors {
    val green    = Color(0xFFA6E3A1)
    val blue     = Color(0xFF89B4FA)
    val teal     = Color(0xFF94E2D5)
    val sky      = Color(0xFF89DCEB)
    val yellow   = Color(0xFFF9E2AF)
    val mauve    = Color(0xFFCBA6F7)
    val lavender = Color(0xFFB4BEFE)
    val peach    = Color(0xFFFAB387)
    val pink     = Color(0xFFF38BA8)
    val muted    = Color(0xFF6C7086)
}

data class CategoryStyle(val icon: String, val accentColor: Color)

fun categoryStyleFor(categoryId: String): CategoryStyle =
    when (categoryId.substringBefore('_').lowercase()) {
        "begin"   -> CategoryStyle("▶", CatColors.green)
        "integer" -> CategoryStyle("#", CatColors.blue)
        "boolean" -> CategoryStyle("?", CatColors.yellow)
        "if"      -> CategoryStyle("if", CatColors.yellow)
        "case"    -> CategoryStyle("≡", CatColors.mauve)
        "for"     -> CategoryStyle("↺", CatColors.sky)
        "while"   -> CategoryStyle("⟳", CatColors.lavender)
        "series"  -> CategoryStyle("∑", CatColors.teal)
        "proc"    -> CategoryStyle("ƒ", CatColors.peach)
        else      -> CategoryStyle("·", CatColors.muted)
    }

fun languageColor(language: String): Color =
    when (language.lowercase()) {
        "java"       -> CatColors.peach
        "csharp"     -> CatColors.mauve
        "javascript" -> CatColors.yellow
        "python"     -> CatColors.blue
        else         -> CatColors.muted
    }

fun languageLabel(language: String): String =
    when (language.lowercase()) {
        "java"       -> "Java"
        "csharp"     -> "C#"
        "javascript" -> "JS"
        "python"     -> "Python"
        else         -> language
    }

fun languageFilename(language: String): String =
    when (language.lowercase()) {
        "java"       -> "Main.java"
        "csharp"     -> "Main.cs"
        "javascript" -> "index.js"
        "python"     -> "main.py"
        else         -> "solution"
    }

val LocalAppColors = staticCompositionLocalOf { DarkAppColors }
