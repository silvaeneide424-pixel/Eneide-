package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = MintNeon,
    onPrimary = EmeraldDeep,
    primaryContainer = DarkForestCard,
    onPrimaryContainer = DarkTextPrimary,
    secondary = AmberWarmDark,
    onSecondary = EmeraldDeep,
    secondaryContainer = EmeraldPrimary,
    onSecondaryContainer = DarkTextPrimary,
    tertiary = EmeraldVibrant,
    onTertiary = PureWhite,
    background = DarkForestBg,
    onBackground = DarkTextPrimary,
    surface = DarkForestSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkForestCard,
    onSurfaceVariant = DarkTextSecondary,
    outline = EmeraldVibrant,
    error = ErrorLight,
    onError = ErrorRed
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = PureWhite,
    primaryContainer = MintSoft,
    onPrimaryContainer = EmeraldDeep,
    secondary = AmberGold,
    onSecondary = PureWhite,
    secondaryContainer = AmberLight,
    onSecondaryContainer = SlateInk,
    tertiary = EmeraldDeep,
    onTertiary = PureWhite,
    background = CreamBackground,
    onBackground = SlateInk,
    surface = PureWhite,
    onSurface = SlateInk,
    surfaceVariant = MintSurface,
    onSurfaceVariant = SlateMuted,
    outline = BorderSubtle,
    error = ErrorRed,
    onError = PureWhite,
    errorContainer = ErrorLight,
    onErrorContainer = ErrorRed
)

val AppShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp)
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
        shapes = AppShapes,
        content = content
    )
}
