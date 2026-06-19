package com.dink.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.dink.app.ui.theme.DinkColors
import kotlin.math.cos
import kotlin.math.sin

/**
 * Full-screen background: a dark matte court-green gradient, faint geometric court
 * lines, and scattered neon "pickleball" decorations — matching the Dink login art.
 *
 * Drop this as the FIRST child of a Box, then layer content above it.
 */
@Composable
fun CourtBackground(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DinkColors.CourtGreen, DinkColors.CourtGreenDeep)
                )
            )
    ) {
        drawCourtLines()
        drawScatteredBalls()
    }
}

private fun DrawScope.drawCourtLines() {
    val w = size.width
    val h = size.height
    val stroke = 1.5f * density
    val line = DinkColors.CourtLine

    val inset = w * 0.08f
    val left = inset
    val right = w - inset
    val top = h * 0.10f
    val bottom = h * 0.90f

    fun seg(a: Offset, b: Offset) = drawLine(line, a, b, stroke)

    // Boundary
    seg(Offset(left, top), Offset(right, top))
    seg(Offset(left, bottom), Offset(right, bottom))
    seg(Offset(left, top), Offset(left, bottom))
    seg(Offset(right, top), Offset(right, bottom))

    // Net + kitchen lines
    val midY = (top + bottom) / 2f
    val kitchen = (bottom - top) * 0.14f
    seg(Offset(left, midY), Offset(right, midY))
    seg(Offset(left, midY - kitchen), Offset(right, midY - kitchen))
    seg(Offset(left, midY + kitchen), Offset(right, midY + kitchen))

    // Center service line
    val midX = (left + right) / 2f
    seg(Offset(midX, top), Offset(midX, midY - kitchen))
    seg(Offset(midX, midY + kitchen), Offset(midX, bottom))
}

/**
 * Scatters small pickleballs around the screen edges. Positions are fractions of the
 * canvas so they adapt to any screen size; each has a radius, alpha and "filled" flag.
 */
private fun DrawScope.drawScatteredBalls() {
    val w = size.width
    val h = size.height

    data class Deco(val fx: Float, val fy: Float, val r: Float, val alpha: Float, val filled: Boolean)

    val decos = listOf(
        Deco(0.12f, 0.16f, 0.030f, 0.85f, true),   // top-left
        Deco(0.82f, 0.13f, 0.034f, 0.85f, true),   // top-right
        Deco(0.90f, 0.40f, 0.026f, 0.45f, false),  // right outline
        Deco(0.08f, 0.46f, 0.022f, 0.45f, false),  // left outline
        Deco(0.86f, 0.66f, 0.024f, 0.40f, false),  // mid-right outline
        Deco(0.50f, 0.93f, 0.038f, 0.80f, true),   // bottom-center
        Deco(0.16f, 0.88f, 0.020f, 0.35f, false),  // bottom-left outline
    )

    decos.forEach { d ->
        val center = Offset(w * d.fx, h * d.fy)
        val r = w * d.r
        val lime = DinkColors.NeonLime.copy(alpha = d.alpha)
        if (d.filled) {
            drawCircle(color = lime, radius = r, center = center)
            drawBallPerforations(center, r, DinkColors.CourtGreen)
        } else {
            drawCircle(color = lime, radius = r, center = center, style = Stroke(width = 2f * density))
            drawBallPerforations(center, r, lime, dotsOnly = true)
        }
    }
}

private fun DrawScope.drawBallPerforations(
    center: Offset,
    ballRadius: Float,
    color: Color,
    dotsOnly: Boolean = false,
) {
    val ringR = ballRadius * 0.55f
    val holeR = if (dotsOnly) ballRadius * 0.10f else ballRadius * 0.16f
    val count = 6
    for (i in 0 until count) {
        val a = (i / count.toFloat()) * (2f * Math.PI).toFloat()
        val hx = center.x + ringR * cos(a)
        val hy = center.y + ringR * sin(a)
        drawCircle(color = color, radius = holeR, center = Offset(hx, hy))
    }
    drawCircle(color = color, radius = holeR, center = center)
}
