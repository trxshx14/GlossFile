package com.dink.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.vector.ImageVector
import com.dink.app.ui.theme.DinkColors

/**
 * Dark transparent gray input field with 8dp rounded corners, left-aligned text,
 * a subtle gray border that turns neon-lime when focused.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DinkTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        textStyle = TextStyle(fontSize = 15.sp, color = DinkColors.TextPrimary),
        placeholder = { Text(placeholder, color = DinkColors.TextHint, fontSize = 15.sp) },
        leadingIcon = leadingIcon?.let {
            { Icon(it, contentDescription = null, tint = DinkColors.NeonLime) }
        },
        shape = RoundedCornerShape(8.dp),
        visualTransformation =
            if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = DinkColors.InputContainer,
            unfocusedContainerColor = DinkColors.InputContainer,
            focusedBorderColor = DinkColors.InputBorderFocused,
            unfocusedBorderColor = DinkColors.InputBorderIdle,
            cursorColor = DinkColors.NeonLime,
            focusedTextColor = DinkColors.TextPrimary,
            unfocusedTextColor = DinkColors.TextPrimary,
        ),
    )
}

/** Neon primary button (LOGIN / CREATE ACCOUNT). */
@Composable
fun DinkPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = DinkColors.NeonLime,
            contentColor = DinkColors.OnAccent,
            disabledContainerColor = DinkColors.NeonLime.copy(alpha = 0.35f),
            disabledContentColor = DinkColors.OnAccent.copy(alpha = 0.6f),
        ),
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
    }
}
