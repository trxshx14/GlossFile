package com.dink.app.ui.auth

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object AuthRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
}

@Composable
fun AuthNavGraph() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = AuthRoutes.LOGIN) {
        composable(AuthRoutes.LOGIN) {
            LoginScreen(
                onLoginClick = { _, _ -> /* TODO: authenticate */ },
                onForgotPassword = { /* TODO */ },
                onNavigateToRegister = { nav.navigate(AuthRoutes.REGISTER) },
            )
        }
        composable(AuthRoutes.REGISTER) {
            RegisterScreen(
                onCreateAccount = { _ -> /* TODO: create account */ },
                onNavigateToLogin = { nav.popBackStack() },
                onOpenTerms = { /* TODO */ },
            )
        }
    }
}
