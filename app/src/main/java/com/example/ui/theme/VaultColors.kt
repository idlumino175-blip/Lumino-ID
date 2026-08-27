package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Extended Clip Vault design tokens that go beyond Material3's colorScheme.
 * Eliminates all hardcoded isDark color branching throughout the app.
 */
@Immutable
data class VaultColors(
    // Surfaces & Cards
    val cardSurface: Color,
    val cardBorder: Color,
    val sheetBackground: Color,

    // Inputs
    val inputBackground: Color,
    val inputBorder: Color,
    val inputBorderFocused: Color,

    // Dock / Navigation
    val dockBackground: Color,
    val dockBorder: Color,

    // Accent / Action
    val accentPrimary: Color,
    val accentOnPrimary: Color,
    val accentSecondary: Color,
    val accentSecondaryText: Color,

    // Text
    val mutedText: Color,
    val bodyText: Color,
    val subtleText: Color,

    // Code & Snippet
    val codeSnippetBg: Color,

    // Semantic
    val destructive: Color,
    val pinnedStar: Color = Color(0xFFF5B700),

    // Reuse / Copy button
    val reuseBg: Color,
    val reuseBgCopied: Color,
    val reuseText: Color,
)

val LightVaultColors = VaultColors(
    cardSurface = Color.White,
    cardBorder = BorderLight,
    sheetBackground = Color(0xFFFAF7F2),

    inputBackground = Color.White,
    inputBorder = BorderLight,
    inputBorderFocused = PrimaryActionLight,

    dockBackground = Color.White,
    dockBorder = BorderLight,

    accentPrimary = PrimaryActionLight,
    accentOnPrimary = Color.White,
    accentSecondary = SecondarySurfaceLight,
    accentSecondaryText = SecondaryForegroundLight,

    mutedText = Color(0xFF7A8B99),
    bodyText = InkLight,
    subtleText = Color(0xFF5E7285),

    codeSnippetBg = CodeSnippetBgLight,

    destructive = DestructiveLight,

    reuseBg = PrimaryActionLight,
    reuseBgCopied = Color(0xFF143B33),
    reuseText = Color.White,
)

val DarkVaultColors = VaultColors(
    cardSurface = CardSurfaceDark,
    cardBorder = BorderDark,
    sheetBackground = CardSurfaceDark,

    inputBackground = CanvasDark,
    inputBorder = BorderDark,
    inputBorderFocused = PrimaryActionDark,

    dockBackground = CardSurfaceDark,
    dockBorder = BorderDark,

    accentPrimary = PrimaryActionDark,
    accentOnPrimary = PrimaryForegroundDark,
    accentSecondary = SecondarySurfaceDark,
    accentSecondaryText = InkDark,

    mutedText = MutedForegroundDark,
    bodyText = InkDark,
    subtleText = MutedForegroundDark,

    codeSnippetBg = CodeSnippetBgDark,

    destructive = DestructiveDark,

    reuseBg = PrimaryActionDark,
    reuseBgCopied = Color(0xFF143B33),
    reuseText = PrimaryForegroundDark,
)

val LocalVaultColors = staticCompositionLocalOf { LightVaultColors }

/**
 * Convenience accessor: `VaultTheme.colors.accentPrimary`
 */
object VaultTheme {
    val colors: VaultColors
        @Composable
        get() = LocalVaultColors.current
}
