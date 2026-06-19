package com.dink.app.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.input.KeyboardType
import com.dink.app.ui.components.CourtBackground
import com.dink.app.ui.components.DinkLogo
import com.dink.app.ui.components.DinkPrimaryButton
import com.dink.app.ui.components.DinkTextField
import com.dink.app.ui.theme.DinkColors

@Composable
fun LoginScreen(
    onLoginClick: (emailOrId: String, password: String) -> Unit = { _, _ -> },
    onForgotPassword: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
) {
    // --- State ---
    var emailOrId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val canSubmit = emailOrId.isNotBlank() && password.isNotBlank()

    Box(modifier = Modifier.fillMaxSize()) {
        CourtBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 480.dp)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Paddle-and-ball emblem + "Dink" wordmark
            DinkLogo(markSize = 120, wordSize = 64)
            Spacer(Modifier.height(2.dp))
            Text(
                text = "Rank. Play. Repeat.",
                color = DinkColors.NeonLime,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(Modifier.height(48.dp))

            Text(
                text = "Welcome Back",
                color = DinkColors.TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(24.dp))

            DinkTextField(
                value = emailOrId,
                onValueChange = { emailOrId = it },
                placeholder = "Email / Player ID",
                keyboardType = KeyboardType.Email,
            )

            Spacer(Modifier.height(14.dp))

            DinkTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                isPassword = true,
                keyboardType = KeyboardType.Password,
            )

            Spacer(Modifier.height(28.dp))

            DinkPrimaryButton(
                text = "LOGIN",
                enabled = canSubmit,
                onClick = { onLoginClick(emailOrId, password) },
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Forgot Password?",
                color = DinkColors.TextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.clickable { onForgotPassword() },
            )

            Spacer(Modifier.height(40.dp))

            // "Don't have an account? REGISTER"
            val registerText = buildAnnotatedString {
                withStyle(SpanStyle(color = DinkColors.TextSecondary)) {
                    append("Don't have an account?  ")
                }
                withStyle(SpanStyle(color = DinkColors.NeonLime, fontWeight = FontWeight.Bold)) {
                    append("REGISTER")
                }
            }
            ClickableText(
                text = registerText,
                fontSize = 13,
                onClick = onNavigateToRegister,
            )
        }
    }
}

/** Small helper so a styled AnnotatedString is fully tappable. */
@Composable
private fun ClickableText(text: AnnotatedString, fontSize: Int, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = fontSize.sp,
        modifier = Modifier.clickable { onClick() },
    )
}
