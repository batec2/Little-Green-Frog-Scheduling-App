package com.example.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color


private val LightColors = lightColorScheme(
    //primary = md_theme_light_primary,
    primary = md_theme_light_morningShift, //morning shift light
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    //onTertiaryContainer = md_theme_light_onTertiaryContainer,
    onTertiaryContainer = md_theme_light_eveningShift, //evening shift light
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,

    )


private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    //tertiaryContainer = md_theme_dark_tertiaryContainer,
    tertiaryContainer = md_theme_dark_morningShift, //morning shift dark
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)

class CustomColor {
    companion object {
        val specialDay: Color
            @Composable get() = if (isSystemInDarkTheme()) {
                md_theme_dark_specialDay // Dark theme special day color
            } else md_theme_light_specialDay// Light theme special day color


        val secondaryBackground: Color
            @Composable get() = if (isSystemInDarkTheme()) {
                md_theme_dark_secondaryBackground // Dark theme special day color
            } else md_theme_light_secondaryBackground// Light theme special day color


        val shiftRowNightBackground: Color
            @Composable get() = if (isSystemInDarkTheme()) {
                md_theme_dark_shift_row_night
            } else
                md_theme_light_shift_row_night

        val shiftRowDayBackground: Color
            @Composable get() = if (isSystemInDarkTheme()) {
                md_theme_dark_shift_row_day
            } else
                md_theme_light_shift_row_day

        val shiftRowFullBackground: Color
            @Composable get() = if (isSystemInDarkTheme()) {
                md_theme_dark_shift_row_full
            } else
                md_theme_light_shift_row_full

        val inactiveMonthDayBackground: Color
            @Composable get() = if (isSystemInDarkTheme()) {
                Color.DarkGray
            } else
                md_theme_light_inactive_month_days

        val shiftView1: Color
            @ReadOnlyComposable
            @Composable get() = if (isSystemInDarkTheme()) {
                md_theme_dark_shift1
            } else
                md_theme_light_shift1

        val shiftView2: Color
            @ReadOnlyComposable
            @Composable get() = if (isSystemInDarkTheme()) {
                md_theme_dark_shift2
            } else
                md_theme_light_shift2

        val shiftView3: Color
            @ReadOnlyComposable
            @Composable get() = if(isSystemInDarkTheme()) {
                md_theme_dark_shift3
            } else
                md_theme_light_shift3

        val shiftView4: Color
            @ReadOnlyComposable
            @Composable get() = if(isSystemInDarkTheme()) {
                md_theme_dark_shift4
            } else
                md_theme_light_shift4

        val shiftView5: Color
            @ReadOnlyComposable
            @Composable get() = if(isSystemInDarkTheme()) {
                md_theme_dark_shift5
            } else
                md_theme_light_shift5

        val shiftView6: Color
            @ReadOnlyComposable
            @Composable get() = if(isSystemInDarkTheme()) {
                md_theme_dark_shift6
            } else
                md_theme_light_shift6
    }

}


@Composable
fun F23HopperTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}