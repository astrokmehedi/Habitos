package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = VolcanicOrange,
    secondary = MutedSilver,
    tertiary = EmeraldGreen,
    background = PitchBlack,
    surface = DarkCharcoal,
    onPrimary = TextWhite,
    onSecondary = PitchBlack,
    onBackground = TextWhite,
    onSurface = CleanWhite,
    surfaceVariant = MatteGray,
    onSurfaceVariant = MutedSilver,
    error = WarningRed
)

@Composable
fun DisciplineTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}

// Keep a backward compatible name just in case, mapping directly to our theme
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    DisciplineTheme(content = content)
}

