package com.cararag.glossfile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GlossLightColors = lightColorScheme(
    primary = DustyRose,
    onPrimary = Color.White,
    secondary = Peach,
    background = Cream,
    onBackground = Ink,
    surface = CreamCard,
    onSurface = Ink,
    surfaceVariant = Color(0xFFF3E9E1),
    onSurfaceVariant = InkSoft,
)

@Composable
fun GlossFileTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GlossLightColors,
        typography = GlossTypography,
        content = content
    )
}