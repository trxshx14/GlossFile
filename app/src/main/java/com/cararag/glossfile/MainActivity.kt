package com.cararag.glossfile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cararag.glossfile.ui.VanityViewModel
import com.cararag.glossfile.ui.screens.AddProductScreen
import com.cararag.glossfile.ui.screens.ProjectPanScreen
import com.cararag.glossfile.ui.screens.VanityScreen
import com.cararag.glossfile.ui.theme.GlossFileTheme


enum class AppScreen { VANITY, PROJECT_PAN, ADD }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GlossFileTheme {
                val vm: VanityViewModel = viewModel()
                var screen by remember { mutableStateOf(AppScreen.VANITY) }

                // The Add screen is a full-screen mode; everything else lives under the bottom bar.
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
                                    icon = { Text("🧴") },
                                    label = { Text("Vanity") }
                                )
                                NavigationBarItem(
                                    selected = screen == AppScreen.PROJECT_PAN,
                                    onClick = { screen = AppScreen.PROJECT_PAN },
                                    icon = { Text("🎯") },
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
        }
    }
}