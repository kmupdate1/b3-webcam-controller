package org.bluebikebase.wcc.ioe.camera

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
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
        val body = buildJsonObject {
            putJsonObject("auth") {
                put("ip", target.ipv4Address)
                put("user", target.username)
                put("pass", target.password)
            }
        }

        httpClient.post("http://kens-mac-mini:8000/control") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(body))
        }
    }

    /**
     * プロキシがウェブカメラとのセッションを閉じさせるためのコマンドAPI
     */
    override suspend fun endSession() {
        httpClient.post("http://kens-mac-mini:8000/reset")
    }

    /**
     * プロキシにウェブカメラのスペックを取得させるためのコマンドAPI
     */
    override suspend fun fetchSpecs() {
        val response = httpClient.get("http://kens-mac-mini:8000/specs").body<TapoCamSpecs>()

        horizontalLimit = response.horizontalRange.let { ScalarDRange(start = it.min, end = it.max) }
        verticalLimit = response.verticalRange.let { ScalarDRange(start = it.min, end = it.max) }
        zoomRange = response.isZoomSupported
    }

    override suspend fun move(target: Camera, horizontal: Vector, vertical: Vector) {
        val body = buildJsonObject {
            putJsonObject("move") {
                put("x", horizontal.run { magnitude * direction.multiplier }.value)
                put("y", vertical.run { magnitude * direction.multiplier }.value)
            }
        }

        httpClient.post("http://kens-mac-mini:8000/control") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(body))
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
