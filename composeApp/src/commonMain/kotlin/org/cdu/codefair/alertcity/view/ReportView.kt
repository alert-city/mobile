package org.cdu.codefair.alertcity.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.apollographql.apollo.api.Optional
import kotlinx.coroutines.launch
import org.cdu.codefair.alertcity.network.GraphQLClient
import org.cdu.codefair.alertcity.type.CreateEventInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportView() {
    val client = remember { GraphQLClient() }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // State variables for the form fields
    var eventType by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var matter by remember { mutableStateOf("") }
    var ERTime by remember { mutableStateOf("") }
    var ERDate by remember { mutableStateOf("") }
    var submitter by remember { mutableStateOf("") }
    var orgName by remember { mutableStateOf("") }

    // State for loading and result feedback
    var isSubmitting by remember { mutableStateOf(false) }
    var submissionResult by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Report Event") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Form fields for event details
            OutlinedTextField(
                value = eventType,
                onValueChange = { eventType = it },
                label = { Text("Event Type") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Time (HH:MM)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Subject") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = matter,
                onValueChange = { matter = it },
                label = { Text("Matter") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = ERTime,
                onValueChange = { ERTime = it },
                label = { Text("Emergency Response Time (HH:MM)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = ERDate,
                onValueChange = { ERDate = it },
                label = { Text("Emergency Response Date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = submitter,
                onValueChange = { submitter = it },
                label = { Text("Submitter") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = orgName,
                onValueChange = { orgName = it },
                label = { Text("Organization Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Submit button
            Button(
                onClick = {
                    isSubmitting = true
                    submissionResult = null
                    coroutineScope.launch {
                        val success = submitEvent(
                            eventType, date, time, location, subject, matter,
                            ERTime, ERDate, submitter, orgName, client
                        )
                        isSubmitting = false
                        submissionResult =
                            if (success) "Event reported successfully!" else "Failed to report the event."
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSubmitting
            ) {
                Text(if (isSubmitting) "Submitting..." else "Submit")
            }

            // Display submission result
            submissionResult?.let {
                Text(
                    it,
                    color = if (it.contains("successfully")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )

                LaunchedEffect(it) {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            }
        }
    }
}

// Function to handle event submission using createEvent GraphQL mutation
suspend fun submitEvent(
    eventType: String,
    date: String,
    time: String,
    location: String,
    subject: String,
    matter: String,
    ERTime: String,
    ERDate: String,
    submitter: String,
    orgName: String,
    client: GraphQLClient
): Boolean {
    return try {
        val input = CreateEventInput(
            eventType = eventType,
            date = date,
            time = time,
            location = Optional.present(location),
            subject = subject,
            matter = matter,
            ERTime = Optional.present(ERTime),
            ERDate = Optional.present(ERDate),
            submitter = submitter,
            orgName = Optional.present(orgName)
        )
        val response = client.createEvent(input)
        response.data?.createEvent != null
    } catch (e: Exception) {
        println("Error reporting event: ${e.message}")
        false
    }
}