package org.bluebikebase.wcc.ioe.util

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import kotlinx.serialization.json.Json

val Client = HttpClient(CIO) {
    install(WebSockets)
    install(ContentNegotiation) {
        Json { ignoreUnknownKeys = true }
    }
    install(Logging) {
        level = LogLevel.ALL
        logger = Logger.SIMPLE
    }
}
