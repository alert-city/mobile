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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.cdu.codefair.alertcity.viewmodel.AuthViewModel
import org.cdu.codefair.alertcity.viewmodel.AuthViewModelFactory

// Sealed class representing each screen in the navigation bar
sealed class Subpage(val route: String, val title: String, val icon: ImageVector) {
    data object Events : Subpage("events", "Events", Icons.Filled.Event)
    data object Report : Subpage("report", "Report", Icons.AutoMirrored.Filled.Assignment)
    data object Map : Subpage("map", "Map", Icons.Filled.Map)
    data object Help : Subpage("help", "Help", Icons.AutoMirrored.Filled.Help)
    data object Profile : Subpage("profile", "Me", Icons.Filled.Person)
}

// List of all navigation items for the navigation bar
val navItems = mapOf(
    Subpage.Events.route to Subpage.Events,
    Subpage.Report.route to Subpage.Report,
    Subpage.Map.route to Subpage.Map,
    Subpage.Help.route to Subpage.Help,
    Subpage.Profile.route to Subpage.Profile,
)

@Composable
fun MainPage(
    navController: NavHostController,
    subpageId: String? = Subpage.Events.route,
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory())
) {
    var pageId by rememberSaveable { mutableStateOf(subpageId ?: Subpage.Events.route) }

    Scaffold(
        bottomBar = { MainBottomNavigationBar(pageId) { newId -> pageId = newId.route } }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            when (pageId) {
                Subpage.Events.route -> EventsView(navController)
                Subpage.Report.route -> ReportView()
                Subpage.Map.route -> MapView()
                Subpage.Help.route -> HelpView()
                Subpage.Profile.route -> ProfileView(navController, authViewModel)
            }
        }
    }
}

// Composable function for the bottom navigation bar
@Composable
fun MainBottomNavigationBar(subpageId: String, onItemSelected: (Subpage) -> Unit) {
    NavigationBar {
        navItems.forEach { pageItem ->
            val isSelected = subpageId == pageItem.key
            val view = pageItem.value
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = view.icon,
                        contentDescription = view.title,
                        modifier = Modifier.size(if (isSelected) 30.dp else 24.dp),
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    )
                },
                label = {
                    Text(
                        text = view.title,
                        fontSize = if (isSelected) 14.sp else 12.sp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                },
                selected = isSelected,
                onClick = { onItemSelected(view) }
            )
        }
    }
}