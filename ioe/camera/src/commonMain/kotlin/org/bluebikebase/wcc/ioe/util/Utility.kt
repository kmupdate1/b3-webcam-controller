package org.bluebikebase.wcc.ioe.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.serialization.json.Json

val Client = HttpClient(CIO) {
    install(WebSockets)
    install(ContentNegotiation) {
        Json { ignoreUnknownKeys = true }
    }
    install(Logging) {
        level = LogLevel.ALL
        logger = Logger.DEFAULT
    }
}
