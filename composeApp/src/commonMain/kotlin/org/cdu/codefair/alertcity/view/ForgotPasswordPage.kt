package org.cdu.codefair.alertcity.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.cdu.codefair.alertcity.network.GraphQLClient
import org.cdu.codefair.alertcity.type.ResetPasswordRequestDto

@Composable
fun ForgotPasswordPage(navController: NavHostController, onPasswordReset: () -> Unit) {
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var validationCode by remember { mutableStateOf("") }
    var codeSent by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    val graphQLClient = remember { GraphQLClient() }
    // Countdown state
    var countdownTime by remember { mutableStateOf(60) } // 60 seconds countdown
    var isCountingDown by remember { mutableStateOf(false) }

    LaunchedEffect(isCountingDown) {
        if (isCountingDown) {
            while (countdownTime > 0) {
                delay(1000L)
                countdownTime--
            }
            isCountingDown = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Forgot Password", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        // TODO: update verification code and password
                        val input = ResetPasswordRequestDto(
                            verificationCode = validationCode, password = "", confirmPassword = ""
                        )
                        val response = graphQLClient.resetPassword(email, input)
                        if (response.data?.resetPassword == true) {
                            successMessage = "Validation code sent to your email"
                            codeSent = true
                            // reset countdown
                            countdownTime = 60
                            isCountingDown = true
                        } else {
                            // TODO: graphql error or response exception
                            errorMessage = "Failed to send validation code"
                        }
                    } catch (e: Exception) {
                        errorMessage = e.message
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isCountingDown,
        ) {
            Text(
                if (isCountingDown) {
                    "Resend in $countdownTime seconds"
                } else {
                    "Get Validation Code"
                }
            )
        }

        // display validation code input only if code is sent
        if (codeSent) {
            Spacer(Modifier.height(16.dp))

            // input field
            OutlinedTextField(
                value = validationCode,
                onValueChange = { validationCode = it },
                label = { Text("Validation Code") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(16.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // TODO: handle verification here

                    onPasswordReset() // TODO: navigate back to login
                }) {
                Text("Submit")
            }
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        successMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = MaterialTheme.colorScheme.primary)
        }
    }
}