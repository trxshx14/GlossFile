package com.dink.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dink.app.ui.theme.DinkColors
import kotlin.math.cos
import kotlin.math.sin

/**
 * Dink brand lockup: a neon paddle-and-ball emblem with the "Dink" wordmark below it.
 *
 * The emblem is drawn with Canvas as a faithful placeholder — swap [DinkLogoMark] for
 * `Image(painterResource(R.drawable.ic_dink_logo), ...)` once you export the real vector.
 */
@Composable
fun DinkLogo(
    modifier: Modifier = Modifier,
    markSize: Int = 120,
    wordSize: Int = 64,
    showWordmark: Boolean = true,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        DinkLogoMark(Modifier.size(markSize.dp))
        if (showWordmark) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Dink",
                color = DinkColors.TextPrimary,
                fontSize = wordSize.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-2).sp,
            )
        }
    }
}

/** The emblem only: a lime pickleball paddle with a perforated ball at the upper right. */
@Composable
fun DinkLogoMark(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val s = size.minDimension
        val lime = DinkColors.NeonLime
        val bg = DinkColors.CourtGreen

        // --- Paddle ---------------------------------------------------------
        // Handle: a rounded bar angling down to the lower-left.
        rotate(degrees = -32f, pivot = Offset(s * 0.34f, s * 0.62f)) {
            val handle = RoundRect(
                rect = Rect(
                    offset = Offset(s * 0.26f, s * 0.58f),
                    size = Size(s * 0.16f, s * 0.42f),
                ),
                radiusX = s * 0.08f, radiusY = s * 0.08f,
            )
            drawPath(Path().apply { addRoundRect(handle) }, color = lime)
        }
        // Paddle face: a big rounded oval.
        drawOval(
            color = lime,
            topLeft = Offset(s * 0.16f, s * 0.10f),
            size = Size(s * 0.56f, s * 0.62f),
        )

        // --- Ball -----------------------------------------------------------
        val ballCenter = Offset(s * 0.66f, s * 0.34f)
        val ballR = s * 0.22f
        drawCircle(color = lime, radius = ballR, center = ballCenter)
        // Separate the ball from the paddle with a dark ring.
        drawCircle(
            color = bg, radius = ballR, center = ballCenter,
            style = Stroke(width = s * 0.03f),
        )
        // Perforations.
        drawBallHoles(center = ballCenter, ballRadius = ballR, holeColor = bg, holes = 7)
    }
}

/** Punches evenly spaced holes around a ring inside a ball — the pickleball look. */
private fun DrawScope.drawBallHoles(
    center: Offset,
    ballRadius: Float,
    holeColor: androidx.compose.ui.graphics.Color,
    holes: Int,
) {
    val ringR = ballRadius * 0.52f
    val holeR = ballRadius * 0.14f
    for (i in 0 until holes) {
        val a = (i / holes.toFloat()) * (2f * Math.PI).toFloat()
        val hx = center.x + ringR * cos(a)
        val hy = center.y + ringR * sin(a)
        drawCircle(color = holeColor, radius = holeR, center = Offset(hx, hy))
    }
    // center hole
    drawCircle(color = holeColor, radius = holeR, center = center)
}
