package org.bluebikebase.wcc.ioe.camera

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import org.bluebikebase.core.algebra.Vector
import org.bluebikebase.core.geometry.ScalarDRange
import org.bluebikebase.wcc.domain.camera.agreement.Motion
import org.bluebikebase.wcc.domain.camera.agreement.Zoom
import org.bluebikebase.wcc.domain.camera.entity.Camera
import org.bluebikebase.wcc.ioe.util.client

class DirectWsMotionClient(
    private val camera: Camera,
    private val httpClient: HttpClient,
) : Motion, Zoom {
    suspend fun connect() {
        val range = client.get("http://${camera.ipv4Address}/config").body<ScalarDRange>()

        client.webSocket(host = camera.ipv4Address, path = "/control") {

        }
    }

    override val horizontalLimit: ScalarDRange
        get() = TODO("Not yet implemented")
    override val verticalLimit: ScalarDRange
        get() = TODO("Not yet implemented")

    override fun move(
        horizontal: Vector,
        vertical: Vector
    ) {
        TODO("Not yet implemented")
    }

    override val zoomRange: ScalarDRange
        get() = TODO("Not yet implemented")

    override fun zoom(velocity: Vector) {
        TODO("Not yet implemented")
    }
}
