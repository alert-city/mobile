package org.cdu.codefair.alertcity.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.cdu.codefair.alertcity.navigation.Routes
import org.cdu.codefair.alertcity.network.GraphQLClient
import org.cdu.codefair.alertcity.type.ResetPasswordRequestDto
import org.cdu.codefair.alertcity.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordPage(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val graphQLClient = remember { GraphQLClient() }
    val scrollState = rememberScrollState()

    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    var countdown by remember { mutableStateOf(0) }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isRequestingCode by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    var step by remember { mutableStateOf(1) } // 1 = Request Code, 2 = Reset Password

    LaunchedEffect(countdown) {
        while (countdown > 0) {
            delay(1000L)
            countdown--
        }
    }
    Scaffold(topBar = {
        TopAppBar(title = { Text("Forgot Password") }, navigationIcon = {
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
            Text(
                text = if (step == 1) "Forgot Password" else "Reset Password",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(24.dp))
            if (step == 1) {
                // Request Verification Code UI
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        keyboardController?.hide()
                        errorMessage = null
                        isRequestingCode = true
                        countdown = 60 // Reset countdown to 60 seconds

                        scope.launch {
                            try {
                                val response = graphQLClient.sendVerificationCodeEmail(email, 1)

                                if (response.data?.sendVerificationCodeEmail == true) {
//                                    startCountdownTimer { countdown-- }
                                    // reset countdown
                                    countdown = 60
                                    step = 2 // Move to reset password step
                                } else {
                                    if (response.hasErrors()) {
                                        response.errors?.let { errorMessage = it.first().message }
                                    } else {
                                        response.exception?.let { errorMessage = it.message }
                                    }
                                    if (errorMessage.isNullOrBlank()) {
                                        errorMessage = "Failed to send validation code"
                                    }
                                }
                            } catch (e: Exception) {
                                errorMessage = e.message
                            } finally {
                                isRequestingCode = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isRequestingCode && countdown == 0
                ) {
                    Text(if (countdown > 0) "Resend in $countdown seconds" else "Send Verification Code")
                }
            } else if (step == 2) {
                // Reset Password UI
                OutlinedTextField(
                    value = verificationCode,
                    onValueChange = { verificationCode = it },
                    label = { Text("Verification Code") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    singleLine = true,
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    singleLine = true,
                    visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = {
                            isConfirmPasswordVisible = !isConfirmPasswordVisible
                        }) {
                            Icon(
                                imageVector = if (isConfirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (isConfirmPasswordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        keyboardController?.hide()
                        errorMessage = null

                        scope.launch {
                            try {
                                val input = ResetPasswordRequestDto(
                                    verificationCode = verificationCode,
                                    password = newPassword,
                                    confirmPassword = confirmPassword
                                )
                                val response = graphQLClient.resetPassword(email, input)

                                if (response.data?.resetPassword == true) {
                                    navController.navigate(Routes.LOGIN) {
                                        popUpTo(Routes.FORGOT_PASSWORD) {
                                            inclusive = true
                                        }
                                    }
                                } else {
                                    if (response.hasErrors()) {
                                        response.errors?.let { errorMessage = it.first().message }
                                    } else {
                                        response.exception?.let { errorMessage = it.message }
                                    }
                                    if (errorMessage.isNullOrBlank()) {
                                        errorMessage = "Failed to reset password"
                                    }
                                }
                            } catch (e: Exception) {
                                errorMessage = e.message
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset Password")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Back to Login",
                style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.Underline),
                modifier = Modifier.clickable {
                    navController.navigate(Routes.LOGIN)
                }
            )

            errorMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.verticalScroll(scrollState),
                )
            }
        }
    }
}

