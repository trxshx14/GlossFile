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
    var selectedPreset by remember { mutableStateOf<PaoPreset?>(null) }
    var dateOpened by remember { mutableStateOf(LocalDate.now()) }
    var presetExpanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Add product") },
                navigationIcon = {
                    TextButton(onClick = onCancel) { Text("Cancel", color = InkSoft) }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Auto-fill template: picking a type presets category + PAO for the user.
            ExposedDropdownMenuBox(
                expanded = presetExpanded,
                onExpandedChange = { presetExpanded = !presetExpanded }
            ) {
                OutlinedTextField(
                    value = selectedPreset?.label ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Product type (auto-fills PAO)") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(presetExpanded) },
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
                                selectedPreset = preset
                                presetExpanded = false
                            }
                        )
                    }
                }
            }

            // Show what the template filled in.
            selectedPreset?.let {
                Text(
                    "Category: ${it.category.label}   •   Expires ${it.paoMonths} months after opening",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            OutlinedTextField(
                value = brand, onValueChange = { brand = it },
                label = { Text("Brand") }, singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Product name") }, singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = price, onValueChange = { price = it },
                label = { Text("Price (for cost-per-use)") }, singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            // Date opened
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Date opened", color = InkSoft)
                    Text("$dateOpened", color = Ink)
                }
            }

            Spacer(Modifier.height(8.dp))

            val canSave = brand.isNotBlank() && name.isNotBlank() && selectedPreset != null
            Button(
                onClick = {
                    val preset = selectedPreset ?: return@Button
                    vm.addProduct(
                        Product(
                            id = 0,
                            name = name.trim(),
                            brand = brand.trim(),
                            category = preset.category,
                            paoMonths = preset.paoMonths,
                            dateOpened = dateOpened,
                            price = price.toDoubleOrNull() ?: 0.0
                        )
                    )
                    onDone()
                },
                enabled = canSave,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Add to vanity", color = Color.White)
            }
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
                        state.selectedDateMillis?.let { millis ->
                            dateOpened = Instant.ofEpochMilli(millis)
                                .atZone(ZoneOffset.UTC).toLocalDate()
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