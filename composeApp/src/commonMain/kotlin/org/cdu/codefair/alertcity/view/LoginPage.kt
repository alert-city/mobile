package org.cdu.codefair.alertcity.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.cdu.codefair.alertcity.navigation.Routes
import org.cdu.codefair.alertcity.network.GraphQLClient
import org.cdu.codefair.alertcity.type.LoginRequestDto
import org.cdu.codefair.alertcity.viewmodel.AuthViewModel
import org.cdu.codefair.alertcity.viewmodel.AuthViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory())
) {
    val scope = rememberCoroutineScope()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isStaySignedIn by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val graphQLClient = remember { GraphQLClient() }
    val scrollState = rememberScrollState()
    var isPasswordVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(topBar = {
        TopAppBar(title = { Text("Login") }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isStaySignedIn,
                    onCheckedChange = { isStaySignedIn = it },
                )
                Text("Stay Signed In")
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "Forgot Password?",
                    style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.Underline),
                    modifier = Modifier.clickable(onClick = {
                        navController.navigate(Routes.FORGOT_PASSWORD)
                    })
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    errorMessage = null

                    scope.launch {
                        try {
                            val input = LoginRequestDto(username, password, isStaySignedIn)
                            val response = graphQLClient.login(input)
                            val user = response.data?.login
                            if (user != null) {
                                authViewModel.login(user)
                                // navigate back to MainPage
                                navController.navigate(Routes.PROFILE) {
                                    popUpTo(Routes.LOGIN) { inclusive = true }
                                }
                            } else {
                                if (response.hasErrors()) {
                                    response.errors?.let { errorMessage = it.first().message }
                                } else {
                                    response.exception?.let { errorMessage = it.message }
                                }
                                if (errorMessage.isNullOrBlank()) {
                                    errorMessage = "Invalid credentials"
                                }
                            }
                        } catch (e: Exception) {
                            errorMessage = e.message
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.Underline),
                modifier = Modifier.clickable {
                    navController.navigate(Routes.SIGNUP)
                }
            )
            errorMessage?.let {
                Text(
                    modifier = Modifier.verticalScroll(scrollState),
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}