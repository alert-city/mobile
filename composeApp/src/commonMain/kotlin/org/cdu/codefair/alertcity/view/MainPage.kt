package org.cdu.codefair.alertcity.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.cdu.codefair.alertcity.LoginMutation

// Sealed class representing each screen in the navigation bar
sealed class Subpage(val route: String, val title: String, val icon: ImageVector) {
    data object Events : Subpage("events", "Events", Icons.Filled.Event)
    data object Report : Subpage("report", "Report", Icons.AutoMirrored.Filled.Assignment)
    data object Map : Subpage("map", "Map", Icons.Filled.Map)
    data object Help : Subpage("help", "Help", Icons.AutoMirrored.Filled.Help)
    data object Profile : Subpage("profile", "Me", Icons.Filled.Person)
}

// List of all navigation items for the navigation bar
val navItems = listOf(
    Subpage.Events,
    Subpage.Report,
    Subpage.Map,
    Subpage.Help,
    Subpage.Profile,
)

@Composable
fun MainPage(user: LoginMutation.Login?) {
    // state to keep track of the current page view
    var currentView by remember { mutableStateOf<Subpage>(Subpage.Events) }

    // Scaffold provides a basic layout structure with a bottom bar
    Scaffold(bottomBar = {
        NavigationBar {
            navItems.forEach { view ->
                val isSelected = currentView == view
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = view.icon,
                            contentDescription = view.title,
                            modifier = Modifier.size(if (isSelected) 30.dp else 24.dp),
                            tint = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    label = {
                        Text(
                            text = view.title,
                            fontSize = if (isSelected) 14.sp else 12.sp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    selected = currentView == view,
                    onClick = { currentView = view })
            }
        }
    }) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            when (currentView) {
                Subpage.Events -> EventsView()
                Subpage.Report -> ReportView()
                Subpage.Map -> MapView()
                Subpage.Help -> HelpView()
                Subpage.Profile -> ProfileView()
            }
        }
    }
}