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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.apollographql.apollo.api.Optional
import kotlinx.coroutines.launch
import org.cdu.codefair.alertcity.navigation.Routes
import org.cdu.codefair.alertcity.network.GraphQLClient
import org.cdu.codefair.alertcity.type.UserRequestDto
import org.cdu.codefair.alertcity.viewmodel.AuthViewModel
import org.cdu.codefair.alertcity.viewmodel.AuthViewModelFactory

private enum class AccountType(val type: String) {
    Personal("Personal"), Organization("Organization")
}

private enum class Role(val type: String) {
    Admin("admin"), Staff("staff"), Normal("normal")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpPage(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory())
) {

    val scope = rememberCoroutineScope()
    val graphQLClient = remember { GraphQLClient() }

    var accountType by remember { mutableStateOf(AccountType.Personal) }
    var username by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var organizationName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    var errorMessage by remember { mutableStateOf<String?>(null) }
    Scaffold(topBar = {
        TopAppBar(title = { Text("Sign Up") }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Account Information Section
            Text("Account Information", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))

            // Toggle between personal and organization account types
            Text("Account Type:", modifier = Modifier.fillMaxWidth())
            Row {
                Row(
                    Modifier
                        .weight(1f)
                        .clickable { accountType = AccountType.Personal },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = accountType == AccountType.Personal,
                        onClick = { accountType = AccountType.Personal },
                    )
                    Text(
                        text = "Personal",
                        color = if (accountType == AccountType.Personal) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
                Row(
                    Modifier
                        .weight(1f)
                        .clickable { accountType = AccountType.Organization },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = accountType == AccountType.Organization,
                        onClick = { accountType = AccountType.Organization },
                    )
                    Text(
                        text = "Organization",
                        color = if (accountType == AccountType.Organization) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
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

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                        Icon(
                            imageVector = if (isConfirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (isConfirmPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
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
                    keyboardController?.hide()
                    errorMessage = null
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

                    scope.launch {
                        try {
                            val input = UserRequestDto(
                                username = username,
                                password = password,
                                confirmPassword = Optional.present(confirmPassword),
                                displayName = displayName,
                                accountType = accountType.type,
                                phoneNumber = phoneNumber,
                                role = listOf(if (accountType == AccountType.Personal) Role.Normal.type else Role.Admin.type),
                                orgName = if (accountType == AccountType.Organization) Optional.present(
                                    organizationName
                                ) else Optional.absent(),
                                firstName = if (accountType == AccountType.Personal) Optional.present(
                                    firstName
                                ) else Optional.absent(),
                                lastName = if (accountType == AccountType.Personal) Optional.present(
                                    lastName
                                ) else Optional.absent(),
                                staffs = Optional.presentIfNotNull(
                                    if (accountType == AccountType.Organization) listOf(username)
                                    else null
                                )
                            )
                            val response = graphQLClient.createUser(input)
                            val user = response.data?.createUser
                            if (user != null) {
                                authViewModel.login(user)
                                navController.navigate(Routes.PROFILE) {
                                    popUpTo(Routes.SIGNUP) { inclusive = true }
                                }
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
                LaunchedEffect(it) {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            }
        }
    }
}