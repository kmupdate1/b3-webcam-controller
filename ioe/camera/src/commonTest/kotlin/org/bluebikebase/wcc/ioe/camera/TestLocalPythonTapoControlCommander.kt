package org.bluebikebase.wcc.ioe.camera

import kotlinx.coroutines.test.runTest
import org.bluebikebase.wcc.domain.camera.entity.Camera
import org.bluebikebase.wcc.domain.camera.entity.CameraId
import org.bluebikebase.wcc.domain.camera.entity.CameraName
import org.bluebikebase.wcc.ioe.util.Client
import kotlin.test.Test
import kotlin.test.assertTrue

class TestLocalPythonTapoControlCommander {
    @Test
    fun `LocalPythonTapoControlCommander startSession test`() = runTest {
        val res = runCatching { commander.startSession(target = camera) }.isSuccess
        assertTrue(res)
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
