package com.cararag.glossfile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cararag.glossfile.ui.AuthViewModel
import com.cararag.glossfile.ui.theme.BlushPink
import com.cararag.glossfile.ui.theme.Cream
import com.cararag.glossfile.ui.theme.DustyRose
import com.cararag.glossfile.ui.theme.ExpiredRose
import com.cararag.glossfile.ui.theme.Ink
import com.cararag.glossfile.ui.theme.InkSoft
import com.cararag.glossfile.ui.theme.Peach

// Swap for FontFamily(Font(R.font.dancing_script)) once you bundle the font.
private val Script = FontFamily.Cursive

/* -------------------------------- Login --------------------------------- */

@Composable
fun LoginScreen(
    vm: AuthViewModel,
    onLoggedIn: () -> Unit,
    onGoToRegister: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    AuthScaffold(title = "Welcome back", subtitle = "Sign in to your vanity") {
        AuthField(
            label = "Email",
            value = email,
            onValueChange = { email = it; vm.clearError() },
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(14.dp))
        AuthField(
            label = "Password",
            value = password,
            onValueChange = { password = it; vm.clearError() },
            keyboardType = KeyboardType.Password,
            isPassword = true,
            passwordVisible = showPassword,
            onTogglePassword = { showPassword = !showPassword }
        )

        Text(
            "Forgot password?",
            style = MaterialTheme.typography.labelSmall,
            color = DustyRose,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp)
                .clickable { /* hook up later */ }
        )

        ErrorText(vm.error)

        Spacer(Modifier.height(20.dp))
        PrimaryButton(text = "Sign in") {
            vm.login(email, password, onLoggedIn)
        }

        Spacer(Modifier.height(20.dp))
        SwitchPrompt(
            question = "New here?",
            action = "Create account",
            onClick = onGoToRegister
        )
    }
}

/* ------------------------------- Register ------------------------------- */

@Composable
fun RegisterScreen(
    vm: AuthViewModel,
    onRegistered: () -> Unit,
    onGoToLogin: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    AuthScaffold(title = "Create account", subtitle = "Start tracking your collection") {
        AuthField(
            label = "Email",
            value = email,
            onValueChange = { email = it; vm.clearError() },
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(14.dp))
        AuthField(
            label = "Password",
            value = password,
            onValueChange = { password = it; vm.clearError() },
            keyboardType = KeyboardType.Password,
            isPassword = true,
            passwordVisible = showPassword,
            onTogglePassword = { showPassword = !showPassword }
        )
        Spacer(Modifier.height(14.dp))
        AuthField(
            label = "Confirm password",
            value = confirm,
            onValueChange = { confirm = it; vm.clearError() },
            keyboardType = KeyboardType.Password,
            isPassword = true,
            passwordVisible = showPassword,
            onTogglePassword = { showPassword = !showPassword }
        )

        ErrorText(vm.error)

        Spacer(Modifier.height(20.dp))
        PrimaryButton(text = "Create account") {
            vm.register(email, password, confirm, onRegistered)
        }

        Spacer(Modifier.height(20.dp))
        SwitchPrompt(
            question = "Already have an account?",
            action = "Sign in",
            onClick = onGoToLogin
        )
    }
}

/* ----------------------------- Shared pieces ---------------------------- */

@Composable
private fun AuthScaffold(
    title: String,
    subtitle: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Cream, Peach.copy(alpha = 0.35f))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Brand mark
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(BlushPink.copy(alpha = 0.18f))
                    .border(BorderStroke(1.dp, DustyRose.copy(alpha = 0.65f)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("g", fontFamily = Script, fontWeight = FontWeight.SemiBold, fontSize = 30.sp, color = Ink)
            }
            Spacer(Modifier.height(8.dp))
            Text("Glossfile", fontFamily = Script, fontSize = 26.sp, color = Ink)

            Spacer(Modifier.height(28.dp))

            Text(
                title,
                style = MaterialTheme.typography.headlineLarge,
                color = Ink,
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = InkSoft,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(Modifier.height(24.dp))

            content()
        }
    }
}

@Composable
private fun AuthField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: () -> Unit = {},
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = if (isPassword) {
            {
                Text(
                    text = if (passwordVisible) "Hide" else "Show",
                    style = MaterialTheme.typography.labelSmall,
                    color = DustyRose,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clickable { onTogglePassword() }
                )
            }
        } else null,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun PrimaryButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = DustyRose),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(text, color = Color.White, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun SwitchPrompt(question: String, action: String, onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(question, style = MaterialTheme.typography.bodyMedium, color = InkSoft)
        Spacer(Modifier.size(4.dp))
        Text(
            action,
            style = MaterialTheme.typography.bodyMedium,
            color = DustyRose,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { onClick() }
        )
    }
}

@Composable
private fun ErrorText(error: String?) {
    if (error != null) {
        Spacer(Modifier.height(12.dp))
        Text(
            error,
            style = MaterialTheme.typography.labelSmall,
            color = ExpiredRose,
            modifier = Modifier.fillMaxWidth()
        )
    }
}