package com.cararag.glossfile.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Minimal, airy type scale. Swap fontFamily for a custom font (e.g. a rounded
// sans like Nunito or Quicksand) once you add it to res/font to nail the look.
val GlossTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.SemiBold, fontSize = 28.sp, letterSpacing = (-0.5).sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium, fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal, fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium, fontSize = 12.sp, letterSpacing = 0.4.sp
    ),
)