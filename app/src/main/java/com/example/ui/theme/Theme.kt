package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class ThemeMode(val displayName: String) {
    LIGHT("Clair"),
    DARK("Sombre"),
    SYSTEM("Système")
}

// Modern Light Color Scheme (Sophisticated, Crisp & High-Contrast)
private val ModernLightColorScheme = lightColorScheme(
    primary = PrimaryIndigo,
    onPrimary = Color.White,
    primaryContainer = PrimaryIndigoContainerLight,
    onPrimaryContainer = OnPrimaryIndigoContainerLight,
    secondary = AccentCyan,
    onSecondary = Color.White,
    secondaryContainer = StatusBlueContainer,
    onSecondaryContainer = StatusBlue,
    tertiary = AccentPurple,
    onTertiary = Color.White,
    background = SurfaceLight,
    onBackground = TextPrimaryLight,
    surface = CardBackgroundLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = TextSecondaryLight,
    outline = BorderLight,
    outlineVariant = CardBorderLight,
    error = StatusRed,
    errorContainer = StatusRedContainer,
    onError = Color.White
)

// Modern Dark Color Scheme (Sleek Midnight Obsidian)
private val ModernDarkColorScheme = darkColorScheme(
    primary = PrimaryIndigoDark,
    onPrimary = Color(0xFF1E1B4B),
    primaryContainer = PrimaryIndigoContainerDark,
    onPrimaryContainer = OnPrimaryIndigoContainerDark,
    secondary = StatusBlueDark,
    onSecondary = Color(0xFF00354A),
    secondaryContainer = StatusBlueContainerDark,
    onSecondaryContainer = Color(0xFFBAE6FD),
    tertiary = StatusOrangeDark,
    onTertiary = Color(0xFF541C00),
    background = SurfaceDark,
    onBackground = TextPrimaryDark,
    surface = CardBackgroundDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = TextSecondaryDark,
    outline = CardBorderDark,
    outlineVariant = Color(0xFF334155),
    error = StatusRedDark,
    errorContainer = StatusRedContainerDark,
    onError = Color(0xFF450A0A)
)

@Composable
fun AppTheme(
    themeMode: ThemeMode = ThemeMode.LIGHT,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (darkTheme) ModernDarkColorScheme else ModernLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AppTheme(
        themeMode = if (darkTheme) ThemeMode.DARK else ThemeMode.LIGHT,
        dynamicColor = dynamicColor,
        content = content
    )
}
