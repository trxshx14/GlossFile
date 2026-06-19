package com.cararag.glossfile.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cararag.glossfile.ui.theme.BlushPink
import com.cararag.glossfile.ui.theme.Cream
import com.cararag.glossfile.ui.theme.DustyRose
import com.cararag.glossfile.ui.theme.Ink
import com.cararag.glossfile.ui.theme.InkSoft
import com.cararag.glossfile.ui.theme.Peach
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.drawscope.clipRect

// Smooth, premium ease-in-out.
private val Graceful = CubicBezierEasing(0.33f, 0f, 0.18f, 1f)

// System cursive works immediately. For a nicer script, bundle a Google font
// (e.g. Dancing Script) in res/font and change this to:
//     private val Script = FontFamily(Font(R.font.dancing_script))
private val Script = FontFamily.Cursive

/**
 * Elevated splash:
 *  - Soft mesh background (three drifting, breathing gradient orbs).
 *  - A framed cursive monogram, the cursive wordmark written on,
 *    and a tracked-out tagline between hairlines.
 *  - Everything arrives in a gentle staggered sequence.
 */
@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val bg = remember { Animatable(0f) }       // mesh fade-in
    val mark = remember { Animatable(0f) }     // monogram scale + fade
    val write = remember { Animatable(0f) }    // wordmark write-on
    val tag = remember { Animatable(0f) }      // tagline fade

    // Slow continuous "breathing" so the background feels alive, not static.
    val infinite = rememberInfiniteTransition(label = "mesh")
    val breathe by infinite.animateFloat(
        initialValue = 1f,
        targetValue = 1.10f,
        animationSpec = infiniteRepeatable(tween(4500), RepeatMode.Reverse),
        label = "breathe"
    )

    LaunchedEffect(Unit) {
        bg.animateTo(1f, tween(800, easing = Graceful))
        mark.animateTo(1f, tween(700, easing = Graceful))
        write.animateTo(1f, tween(1500, easing = Graceful))
        tag.animateTo(1f, tween(700, easing = Graceful))
        delay(900)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
        contentAlignment = Alignment.Center
    ) {
        // ---- Mesh background: overlapping soft orbs ----
        Box(modifier = Modifier
            .fillMaxSize()
            .alpha(bg.value)
        ) {
            Orb(280.dp, Peach, Alignment.TopStart, (-70).dp, (-90).dp, breathe)
            Orb(300.dp, BlushPink, Alignment.BottomEnd, 80.dp, 90.dp, breathe)
            Orb(220.dp, DustyRose, Alignment.CenterEnd, 60.dp, (-40).dp, breathe)
        }

        // ---- Foreground lockup ----
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Framed monogram
            Box(
                modifier = Modifier
                    .scale(0.8f + 0.2f * mark.value)
                    .alpha(mark.value)
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(BlushPink.copy(alpha = 0.18f))
                    .border(BorderStroke(1.dp, DustyRose.copy(alpha = 0.65f)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "g",
                    fontFamily = Script,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 34.sp,
                    color = Ink
                )
            }

            Spacer(Modifier.height(18.dp))

            // Cursive wordmark, written on left-to-right
            Text(
                text = "Glossfile",
                fontFamily = Script,
                fontWeight = FontWeight.Normal,
                fontSize = 54.sp,
                color = Ink,
                modifier = Modifier.drawWithContent {
                    clipRect(right = size.width * write.value) {
                        this@drawWithContent.drawContent()
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            // Tracked tagline between two hairlines
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.alpha(tag.value)
            ) {
                Box(Modifier.width(18.dp).height(1.dp).background(InkSoft))
                Text(
                    text = "BEAUTY  INVENTORY",
                    fontSize = 10.sp,
                    letterSpacing = 3.sp,
                    color = InkSoft,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Box(Modifier.width(18.dp).height(1.dp).background(InkSoft))
            }
        }
    }
}

/** A single soft gradient orb for the mesh background. */
@Composable
private fun Orb(
    diameter: androidx.compose.ui.unit.Dp,
    color: Color,
    alignment: Alignment,
    offsetX: androidx.compose.ui.unit.Dp,
    offsetY: androidx.compose.ui.unit.Dp,
    breathe: Float,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .offset(x = offsetX, y = offsetY)
                .size(diameter)
                .scale(breathe)
                .background(
                    Brush.radialGradient(
                        colors = listOf(color.copy(alpha = 0.55f), color.copy(alpha = 0f))
                    ),
                    CircleShape
                )
        )
    }
}