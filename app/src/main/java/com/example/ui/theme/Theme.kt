package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryActionLight,
    onPrimary = Color.White,
    primaryContainer = SecondarySurfaceLight,
    onPrimaryContainer = SecondaryForegroundLight,
    secondary = AccentPeachLight,
    onSecondary = AccentPeachForegroundLight,
    secondaryContainer = MutedSurfaceLight,
    onSecondaryContainer = MutedForegroundLight,
    tertiary = BadgeLinkBg,
    onTertiary = BadgeLinkText,
    background = CanvasLight,
    onBackground = InkLight,
    surface = CardSurfaceLight,
    onSurface = InkLight,
    surfaceVariant = MutedSurfaceLight,
    onSurfaceVariant = MutedForegroundLight,
    outline = BorderLight,
    outlineVariant = BorderLight,
    error = DestructiveLight,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryActionDark,
    onPrimary = PrimaryForegroundDark,
    primaryContainer = SecondarySurfaceDark,
    onPrimaryContainer = SecondaryForegroundDark,
    secondary = AccentMintDark,
    onSecondary = AccentMintForegroundDark,
    secondaryContainer = MutedSurfaceDark,
    onSecondaryContainer = MutedForegroundDark,
    tertiary = BadgeCodeBg,
    onTertiary = BadgeCodeText,
    background = CanvasDark,
    onBackground = InkDark,
    surface = CardSurfaceDark,
    onSurface = InkDark,
    surfaceVariant = MutedSurfaceDark,
    onSurfaceVariant = MutedForegroundDark,
    outline = BorderDark,
    outlineVariant = BorderDark,
    error = DestructiveDark,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
