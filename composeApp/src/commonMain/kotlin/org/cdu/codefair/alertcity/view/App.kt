package org.cdu.codefair.alertcity.view

import EventDetailsPage
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.cdu.codefair.alertcity.LoginMutation
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class Page {
    Login,
    SignUp,
    ForgotPassword,
    Main,
    EventDetails,
}

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
            AppNavHost(navController)
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    var currentPage by remember { mutableStateOf(Page.Main) }
    var loggedInUser by remember { mutableStateOf<LoginMutation.Login?>(null) }

    NavHost(navController, Page.Main.name) {
        composable(Page.Main.name) {
            MainPage(navController, user = loggedInUser)
        }
        composable(Page.Login.name) {
            LoginPage(
                navController,
                onLoginSuccess = { info ->
                    loggedInUser = info
                    currentPage = Page.Main
                },
                onForgotPassword = { currentPage = Page.ForgotPassword },
                onSignUp = { currentPage = Page.SignUp },
            )
        }
        composable(Page.SignUp.name) {
            SignUpPage(navController) {
//                    currentScreen = Screen.Login
            }
        }
        composable(Page.ForgotPassword.name) {
            ForgotPasswordPage(navController) {
//                    currentScreen = Screen.Login
            }
        }
        composable(Page.EventDetails.name + "/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            EventDetailsPage(navController, eventId)
        }
    }
}