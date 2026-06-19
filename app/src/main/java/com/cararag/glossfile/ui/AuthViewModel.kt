package com.cararag.glossfile.ui

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * Minimal auth for the UI. Accounts live in memory and reset when the app
 * restarts. A demo account is seeded so you can test login immediately:
 *     email: demo@glossfile.com   password: 123456
 *
 * To make this real later, replace the in-memory map with Firebase Auth or
 * Supabase (you already have Supabase connected).
 */
class AuthViewModel : ViewModel() {

    private val users = mutableStateMapOf("demo@glossfile.com" to "123456")

    var error by mutableStateOf<String?>(null)
        private set

    fun clearError() { error = null }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        val e = email.trim().lowercase()
        when {
            e.isBlank() || password.isBlank() -> error = "Please fill in both fields."
            !Patterns.EMAIL_ADDRESS.matcher(e).matches() -> error = "Enter a valid email address."
            users[e] == null -> error = "No account found for that email."
            users[e] != password -> error = "Incorrect password."
            else -> { error = null; onSuccess() }
        }
    }

    fun register(email: String, password: String, confirm: String, onSuccess: () -> Unit) {
        val e = email.trim().lowercase()
        when {
            e.isBlank() || password.isBlank() -> error = "Please fill in all fields."
            !Patterns.EMAIL_ADDRESS.matcher(e).matches() -> error = "Enter a valid email address."
            password.length < 6 -> error = "Password must be at least 6 characters."
            password != confirm -> error = "Passwords don't match."
            users.containsKey(e) -> error = "An account with that email already exists."
            else -> { users[e] = password; error = null; onSuccess() }
        }
    }
}