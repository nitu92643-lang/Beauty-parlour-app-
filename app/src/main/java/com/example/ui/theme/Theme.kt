package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = DulhanPink,
    onPrimary = Color.White,
    primaryContainer = DulhanBlush,
    onPrimaryContainer = DulhanMaroonDeep,

    secondary = DulhanMaroon,
    onSecondary = Color.White,
    secondaryContainer = DulhanBlushLight,
    onSecondaryContainer = DulhanMaroonDeep,

    tertiary = DulhanGoldDark,
    onTertiary = Color.White,
    tertiaryContainer = DulhanGoldPale,
    onTertiaryContainer = Color(0xFF433000),

    background = DulhanCream,
    onBackground = DulhanTextDark,
    surface = DulhanSurfaceLight,
    onSurface = DulhanTextDark,
    surfaceVariant = Color(0xFFF9EEF3),
    onSurfaceVariant = DulhanTextMuted,
    outline = DulhanBorder,
    outlineVariant = Color(0xFFF0E0E8)
)

private val DarkColorScheme = darkColorScheme(
    primary = DulhanPinkLight,
    onPrimary = DulhanMaroonDeep,
    primaryContainer = DulhanMaroon,
    onPrimaryContainer = DulhanBlush,

    secondary = DulhanPink,
    onSecondary = Color.White,
    secondaryContainer = DulhanWine,
    onSecondaryContainer = DulhanBlushLight,

    tertiary = DulhanGoldBright,
    onTertiary = Color(0xFF332000),
    tertiaryContainer = Color(0xFF503800),
    onTertiaryContainer = DulhanGoldPale,

    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = Color(0xFF5B4350),
    outlineVariant = Color(0xFF422C39)
)

@Composable
fun DulhanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve Dulhan signature branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
