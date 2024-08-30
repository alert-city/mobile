package org.cdu.codefair.alertcity.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.cdu.codefair.alertcity.LoginMutation
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class Screen {
    Login,
    SignUp,
    ForgotPassword,
    Main
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf(Screen.Login) }
        var loggedInUser by remember { mutableStateOf<LoginMutation.Login?>(null) }

        when (currentScreen) {
            Screen.Login -> {
                LoginPage(
                    onLoginSuccess = { info ->
                        loggedInUser = info
                        currentScreen = Screen.Main
                    },
                    onForgotPassword = { currentScreen = Screen.ForgotPassword },
                    onSignUp = { currentScreen = Screen.SignUp },
                )
            }

            Screen.SignUp -> {
                SignUpPage {
//                    currentScreen = Screen.Login
                }
            }

            Screen.ForgotPassword -> {
                ForgotPasswordPage {
//                    currentScreen = Screen.Login
                }
            }

            Screen.Main -> {
                loggedInUser?.let { MainScreen(user = it) }
            }
        }
    }
}