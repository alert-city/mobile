package org.cdu.codefair.alertcity.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.cdu.codefair.alertcity.network.GraphQLClient
import org.cdu.codefair.alertcity.network.model.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsView(navController: NavController) {
    val client = remember { GraphQLClient() }
    val scope = rememberCoroutineScope()
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            val response = client.findAllEvents()
            response.data?.findAllEvents?.let {
                events = it.map { eventData ->
                    Event(
                        id = eventData.id,
                        eventType = eventData.eventType,
                        date = eventData.date,
                        time = eventData.time,
                        location = eventData.location,
                        subject = eventData.subject,
                        matter = eventData.matter,
                        ERTime = eventData.ERTime,
                        ERDate = eventData.ERDate,
                        submitter = eventData.submitter,
                        orgName = eventData.orgName,
                    )
                }
            }
            // TODO: for test
            events = mutableListOf(
                Event(
                    "id",
                    "eventType",
                    "date",
                    "time",
                    "location",
                    "subject",
                    "matter",
                    "ERTime",
                    "ERDate",
                    "submitter",
                    "orgName"
                )
            )
            isLoading = false
        }
    }
    Scaffold(topBar = { TopAppBar(title = { Text("Events") }) }) { innerPadding ->
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(innerPadding).padding(16.dp))
        } else {
            LazyColumn(
                // Adding padding around the list
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(events) { event ->
                    EventItem(navController, event)
                }
            }
        }
    }
}

@Composable
fun EventItem(navController: NavController, event: Event) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { navController.navigate(Page.EventDetails.name + "/${event.id}") }, // Navigate to EventDetailsPage
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                text = event.subject,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date: ${event.date}", style = MaterialTheme.typography.bodySmall)
            Text(
                text = "Location: ${event.location ?: "N/A"}",
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))  // Adding space after location
            Text(
                text = "Type: ${event.eventType}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Submitted by: ${event.submitter}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}