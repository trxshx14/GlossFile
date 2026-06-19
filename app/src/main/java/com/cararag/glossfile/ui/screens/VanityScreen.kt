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
fun VanityScreen(vm: VanityViewModel) {
    Column(Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(8.dp))
        Text("Your Vanity", style = MaterialTheme.typography.headlineLarge, color = Ink)
        Text(
            "${vm.products.size} products tracked",
            style = MaterialTheme.typography.bodyMedium,
            color = InkSoft
        )
        Spacer(Modifier.height(16.dp))

        // Category facets
        val categories = listOf<Category?>(null) + Category.values().toList()
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { cat ->
                FilterChip(
                    selected = vm.selectedCategory == cat,
                    onClick = { vm.setCategory(cat) },
                    label = { Text(cat?.label ?: "All") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Sort
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Sort:", style = MaterialTheme.typography.labelSmall, color = InkSoft)
            Spacer(Modifier.width(8.dp))
            SortPill("Expiring soon", vm.sortMode == SortMode.EXPIRING_SOON) {
                vm.setSort(SortMode.EXPIRING_SOON)
            }
            Spacer(Modifier.width(8.dp))
            SortPill("Recently opened", vm.sortMode == SortMode.RECENT) {
                vm.setSort(SortMode.RECENT)
            }
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            items(vm.visibleProducts, key = { it.id }) { p ->
                ProductCard(product = p, onLogUse = { vm.logUse(p.id) })
            }
        }
    }
}

@Composable
private fun SortPill(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        else MaterialTheme.colorScheme.surface,
        onClick = onClick
    ) {
        Text(
            text,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) MaterialTheme.colorScheme.primary else InkSoft,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}