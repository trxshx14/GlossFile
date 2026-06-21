package com.cararag.glossfile.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
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

// Soft, shadowless field fill.
private val FieldBg = Color(0xFFF5EFE9)

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
        ModernField(
            label = "Email",
            value = email,
            onValueChange = { email = it; vm.clearError() },
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(14.dp))
        ModernField(
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
        PrimaryButton(text = "Sign in") { vm.login(email, password, onLoggedIn) }

        Spacer(Modifier.height(18.dp))
        SwitchPrompt(question = "New here?", action = "Create account", onClick = onGoToRegister)

        SocialBlock()
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
        ModernField(
            label = "Email",
            value = email,
            onValueChange = { email = it; vm.clearError() },
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(14.dp))
        ModernField(
            label = "Password",
            value = password,
            onValueChange = { password = it; vm.clearError() },
            keyboardType = KeyboardType.Password,
            isPassword = true,
            passwordVisible = showPassword,
            onTogglePassword = { showPassword = !showPassword }
        )
        Spacer(Modifier.height(14.dp))
        ModernField(
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
        PrimaryButton(text = "Create account") { vm.register(email, password, confirm, onRegistered) }

        Spacer(Modifier.height(18.dp))
        SwitchPrompt(question = "Already have an account?", action = "Sign in", onClick = onGoToLogin)

        SocialBlock()
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
                .padding(horizontal = 24.dp, vertical = 44.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Brand mark + wordmark
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(BlushPink.copy(alpha = 0.18f))
                    .border(BorderStroke(1.dp, DustyRose.copy(alpha = 0.65f)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("g", fontFamily = Script, fontWeight = FontWeight.SemiBold, fontSize = 30.sp, color = Ink)
            }
            Spacer(Modifier.height(6.dp))
            Text("Glossfile", fontFamily = Script, fontSize = 26.sp, color = Ink)

            Spacer(Modifier.height(24.dp))

            // The modal card
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                shadowElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(title, fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = Ink)
                    Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = InkSoft)
                    Spacer(Modifier.height(22.dp))
                    content()
                }
            }
        }
    }
}

@Composable
private fun ModernField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: () -> Unit = {},
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = if (isPassword) {
            { EyeToggle(open = passwordVisible, onClick = onTogglePassword) }
        } else null,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = FieldBg,
            unfocusedContainerColor = FieldBg,
            disabledContainerColor = FieldBg,
            errorContainerColor = FieldBg,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedLabelColor = DustyRose,
            unfocusedLabelColor = InkSoft,
            cursorColor = DustyRose,
            focusedTextColor = Ink,
            unfocusedTextColor = Ink
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

/** A small eye glyph drawn by hand — no icon dependency needed. */
@Composable
private fun EyeToggle(open: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(end = 10.dp)
            .size(28.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(22.dp)) {
            val w = size.width
            val h = size.height
            val line = Stroke(width = w * 0.08f)
            val eye = Path().apply {
                moveTo(w * 0.05f, h * 0.5f)
                quadraticBezierTo(w * 0.5f, h * 0.12f, w * 0.95f, h * 0.5f)
                quadraticBezierTo(w * 0.5f, h * 0.88f, w * 0.05f, h * 0.5f)
                close()
            }
            drawPath(eye, color = InkSoft, style = line)
            drawCircle(color = InkSoft, radius = w * 0.13f, center = Offset(w * 0.5f, h * 0.5f))
            if (!open) {
                drawLine(
                    color = InkSoft,
                    start = Offset(w * 0.12f, h * 0.88f),
                    end = Offset(w * 0.88f, h * 0.12f),
                    strokeWidth = w * 0.08f
                )
            }
        }
    }
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
        modifier = Modifier.fillMaxWidth(),
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

/* ---- Social block: "or continue with" + three soft buttons ---- */

@Composable
private fun ColumnScope.SocialBlock() {
    Spacer(Modifier.height(22.dp))
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.weight(1f).height(1.dp).background(FieldBg))
        Text(
            "  or continue with  ",
            style = MaterialTheme.typography.labelSmall,
            color = InkSoft
        )
        Box(Modifier.weight(1f).height(1.dp).background(FieldBg))
    }
    Spacer(Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally)
    ) {
        SocialButton("G")
        SocialButton("\uF8FF")   // Apple glyph; replace with a logo drawable later
        SocialButton("f")
    }
}

@Composable
private fun SocialButton(label: String) {
    Surface(
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(1.dp, FieldBg),
        modifier = Modifier
            .size(48.dp)
            .clickable { /* hook up real provider later */ }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(label, fontWeight = FontWeight.SemiBold, color = Ink, fontSize = 18.sp)
        }
    }
}