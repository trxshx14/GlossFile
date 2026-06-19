package com.cararag.glossfile.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cararag.glossfile.data.Freshness
import com.cararag.glossfile.data.Product
import com.cararag.glossfile.ui.theme.*

@Composable
fun ProductCard(
    product: Product,
    onLogUse: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val targetColor = when (product.freshness()) {
        Freshness.FRESH -> FreshGreen
        Freshness.SOON -> SoonAmber
        Freshness.EXPIRED -> ExpiredRose
    }
    // Smooth crossfade between freshness states instead of a hard color switch.
    val barColor by animateColorAsState(targetColor, tween(600), label = "barColor")
    val barFraction by animateFloatAsState(product.freshnessFraction(), tween(700), label = "barFraction")

    // Micro-interaction: a gentle "pop" on the check when you log a use.
    var justUsed by remember { mutableStateOf(false) }
    val checkScale by animateFloatAsState(
        targetValue = if (justUsed) 1.25f else 1f,
        animationSpec = tween(180),
        label = "checkScale",
        finishedListener = { justUsed = false }
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Product image placeholder — swap for Coil AsyncImage once you add photos.
                Box(
                    Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Peach.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(product.name.take(1), fontWeight = FontWeight.SemiBold, color = Ink)
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        product.brand.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = InkSoft
                    )
                    Text(
                        product.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Ink
                    )
                }
                // "Used today" tap target.
                Surface(
                    shape = CircleShape,
                    color = barColor.copy(alpha = 0.18f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .clickable {
                                justUsed = true
                                onLogUse()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "✓",
                            color = barColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.scale(checkScale)
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Freshness bar: a track with a colored fill that shrinks as expiry nears.
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(TrackGrey)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(barFraction)
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(barColor)
                )
            }

            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(freshnessLabel(product), style = MaterialTheme.typography.labelSmall, color = barColor)
                product.costPerUse?.let {
                    Text(
                        "$${"%.2f".format(it)}/use",
                        style = MaterialTheme.typography.labelSmall,
                        color = InkSoft
                    )
                }
            }
        }
    }
}

private fun freshnessLabel(product: Product): String {
    val days = product.remainingDays()
    return when {
        days < 0 -> "Expired ${-days}d ago"
        days == 0L -> "Expires today"
        days <= 30 -> "Use soon · ${days}d left"
        else -> "$days days left"
    }
}