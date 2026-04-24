package org.bluebikebase.wcc.ioe.camera

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import kotlinx.coroutines.channels.Channel
import org.bluebikebase.core.algebra.Vector
import org.bluebikebase.core.geometry.ScalarDRange
import org.bluebikebase.wcc.domain.camera.agreement.Motion
import org.bluebikebase.wcc.domain.camera.agreement.Zoom
import org.bluebikebase.wcc.domain.camera.entity.Camera
import org.bluebikebase.wcc.ioe.util.client

class DirectWsMotionClient(
    private val httpClient: HttpClient,
) : Motion, Zoom {
    suspend fun connect(target: Camera) {
        val range = client.get("http://${target.ipv4Address}/config").body<ScalarDRange>()

        client.webSocket(host = target.ipv4Address, path = "/control") {

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

    private val motionChannel = Channel<Pair<Vector, Vector>>(Channel.CONFLATED)
    private val zoomChannel = Channel<Vector>(Channel.CONFLATED)
}
