package org.cdu.codefair.alertcity.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.cdu.codefair.alertcity.LoginMutation
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class Page {
    Login,
    SignUp,
    ForgotPassword,
    Main
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        var currentPage by remember { mutableStateOf(Page.Main) }
        var loggedInUser by remember { mutableStateOf<LoginMutation.Login?>(null) }

        when (currentPage) {
            Page.Login -> {
                LoginPage(
                    onLoginSuccess = { info ->
                        loggedInUser = info
                        currentPage = Page.Main
                    },
                    onForgotPassword = { currentPage = Page.ForgotPassword },
                    onSignUp = { currentPage = Page.SignUp },
                )
            }

            Page.SignUp -> {
                SignUpPage {
//                    currentScreen = Screen.Login
                }
            }

            Page.ForgotPassword -> {
                ForgotPasswordPage {
//                    currentScreen = Screen.Login
                }
            }

            Page.Main -> {
                MainPage(user = loggedInUser)
            }
        }
    }
}