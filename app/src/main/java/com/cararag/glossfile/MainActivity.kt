package com.cararag.glossfile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cararag.glossfile.ui.AuthViewModel
import com.cararag.glossfile.ui.VanityViewModel
import com.cararag.glossfile.ui.screens.AddProductScreen
import com.cararag.glossfile.ui.screens.LoginScreen
import com.cararag.glossfile.ui.screens.ProjectPanScreen
import com.cararag.glossfile.ui.screens.RegisterScreen
import com.cararag.glossfile.ui.screens.SplashScreen
import com.cararag.glossfile.ui.screens.VanityScreen
import com.cararag.glossfile.ui.theme.GlossFileTheme

// Top-level app flow.
enum class Stage { SPLASH, LOGIN, REGISTER, MAIN }

// Tabs/screens inside the signed-in app.
enum class AppScreen { VANITY, PROJECT_PAN, ADD }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GlossFileTheme {
                var stage by remember { mutableStateOf(Stage.SPLASH) }
                val authVm: AuthViewModel = viewModel()

                when (stage) {
                    Stage.SPLASH -> SplashScreen(onTimeout = { stage = Stage.LOGIN })

                    Stage.LOGIN -> LoginScreen(
                        vm = authVm,
                        onLoggedIn = { stage = Stage.MAIN },
                        onGoToRegister = { authVm.clearError(); stage = Stage.REGISTER }
                    )

                    Stage.REGISTER -> RegisterScreen(
                        vm = authVm,
                        onRegistered = { stage = Stage.MAIN },
                        onGoToLogin = { authVm.clearError(); stage = Stage.LOGIN }
                    )

                    Stage.MAIN -> MainApp()
                }
            }
        }
    }
}

@Composable
private fun MainApp() {
    val vm: VanityViewModel = viewModel()
    var screen by remember { mutableStateOf(AppScreen.VANITY) }

    if (screen == AppScreen.ADD) {
        AddProductScreen(
            vm = vm,
            onDone = { screen = AppScreen.VANITY },
            onCancel = { screen = AppScreen.VANITY }
        )
    } else {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    NavigationBarItem(
                        selected = screen == AppScreen.VANITY,
                        onClick = { screen = AppScreen.VANITY },
                        icon = { Text("\uD83E\uDDF4") },
                        label = { Text("Vanity") }
                    )
                    NavigationBarItem(
                        selected = screen == AppScreen.PROJECT_PAN,
                        onClick = { screen = AppScreen.PROJECT_PAN },
                        icon = { Text("\uD83C\uDFAF") },
                        label = { Text("Project Pan") }
                    )
                }
            },
            floatingActionButton = {
                if (screen == AppScreen.VANITY) {
                    FloatingActionButton(
                        onClick = { screen = AppScreen.ADD },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ) { Text("+", fontSize = 26.sp) }
                }
            }
        ) { padding ->
            Box(Modifier.padding(padding)) {
                when (screen) {
                    AppScreen.PROJECT_PAN -> ProjectPanScreen(vm)
                    else -> VanityScreen(vm)
                }
            }
        }
    }
}