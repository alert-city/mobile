package org.cdu.codefair.alertcity.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.cdu.codefair.alertcity.network.NetworkClient

@Composable
fun MainScreen(username: String) {
    val networkClient = remember { NetworkClient() }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Welcome, $username!")
        LaunchedEffect(Unit) {
            println("start fetch data")
            try {
                // TODO: temporary url for now
                val fetchData = networkClient.fetchData("https://192.168.1.103:51004/graphql")
                println(fetchData)
            } catch (e: Exception) {
                println(e.message)
            }

        }
    }
}