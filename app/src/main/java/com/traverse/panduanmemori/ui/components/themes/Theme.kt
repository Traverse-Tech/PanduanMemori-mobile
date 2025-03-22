package com.traverse.panduanmemori.ui.components.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = lightColors(
    primary = AppColors.Primary.Main,
    primaryVariant = AppColors.Primary.`500`,
    secondary = AppColors.Secondary.Main,
    background = AppColors.Neutral.`10`,
    surface = AppColors.Neutral.`10`,
    onPrimary = AppColors.Neutral.`10`,
    onSecondary = AppColors.Neutral.`10`,
    onBackground = AppColors.Neutral.`100`,
    onSurface = AppColors.Neutral.`100`
)

private val LightColorPalette = lightColors(
    primary = AppColors.Primary.Main,
    primaryVariant = AppColors.Primary.`500`,
    secondary = AppColors.Secondary.Main,
    background = AppColors.Neutral.`10`,
    surface = AppColors.Neutral.`10`,
    onPrimary = AppColors.Neutral.`10`,
    onSecondary = AppColors.Neutral.`10`,
    onBackground = AppColors.Neutral.`100`,
    onSurface = AppColors.Neutral.`100`
)

@Composable
fun PanduanMemoriTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content
    )
}