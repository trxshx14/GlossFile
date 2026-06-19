package com.cararag.glossfile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cararag.glossfile.data.*
import com.cararag.glossfile.ui.SortMode
import com.cararag.glossfile.ui.VanityViewModel
import com.cararag.glossfile.ui.components.ProductCard
import com.cararag.glossfile.ui.theme.DustyRose
import com.cararag.glossfile.ui.theme.Ink
import com.cararag.glossfile.ui.theme.InkSoft
import com.cararag.glossfile.ui.theme.TrackGrey
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/* ------------------------------- Vanity ---------------------------------- */

@Composable
fun ProjectPanScreen(vm: VanityViewModel) {
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(8.dp))
        Text("Project Pan", style = MaterialTheme.typography.headlineLarge, color = Ink)
        Text(
            "Finish what you have. Star items to track cost per use.",
            style = MaterialTheme.typography.bodyMedium,
            color = InkSoft
        )
        Spacer(Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            items(vm.products, key = { it.id }) { p ->
                PanRow(
                    product = p,
                    onToggle = { vm.toggleProjectPan(p.id) },
                    onUse = { vm.logUse(p.id) }
                )
            }
        }
    }
}

@Composable
private fun PanRow(product: Product, onToggle: () -> Unit, onUse: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = if (product.inProjectPan) 2.dp else 0.dp
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium, color = Ink)
                val sub = "${product.usesLogged} uses" +
                        (product.costPerUse?.let { "  ·  $${"%.2f".format(it)}/use" } ?: "")
                Text(sub, style = MaterialTheme.typography.labelSmall, color = InkSoft)
            }
            if (product.inProjectPan) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    onClick = onUse
                ) {
                    Text(
                        "+1 use",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
            }
            Surface(
                shape = CircleShape,
                color = if (product.inProjectPan) DustyRose.copy(alpha = 0.25f) else TrackGrey,
                onClick = onToggle,
                modifier = Modifier.size(40.dp)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(if (product.inProjectPan) "★" else "☆", color = DustyRose, fontSize = 18.sp)
                }
            }
        }
    }
}