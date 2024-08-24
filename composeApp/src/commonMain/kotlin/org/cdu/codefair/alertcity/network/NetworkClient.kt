package org.cdu.codefair.alertcity.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.cdu.codefair.alertcity.isDebug
import org.cdu.codefair.alertcity.network.entity.MyResponse

class NetworkClient {
    // define a shared HttpClient with platform-specific engines
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            level = if (isDebug) LogLevel.BODY else LogLevel.NONE
        }
    }

    suspend fun fetchData(url: String): MyResponse? {
        return try {
            httpClient.get {
                url(url)
                contentType(ContentType.Application.Json)
            }.body()
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }
}
