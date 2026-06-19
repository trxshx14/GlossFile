package com.dink.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dink.app.ui.components.CourtBackground
import com.dink.app.ui.components.DinkLogo
import com.dink.app.ui.components.DinkPrimaryButton
import com.dink.app.ui.components.DinkTextField
import com.dink.app.ui.theme.DinkColors

@Composable
fun RegisterScreen(
    barangays: List<String> = DEFAULT_BARANGAYS,
    onCreateAccount: (RegistrationForm) -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onOpenTerms: () -> Unit = {},
) {
    // --- State ---
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedBarangay by remember { mutableStateOf<String?>(null) }
    var acceptedTerms by remember { mutableStateOf(false) }

    val passwordsMatch = password.isNotEmpty() && password == confirmPassword
    val canSubmit = fullName.isNotBlank() &&
            email.isNotBlank() &&
            passwordsMatch &&
            selectedBarangay != null &&
            acceptedTerms

    Box(modifier = Modifier.fillMaxSize()) {
        CourtBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 480.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DinkLogo(markSize = 64, wordSize = 36)

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Join the Court",
                color = DinkColors.TextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(10.dp))

            BarangayRankedBadge()

            Spacer(Modifier.height(24.dp))

            DinkTextField(fullName, { fullName = it }, "Full Name")
            Spacer(Modifier.height(12.dp))

            DinkTextField(email, { email = it }, "Email Address", keyboardType = KeyboardType.Email)
            Spacer(Modifier.height(12.dp))

            DinkTextField(password, { password = it }, "Password", isPassword = true, keyboardType = KeyboardType.Password)
            Spacer(Modifier.height(12.dp))

            DinkTextField(
                confirmPassword,
                { confirmPassword = it },
                "Confirm Password",
                isPassword = true,
                keyboardType = KeyboardType.Password,
            )
            if (confirmPassword.isNotEmpty() && !passwordsMatch) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Passwords don't match",
                    color = DinkColors.NeonLime,
                    fontSize = 11.sp,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(Modifier.height(12.dp))

            BarangayDropdown(
                options = barangays,
                selected = selectedBarangay,
                onSelected = { selectedBarangay = it },
            )

            Spacer(Modifier.height(16.dp))

            TermsCheckbox(
                checked = acceptedTerms,
                onCheckedChange = { acceptedTerms = it },
                onOpenTerms = onOpenTerms,
            )

            Spacer(Modifier.height(24.dp))

            DinkPrimaryButton(
                text = "CREATE ACCOUNT",
                enabled = canSubmit,
                onClick = {
                    onCreateAccount(
                        RegistrationForm(
                            fullName = fullName,
                            email = email,
                            password = password,
                            barangay = selectedBarangay.orEmpty(),
                        )
                    )
                },
            )

            Spacer(Modifier.height(20.dp))

            val loginText = buildAnnotatedString {
                withStyle(SpanStyle(color = DinkColors.TextSecondary)) {
                    append("Already a member?  ")
                }
                withStyle(SpanStyle(color = DinkColors.NeonLime, fontWeight = FontWeight.Bold)) {
                    append("LOGIN")
                }
            }
            Text(
                text = loginText,
                fontSize = 13.sp,
                modifier = Modifier.clickable { onNavigateToLogin() },
            )
        }
    }
}

/** "Barangay Ranked" badge-style element. */
@Composable
private fun BarangayRankedBadge() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = DinkColors.NeonLime.copy(alpha = 0.12f),
                shape = RoundedCornerShape(50),
            )
            .border(
                width = 1.dp,
                color = DinkColors.NeonLime.copy(alpha = 0.5f),
                shape = RoundedCornerShape(50),
            )
            .padding(horizontal = 12.dp, vertical = 5.dp),
    ) {
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = null,
            tint = DinkColors.NeonLime,
            modifier = Modifier.height(14.dp),
        )
        Text(
            text = "BARANGAY RANKED",
            color = DinkColors.NeonLime,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

/** Custom "Select Your Barangay" dropdown spinner with a location icon. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BarangayDropdown(
    options: List<String>,
    selected: String?,
    onSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = selected ?: "",
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Select Your Barangay", color = DinkColors.TextHint, fontSize = 15.sp) },
            leadingIcon = {
                Icon(Icons.Filled.LocationOn, contentDescription = null, tint = DinkColors.NeonLime)
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DinkColors.InputContainer,
                unfocusedContainerColor = DinkColors.InputContainer,
                focusedBorderColor = DinkColors.InputBorderFocused,
                unfocusedBorderColor = DinkColors.InputBorderIdle,
                focusedTrailingIconColor = DinkColors.NeonLime,
                unfocusedTrailingIconColor = DinkColors.TextSecondary,
                focusedTextColor = DinkColors.TextPrimary,
                unfocusedTextColor = DinkColors.TextPrimary,
            ),
            modifier = Modifier
                .menuAnchor()      // required so the menu anchors to the field
                .fillMaxWidth(),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = DinkColors.TextPrimary) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

/** Terms of Service checkbox with a tappable "Terms of Service & Privacy Policy" link. */
@Composable
private fun TermsCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onOpenTerms: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = DinkColors.NeonLime,
                uncheckedColor = DinkColors.TextSecondary,
                checkmarkColor = DinkColors.OnAccent,
            ),
        )
        val terms = buildAnnotatedString {
            withStyle(SpanStyle(color = DinkColors.TextSecondary)) {
                append("I agree to the ")
            }
            withStyle(SpanStyle(color = DinkColors.NeonLime, fontWeight = FontWeight.Medium)) {
                append("Terms of Service & Privacy Policy")
            }
        }
        Text(
            text = terms,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            modifier = Modifier
                .padding(start = 4.dp)
                .clickable { onOpenTerms() },
        )
    }
}

/** Simple data holder for the submitted form. */
data class RegistrationForm(
    val fullName: String,
    val email: String,
    val password: String,
    val barangay: String,
)

/** Sample list — replace with data from your backend / API. */
val DEFAULT_BARANGAYS = listOf(
    "Arevalo", "Jaro", "La Paz", "Mandurriao", "Molo", "City Proper", "Lapuz",
)
