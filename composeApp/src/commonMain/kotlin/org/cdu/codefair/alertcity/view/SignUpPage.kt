package org.cdu.codefair.alertcity.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.apollographql.apollo.api.Optional
import kotlinx.coroutines.launch
import org.cdu.codefair.alertcity.network.GraphQLClient
import org.cdu.codefair.alertcity.type.UserRequestDto

private enum class AccountType(val type: String) {
    Personal("Personal"), Organization("Organization")
}

@Composable
fun SignUpPage(navController: NavHostController, onSignUpSuccess: () -> Unit) {

    val scope = rememberCoroutineScope()
    val graphQLClient = remember { GraphQLClient() }

    var accountType by remember { mutableStateOf(AccountType.Personal) }
    var username by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var emailInfoType by remember { mutableStateOf(1) }
    var organizationName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Sign Up", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(24.dp))

        // Account Information Section
        Text("Account Information", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        Row {
            Text("Account Type:")
            RadioButton(
                selected = accountType == AccountType.Personal,
                onClick = { accountType = AccountType.Personal },
            )
            Text("Personal")
            Spacer(Modifier.width(8.dp))
            RadioButton(
                selected = accountType == AccountType.Organization,
                onClick = { accountType = AccountType.Organization },
            )
            Text("Organization")
        }
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text("Display Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
        )

        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
        )

        Spacer(Modifier.height(24.dp))

        // Basic Information Section
        Text("Basic Information", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        // conditional fields based on account type
        if (accountType == AccountType.Personal) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        } else if (accountType == AccountType.Organization) {
            OutlinedTextField(
                value = organizationName,
                onValueChange = { organizationName = it },
                label = { Text("Organization Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (password != confirmPassword) {
                    errorMessage = "Passwords do not match"
                    return@Button
                }
                if (username.isBlank() || displayName.isBlank() || password.isBlank() ||
                    (accountType == AccountType.Personal && (firstName.isBlank() || lastName.isBlank())) ||
                    (accountType == AccountType.Organization && organizationName.isBlank()) ||
                    phoneNumber.isBlank()
                ) {
                    errorMessage = "All fields are required"
                    return@Button
                }
                errorMessage = null
                successMessage = null

                scope.launch {
                    try {
                        val input = UserRequestDto(
                            username = username,
                            password = password,
                            confirmPassword = Optional.present(confirmPassword),
                            displayName = displayName,
                            accountType = accountType.type,
                            phoneNumber = phoneNumber,
                            role = listOf(accountType.type),
                            orgName = if (accountType == AccountType.Organization) Optional.present(
                                organizationName
                            ) else Optional.absent(),
                            firstName = if (accountType == AccountType.Personal) Optional.present(
                                firstName
                            ) else Optional.absent(),
                            lastName = if (accountType == AccountType.Personal) Optional.present(
                                lastName
                            ) else Optional.absent(),
                            // TODO: implement captcha token, but might be unnecessary
                            captchaToken = "",
                            emailInfoType = emailInfoType.toDouble()
                        )
                        val response = graphQLClient.createUser(input)
                        if (response.data?.createUser != null) {
                            successMessage = "Account created successfully!"
                            onSignUpSuccess()
                        } else {
                            if (response.hasErrors()) {
                                response.errors?.let { errorMessage = it.first().message }
                            } else {
                                response.exception?.let { errorMessage = it.message }
                            }
                            if (errorMessage.isNullOrBlank()) {
                                errorMessage = "Failed to create account"
                            }
                        }
                    } catch (e: Exception) {
                        errorMessage = e.message
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Sign Up")
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