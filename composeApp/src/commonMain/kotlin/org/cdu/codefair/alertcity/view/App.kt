package org.cdu.codefair.alertcity.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var isLoggedIn by remember { mutableStateOf(false) }
        var loggedInUsername by remember { mutableStateOf("") }

        if (isLoggedIn) {
            MainScreen(username = loggedInUsername)
        } else {
            LoginPage(onLoginSuccess = { username ->
                isLoggedIn = true
                loggedInUsername = username
            })
        }
    }
}