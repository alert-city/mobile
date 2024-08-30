package org.cdu.codefair.alertcity.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.cdu.codefair.alertcity.LoginMutation
import org.cdu.codefair.alertcity.network.GraphQLClient

@Composable
fun MainScreen(user: LoginMutation.Login) {
    val scope = rememberCoroutineScope()
    val networkClient = remember { GraphQLClient() }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Welcome, $user!")
        LaunchedEffect(Unit) {
            println("start fetch data")
            try {
//                val fetchData = networkClient.fetchData("https://192.168.1.103:51004/graphql")
//                println(fetchData)
            } catch (e: Exception) {
                println(e.message)
            }

        }
    }
}