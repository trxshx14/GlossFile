package com.dink.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Dink color palette.
 * Background is a very dark matte "court green"; the accent is a vibrant electric lime.
 */
object DinkColors {
    // Backgrounds
    val CourtGreen = Color(0xFF121B16)          // primary matte court-green background
    val CourtGreenDeep = Color(0xFF0D140F)      // gradient bottom / deepest shade
    val Surface = Color(0xFF1A241D)             // raised surfaces / badges

    // Accent (neon / electric lime)
    val NeonLime = Color(0xFFCCFF00)
    val NeonLimeBright = Color(0xFFBFFF00)

    // Inputs — "dark transparent gray" panel with a subtle gray border that turns neon on focus
    val InputContainer = Color(0x401F2A24)      // ~25% opaque dark gray-green
    val InputBorderIdle = Color(0xFF31413A)     // thin, low-contrast border when unfocused
    val InputBorderFocused = NeonLime           // thin neon border when focused

    // Court line overlay (subtle geometric background)
    val CourtLine = Color(0x14CCFF00)           // faint lime lines (~8% alpha)

    // Text
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFF9AA59E)
    val TextHint = Color(0xFF6E7A72)
    val OnAccent = Color(0xFF0E140C)            // near-black text that sits on neon buttons
}
