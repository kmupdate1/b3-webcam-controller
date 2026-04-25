package org.bluebikebase.wcc.ioe.camera

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.bluebikebase.core.algebra.Vector
import org.bluebikebase.core.geometry.ScalarDRange
import org.bluebikebase.wcc.domain.camera.agreement.Initializable
import org.bluebikebase.wcc.domain.camera.agreement.Motion
import org.bluebikebase.wcc.domain.camera.agreement.Zoom
import org.bluebikebase.wcc.domain.camera.entity.Camera

class LocalPythonTapoControlCommander(
    private val httpClient: HttpClient,
) : Initializable, Motion, Zoom {
    /**
     * プロキシがウェブカメラとのセッションを確立させるためのコマンドAPI
     */
    override suspend fun startSession(target: Camera) {
        httpClient.post("http://localhost:8000/control") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "auth" to mapOf(
                        "ip" to target.ipv4Address,
                        "user" to target.username,
                        "pass" to target.password,
                    )
                )
            )
        }
    }

    /**
     * プロキシがウェブカメラとのセッションを閉じさせるためのコマンドAPI
     */
    override suspend fun endSession() {
        httpClient.post("http://localhost:8000/reset")
    }

    /**
     * プロキシにウェブカメラのスペックを取得させるためのコマンドAPI
     */
    override suspend fun fetchSpecs() {
        val response = httpClient.get("http://localhost:8000/specs").body<TapoCamSpecs>()

        horizontalLimit = response.horizontalRange.let { ScalarDRange(start = it.min, end = it.max) }
        verticalLimit = response.verticalRange.let { ScalarDRange(start = it.min, end = it.max) }
        zoomRange = response.isZoomSupported
    }

    override fun move(target: Camera, horizontal: Vector, vertical: Vector) {
        motionChannel.trySend(horizontal to vertical)
        CoroutineScope(Dispatchers.IO).launch {
            httpClient.post("http://localhost:8000/control") {
                setBody(
                    mapOf(
                        "move" to mapOf(
                            "x" to horizontal.magnitude,
                            "y" to vertical.magnitude,
                        )
                    )
                )
            }
        }
    }

    override fun zoom(target: Camera, velocity: Vector) {
        TODO("Not yet implemented")
    }

    private lateinit var horizontalLimit: ScalarDRange
    private lateinit var verticalLimit: ScalarDRange
    private var zoomRange: Boolean = false

    private val motionChannel = Channel<Pair<Vector, Vector>>(Channel.CONFLATED)
    private val zoomChannel = Channel<Vector>(Channel.CONFLATED)
}
