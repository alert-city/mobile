package org.cdu.codefair.alertcity.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import org.cdu.codefair.alertcity.navigation.Routes
import org.cdu.codefair.alertcity.viewmodel.AuthViewModel

@Composable
fun ProfileView(navController: NavHostController, authViewModel: AuthViewModel) {
    // Observe the login state
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    if (isLoggedIn) {
        // Display Profile content if the user is logged in
        ProfileContent(navController, authViewModel)
    } else {
        // Display a "Login" button if the user is not logged in
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "You are not logged in.", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate(Routes.LOGIN)
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log In")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(navController: NavHostController, authViewModel: AuthViewModel) {
    val username by authViewModel.username.collectAsState()
    val displayName by authViewModel.displayName.collectAsState()
    val firstName by authViewModel.firstName.collectAsState()
    val lastName by authViewModel.lastName.collectAsState()
    val organization by authViewModel.organization.collectAsState()
    val avatarUrl by authViewModel.avatarUrl.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Profile") }) }) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = avatarUrl,
                error = rememberVectorPainter(Icons.Filled.Person),
                placeholder = rememberVectorPainter(Icons.Filled.Person),
                contentDescription = "Avatar",
                modifier = Modifier.clip(CircleShape)
                    .size(100.dp)
                    .padding(bottom = 16.dp),
            )

            Text(text = "Welcome, $displayName!", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Username: $username", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (firstName.isNotBlank() or lastName.isNotBlank()) {
                Text(
                    text = "Welcome, $firstName $lastName!",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (organization.isNotBlank()) {
                Text(
                    text = "Organization: $organization",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { authViewModel.logout() }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log Out")
            }
        }
    }
}