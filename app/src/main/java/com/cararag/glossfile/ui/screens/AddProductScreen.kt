package com.cararag.glossfile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cararag.glossfile.data.Category
import com.cararag.glossfile.data.PaoPresets
import com.cararag.glossfile.data.Product
import com.cararag.glossfile.ui.VanityViewModel
import com.cararag.glossfile.ui.theme.DustyRose
import com.cararag.glossfile.ui.theme.Ink
import com.cararag.glossfile.ui.theme.InkSoft
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

private val FieldBg = Color(0xFFF5EFE9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    vm: VanityViewModel,
    onDone: () -> Unit,
    onCancel: () -> Unit,
) {
    var brand by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf<Category?>(null) }
    var paoText by remember { mutableStateOf("") }
    var dateOpened by remember { mutableStateOf(LocalDate.now()) }

    var presetExpanded by remember { mutableStateOf(false) }
    var presetLabel by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Add a product") },
                navigationIcon = { TextButton(onClick = onCancel) { Text("Cancel", color = InkSoft) } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Quick-fill preset (optional convenience)
            ExposedDropdownMenuBox(
                expanded = presetExpanded,
                onExpandedChange = { presetExpanded = !presetExpanded }
            ) {
                TextField(
                    value = presetLabel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Quick fill (optional)") },
                    placeholder = { Text("e.g. Mascara, Serum…") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(presetExpanded) },
                    shape = RoundedCornerShape(14.dp),
                    colors = fieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = presetExpanded,
                    onDismissRequest = { presetExpanded = false }
                ) {
                    PaoPresets.forEach { preset ->
                        DropdownMenuItem(
                            text = { Text("${preset.label}  ·  ${preset.paoMonths}M") },
                            onClick = {
                                presetLabel = preset.label
                                category = preset.category
                                paoText = preset.paoMonths.toString()
                                presetExpanded = false
                            }
                        )
                    }
                }
            }

            // Category — choose makeup area or skincare
            Text("Category", style = MaterialTheme.typography.labelSmall, color = InkSoft)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Category.values().toList()) { cat ->
                    FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DustyRose,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            FilledField("Brand", brand, { brand = it })
            FilledField("Product name", name, { name = it })

            // Period After Opening (months) — auto-filled by preset, fully editable
            FilledField(
                label = "Shelf life after opening (months)",
                value = paoText,
                onValueChange = { paoText = it.filter { c -> c.isDigit() } },
                keyboardType = KeyboardType.Number
            )

            // Date opened
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FieldBg, RoundedCornerShape(14.dp))
                    .clickable { showDatePicker = true }
                    .padding(16.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Date opened", color = InkSoft)
                    Text("$dateOpened", color = Ink)
                }
            }

            FilledField(
                label = "Price (optional, for cost-per-use)",
                value = price,
                onValueChange = { price = it },
                keyboardType = KeyboardType.Decimal
            )

            // Live expiry preview
            val pao = paoText.toIntOrNull()
            if (category != null && pao != null && pao > 0) {
                Text(
                    "Expires on ${dateOpened.plusMonths(pao.toLong())}",
                    style = MaterialTheme.typography.labelSmall,
                    color = DustyRose
                )
            }

            Spacer(Modifier.height(4.dp))

            val canSave = brand.isNotBlank() && name.isNotBlank() &&
                    category != null && (pao != null && pao > 0)

            Button(
                onClick = {
                    vm.addProduct(
                        Product(
                            id = 0,
                            name = name.trim(),
                            brand = brand.trim(),
                            category = category!!,
                            paoMonths = pao!!,
                            dateOpened = dateOpened,
                            price = price.toDoubleOrNull() ?: 0.0
                        )
                    )
                    onDone()
                },
                enabled = canSave,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DustyRose),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Add to vanity", color = Color.White)
            }

            Spacer(Modifier.height(20.dp))
        }

        if (showDatePicker) {
            val state = rememberDatePickerState(
                initialSelectedDateMillis = dateOpened
                    .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
            )
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        state.selectedDateMillis?.let {
                            dateOpened = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                        }
                        showDatePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                }
            ) { DatePicker(state = state) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = fieldColors(),
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun fieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = FieldBg,
    unfocusedContainerColor = FieldBg,
    disabledContainerColor = FieldBg,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    focusedLabelColor = DustyRose,
    unfocusedLabelColor = InkSoft,
    cursorColor = DustyRose,
    focusedTextColor = Ink,
    unfocusedTextColor = Ink
)