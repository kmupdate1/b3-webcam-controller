package org.bluebikebase.wcc.ioe.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets

val client = HttpClient(CIO) {
    install(WebSockets)
    install(ContentNegotiation) {

    }
}
