package org.bluebikebase.wcc.ioe.camera

import kotlinx.coroutines.test.runTest
import org.bluebikebase.core.algebra.Vector
import org.bluebikebase.core.foundation.ScalarD
import org.bluebikebase.wcc.domain.camera.entity.Camera
import org.bluebikebase.wcc.domain.camera.entity.CameraId
import org.bluebikebase.wcc.domain.camera.entity.CameraName
import org.bluebikebase.wcc.ioe.util.Client
import kotlin.test.Test

class TestLocalPythonTapoControlCommander {
    @Test
    fun `LocalPythonTapoControlCommander startSession test`() = runTest {
        runCatching { commander.startSession(target = camera) }
            .onFailure { println(it.message) }
    }

    @Test
    fun `LocalPythonTapoControlCommander endSession test`() = runTest {
        commander.endSession()
    }

    @Test
    fun `LocalPythonTapoControlCommander motion right test`() = runTest {
        runCatching {
            commander.move(
                target = camera,
                horizontal = Vector.of(ScalarD.ONE),
                vertical = Vector.STATIONARY,
            )
        }
            .onFailure { it.printStackTrace() }
    }

    @Test
    fun `LocalPythonTapoControlCommander motion left test`() = runTest {
        runCatching {
            commander.move(
                target = camera,
                horizontal = Vector.of(ScalarD.ONE.inversion),
                vertical = Vector.STATIONARY,
            )
        }
            .onFailure { it.printStackTrace() }
    }

    @Test
    fun `LocalPythonTapoControlCommander motion up test`() = runTest {
        runCatching {
            commander.move(
                target = camera,
                horizontal = Vector.STATIONARY,
                vertical = Vector.of(ScalarD.ONE),
            )
        }
            .onFailure { it.printStackTrace() }
    }

    @Test
    fun `LocalPythonTapoControlCommander motion down test`() = runTest {
        runCatching {
            commander.move(
                target = camera,
                horizontal = Vector.STATIONARY,
                vertical = Vector.of(ScalarD.ONE.inversion),
            )
        }
            .onFailure { it.printStackTrace() }
    }

    @Test
    fun `LocalPythonTapoControlCommander motion left and up test`() = runTest {
        runCatching {
            commander.move(
                target = camera,
                horizontal = Vector.of(ScalarD.ONE.inversion),
                vertical = Vector.of(ScalarD.ONE),
            )
        }
            .onFailure { it.printStackTrace() }
    }

    private val commander = LocalPythonTapoControlCommander(httpClient = Client)
    private val camera = Camera(
        cameraId = CameraId.generate(),
        cameraName = CameraName.of("B3-Alpha-0001"),
        username = "B3C_Alpha_0001",
        password = "nydxiS-puqbyj-4cefro",
        ipv4Address = "192.168.11.6",
        port = 2020,
    )
}
