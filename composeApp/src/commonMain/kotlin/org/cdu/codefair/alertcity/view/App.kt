package org.cdu.codefair.alertcity.view

import EventDetailsPage
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.cdu.codefair.alertcity.navigation.Routes
import org.cdu.codefair.alertcity.viewmodel.AuthViewModel
import org.cdu.codefair.alertcity.viewmodel.AuthViewModelFactory
import org.jetbrains.compose.ui.tooling.preview.Preview


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
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory())
) {
    NavHost(navController, Routes.MAIN) {
        composable(Routes.MAIN) {
            MainPage(navController, authViewModel = authViewModel)
        }
        composable("${Routes.MAIN}/{pageId}") { backStackEntry ->
            val pageId = backStackEntry.arguments?.getString("pageId")
            MainPage(navController, pageId, authViewModel = authViewModel)
        }
        composable(Routes.LOGIN) {
            LoginPage(navController, authViewModel)
        }
        composable(Routes.SIGNUP) {
            SignUpPage(navController, authViewModel)
        }
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordPage(navController, authViewModel)
        }
        composable("${Routes.EVENT_DETAILS}/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            EventDetailsPage(navController, eventId, authViewModel)
        }
    }
}