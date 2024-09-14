import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.cdu.codefair.alertcity.network.GraphQLClient
import org.cdu.codefair.alertcity.network.model.Event
import org.cdu.codefair.alertcity.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsPage(
    navController: NavHostController,
    eventId: String?,
    authViewModel: AuthViewModel
) {
    val client = remember { GraphQLClient() }
    var event by remember { mutableStateOf<Event?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch event details when the composable is first displayed
    LaunchedEffect(eventId) {
        if (eventId != null) {
//            val response = client.findEventById(eventId)  // Fetch event details using the provided ID
//            response?.data?.findOneUser?.let { eventData ->
//                event = Event(
//                    id = eventData.id,
//                    eventType = eventData.eventType,
//                    date = eventData.date,
//                    time = eventData.time,
//                    location = eventData.location,
//                    subject = eventData.subject,
//                    matter = eventData.matter,
//                    ERTime = eventData.ERTime,
//                    ERDate = eventData.ERDate,
//                    submitter = eventData.submitter,
//                    orgName = eventData.orgName
//                )
//            }
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            // Show a loading indicator while fetching data
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            // Display event details
            event?.let {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        text = "Subject: ${it.subject}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Type: ${it.eventType}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Date: ${it.date} ${it.time}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Location: ${it.location ?: "N/A"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Matter: ${it.matter}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Submitted by: ${it.submitter}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Organization: ${it.orgName}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } ?: Text(
                text = "Event not found",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}