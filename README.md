# 💄 GlossFile 

<div align="center">
  <img src="https://img.shields.io/badge/Aesthetic-Pastel%20%2F%20Beauty-pink?style=for-the-badge&logoColor=white&color=F3C6C6" alt="Aesthetic Badge" />
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin Badge" />
  <img src="https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Compose Badge" />
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge&color=FDFBF7&logoColor=1E2022" alt="License Badge" />
</div>

<p align="center">
  <strong>A native Android application built with Jetpack Compose to streamline cosmetic shelf-life tracking, routine architecture, and vanity organization.</strong> Protect your skin and curate your dream collection by turning chaotic makeup drawers into an elegant, high-glanceability digital vanity vault.
</p>

---

## 🌸 Key Visuals & Aesthetic
* **Soft Beauty Palettes:** Replaces boring spreadsheets and harsh system themes with a custom Material 3 palette featuring warm creams, soft peach accents, blush pink highlights, and dusty rose indicators.
* **Tactile Material Components:** Structured with organic, liquid-smooth rounded surfaces (`RoundedCornerShape(24.dp)`), dynamic glassmorphic card overlays, and high-contrast charcoal typography (`#1E2022`) for effortless reading.
* **Mobile-First Ergonomics:** Fully optimized for one-handed native mobile usage, placing key navigation components, custom filters, and Floating Action Buttons (FABs) safely within your thumb's natural reach.

---

## 🧸 Magical Features

### 📅 1. Smart Expiration & Freshness Tracking
Never worry about expired formulas again. GlowFile automatically manages your cosmetic shelf life with clean reactive logic:
* **Dynamic Days Remaining:** Input your "Date Opened" and standard PAO (Period After Opening, e.g., 12M), and the Kotlin backend instantly calculates your remaining days using `java.time`.
* **Empathetic Freshness Bars:** `LinearProgressIndicator` bars shift color dynamically using custom color state logic—moving from a healthy pastel peach to a warm, dusty rose warning as items approach expiration.

### 🎨 2. "Palette Lab" Custom Color Story Builder
A fully interactive canvas built to let you visualize and organize your dream eyeshadow configurations digitally right inside the native app frame:
* **Interactive Grid Layouts:** Leverages Compose `LazyVerticalGrid` configurations to let you select your palette footprint (4-pan quad or 9-pan square) and tap dynamic color swatches to map out your color theory.
* **Finish Signifiers:** Custom canvas shaders and Brush stroke indicators separate smooth matte textures from subtle, reflective multi-color gradient gleams for shimmer shades.

### 🧴 3. "Project Pan" Goal Companion
Turn using up your products into a rewarding self-care journey. Keep track of your open inventory and reduce beauty waste:
* **Hit The Pan Checklist:** Mark your high-priority products to track consecutive daily usage and watch your progress bars fill up.
* **Cost-Per-Use Analytics:** A smart math engine operating within the ViewModel that calculates the real value of your beauty investments (e.g., displaying exactly how many cents an item costs you per application based on usage logs).

### ⚡ 4. Onboarding Template Presets
Data entry shouldn't feel like a chore. GlowFile eliminates tedious typing with smart conditional forms:
* **Auto-Fill Framework:** Selecting a category like "Mascara" or "Liquid Eyeliner" from a Compose `ExposedDropdownMenuBox` instantly defaults the expiration countdown to the industry-standard 3 months.

---

## 🛠️ Tech Stack

* **Language:** Kotlin
* **UI Toolkit:** Jetpack Compose (Material 3)
* **Architecture:** MVVM (Model-View-ViewModel) with StateFlow
* **Local Database:** Room Database (For persistent offline storage of your makeup stash)
* **Dependency Injection:** Hilt / Dagger (Optional, but great for proving architecture skills)
* **Async Operations:** Kotlin Coroutines

---

## 🚀 Quick Start & Installation

To open and run this project in Android Studio, follow these steps:

### 1. Clone the repository
```bash
git clone [https://github.com/your-username/glowfile-android.git](https://github.com/your-username/glowfile-android.git)
