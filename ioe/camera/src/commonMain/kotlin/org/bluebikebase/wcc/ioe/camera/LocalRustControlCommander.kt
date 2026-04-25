package org.bluebikebase.wcc.ioe.camera

import io.ktor.client.HttpClient
import kotlinx.coroutines.channels.Channel
import org.bluebikebase.core.algebra.Vector
import org.bluebikebase.core.geometry.ScalarDRange
import org.bluebikebase.wcc.domain.camera.agreement.Initializable
import org.bluebikebase.wcc.domain.camera.agreement.Motion
import org.bluebikebase.wcc.domain.camera.agreement.Zoom
import org.bluebikebase.wcc.domain.camera.entity.Camera

class LocalRustControlCommander(
    private val httpClient: HttpClient,
) : Initializable, Motion, Zoom {
    override suspend fun startSession(target: Camera) {
        TODO("Not yet implemented")
    }

    override suspend fun endSession() {
        TODO("Not yet implemented")
    }

    override suspend fun fetchSpecs() {
        TODO("Not yet implemented")
    }

    override fun move(target: Camera, horizontal: Vector, vertical: Vector) {
        motionChannel.trySend(horizontal to vertical)
    }

    override fun zoom(target: Camera, velocity: Vector) {
        TODO("Not yet implemented")
    }

    private val horizontalLimit by lazy<ScalarDRange> { TODO() }
    private val verticalLimit by lazy<ScalarDRange> { TODO() }
    private val zoomRange by lazy<ScalarDRange> { TODO() }

    private val motionChannel = Channel<Pair<Vector, Vector>>(Channel.CONFLATED)
    private val zoomChannel = Channel<Vector>(Channel.CONFLATED)
}
