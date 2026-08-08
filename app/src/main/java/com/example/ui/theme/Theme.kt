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
    primary = LilacPrimary,
    onPrimary = CardSurface,
    primaryContainer = LilacSoft,
    onPrimaryContainer = PurpleDark,
    secondary = PinkAccent,
    onSecondary = CardSurface,
    secondaryContainer = PinkPastel,
    onSecondaryContainer = PurpleDark,
    tertiary = BluePrimary,
    onTertiary = CardSurface,
    tertiaryContainer = BluePastel,
    onTertiaryContainer = PurpleDark,
    background = CreamBackground,
    onBackground = TextDark,
    surface = CardSurface,
    onSurface = TextDark,
    surfaceVariant = LilacSoft,
    onSurfaceVariant = PurpleDark
)

private val DarkColorScheme = darkColorScheme(
    primary = LilacSoft,
    onPrimary = PurpleDark,
    primaryContainer = PurpleDark,
    onPrimaryContainer = LilacSoft,
    secondary = PinkPastel,
    onSecondary = PurpleDark,
    secondaryContainer = PurpleDark,
    onSecondaryContainer = PinkPastel,
    tertiary = BluePastel,
    onTertiary = PurpleDark,
    background = Color(0xFF1E1A29),
    onBackground = Color(0xFFF0EBF9),
    surface = Color(0xFF282338),
    onSurface = Color(0xFFF0EBF9)
)

@Composable
fun MiSuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
