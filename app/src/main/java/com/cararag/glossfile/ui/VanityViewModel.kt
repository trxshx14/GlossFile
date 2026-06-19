package com.cararag.glossfile.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cararag.glossfile.data.Category
import com.cararag.glossfile.data.Product
import java.time.LocalDate

enum class SortMode { RECENT, EXPIRING_SOON }

/**
 * Holds the in-memory vanity. Swap [products] for a Room-backed Flow when you're
 * ready to persist (see the notes that came with this starter).
 */
class VanityViewModel : ViewModel() {

    var products by mutableStateOf(sampleProducts())
        private set

    var selectedCategory by mutableStateOf<Category?>(null)   // null = "All"
        private set

    var sortMode by mutableStateOf(SortMode.EXPIRING_SOON)
        private set

    /** Faceted filtering + sorting, recomputed on each read. */
    val visibleProducts: List<Product>
        get() = products
            .filter { selectedCategory == null || it.category == selectedCategory }
            .sortedWith(
                when (sortMode) {
                    SortMode.EXPIRING_SOON -> compareBy { it.remainingDays() }
                    SortMode.RECENT -> compareByDescending { it.dateOpened }
                }
            )

    fun setCategory(category: Category?) { selectedCategory = category }
    fun setSort(mode: SortMode) { sortMode = mode }

    fun addProduct(product: Product) {
        val nextId = (products.maxOfOrNull { it.id } ?: 0L) + 1
        products = products + product.copy(id = nextId)
    }

    fun logUse(id: Long) {
        products = products.map {
            if (it.id == id) it.copy(usesLogged = it.usesLogged + 1) else it
        }
    }

    fun toggleProjectPan(id: Long) {
        products = products.map {
            if (it.id == id) it.copy(inProjectPan = !it.inProjectPan) else it
        }
    }

    private fun sampleProducts(): List<Product> {
        val today = LocalDate.now()
        return listOf(
            Product(1, "Lash Sensational", "Maybelline", Category.EYES, 3, today.minusDays(80), 12.99, 40),
            Product(2, "Soft Matte Foundation", "NARS", Category.FACE, 12, today.minusDays(120), 49.0, 75, inProjectPan = true),
            Product(3, "Cream Lip Stain", "Sephora", Category.LIPS, 18, today.minusDays(30), 14.0, 10),
            Product(4, "Hydra Serum", "The Ordinary", Category.SKINCARE, 6, today.minusDays(150), 8.9, 90, inProjectPan = true),
            Product(5, "Glow Sunscreen SPF50", "Beauty of Joseon", Category.SKINCARE, 12, today.minusDays(20), 18.0, 25),
        )
    }
}


