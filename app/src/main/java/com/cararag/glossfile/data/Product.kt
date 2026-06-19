package com.cararag.glossfile.data

import java.time.LocalDate
import java.time.temporal.ChronoUnit

enum class Category(val label: String) {
    LIPS("Lips"),
    EYES("Eyes"),
    FACE("Face"),
    SKINCARE("Skincare");
}

enum class Freshness { FRESH, SOON, EXPIRED }

/**
 * A single item on the virtual vanity.
 *
 * The whole app revolves around [dateOpened] + [paoMonths] (Period After Opening).
 * Everything else — the freshness bar, the sort order, the "use soon" label — is
 * derived from those two fields, so we never store a stale "days left" number.
 */
data class Product(
    val id: Long,
    val name: String,
    val brand: String,
    val category: Category,
    val paoMonths: Int,                 // e.g. mascara = 3, foundation = 12
    val dateOpened: LocalDate,
    val price: Double = 0.0,
    val usesLogged: Int = 0,
    val inProjectPan: Boolean = false,
) {
    val expiryDate: LocalDate
        get() = dateOpened.plusMonths(paoMonths.toLong())

    /** Full shelf life in days; coerced to >=1 so we never divide by zero. */
    val totalShelfDays: Long
        get() = ChronoUnit.DAYS.between(dateOpened, expiryDate).coerceAtLeast(1)

    fun remainingDays(today: LocalDate = LocalDate.now()): Long =
        ChronoUnit.DAYS.between(today, expiryDate)

    /** 1.0 = freshly opened, 0.0 = at/past expiry. Drives the freshness bar width. */
    fun freshnessFraction(today: LocalDate = LocalDate.now()): Float =
        (remainingDays(today).toFloat() / totalShelfDays).coerceIn(0f, 1f)

    fun freshness(today: LocalDate = LocalDate.now()): Freshness {
        val days = remainingDays(today)
        return when {
            days < 0 -> Freshness.EXPIRED
            days <= 30 -> Freshness.SOON      // "use it up" window
            else -> Freshness.FRESH
        }
    }

    /** Project Pan metric: total price divided by how many times it's been used. */
    val costPerUse: Double?
        get() = if (usesLogged > 0 && price > 0) price / usesLogged else null
}

/**
 * Auto-fill templates so users don't have to look up shelf lives for 50+ items.
 * Picking one in the Add screen presets both the category and the PAO.
 */
data class PaoPreset(val label: String, val category: Category, val paoMonths: Int)

val PaoPresets = listOf(
    PaoPreset("Mascara", Category.EYES, 3),
    PaoPreset("Liquid eyeliner", Category.EYES, 3),
    PaoPreset("Pencil eyeliner", Category.EYES, 12),
    PaoPreset("Eyeshadow (powder)", Category.EYES, 24),
    PaoPreset("Liquid foundation", Category.FACE, 12),
    PaoPreset("Concealer", Category.FACE, 12),
    PaoPreset("Powder / blush", Category.FACE, 24),
    PaoPreset("Lipstick", Category.LIPS, 18),
    PaoPreset("Lip gloss", Category.LIPS, 12),
    PaoPreset("Liquid lipstick", Category.LIPS, 12),
    PaoPreset("Moisturizer", Category.SKINCARE, 12),
    PaoPreset("Serum", Category.SKINCARE, 6),
    PaoPreset("Sunscreen", Category.SKINCARE, 12),
    PaoPreset("Cleanser", Category.SKINCARE, 12),
)