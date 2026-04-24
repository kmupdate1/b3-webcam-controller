package org.bluebikebase.wcc.domain.camera

import org.bluebikebase.wcc.domain.camera.entity.CameraName
import kotlin.test.Test
import kotlin.test.assertTrue

class TestCameraName {
    @Test
    fun `constructor safety name test`() {
        val row = "b3c-alpha-0001"
        val name = CameraName.of(row)
        assertTrue { name.name.startsWith("b3c-alpha-0001@") }
    }

    @Test
    fun `constructor null name test`() {
        val result = runCatching { CameraName.of(null) }
            .onFailure { println(it.message) }
        assertTrue(result.isFailure)
    }

    @Test
    fun `constructor empty name test`() {
        val result = runCatching { CameraName.of("") }
            .onFailure { println(it.message) }
        assertTrue(result.isFailure)
    }

    @Test
    fun `constructor has-blank name test`() {
        val result = runCatching { CameraName.of(" ") }
            .onFailure { println(it.message) }
        assertTrue(result.isFailure)
    }

    @Test
    fun `constructor multi-blank test`() {
        val name = CameraName.of(" a ")
        assertTrue { name.name.startsWith("a@") }
    }

    @Test
    fun `constructor UPPERCASE name test`() {
        val name = CameraName.of("B3C-Alpha-0001")
        assertTrue { name.name.startsWith("b3c-alpha-0001@") }
    }

    @Test
    fun `constructor normalization name test`() {
        val name = CameraName.of(" B3C -alpha - 0001")
        assertTrue { name.name.startsWith("b3c-alpha-0001@") }
    }

    @Test
    fun `constructor invalid char name test`() {
        val result = runCatching { CameraName.of("b3c-£alpha#-£0001") }
            .onFailure { println(it.message) }
        assertTrue(result.isFailure)
    }

    @Test
    fun `constructor uniqueness name test`() {
        val name1 = CameraName.of("B3C-Alpha-0001").also { println(it.name) }
        val name2 = CameraName.of("B3C-Alpha-0001").also { println(it.name) }
        assertTrue { name1 != name2 }
    }

    @Test
    fun `get simple name test`() {
        val name = CameraName.of("B3C-Alpha-0001").also { println(it.name) }
        assertTrue { name.simpleName == "b3c-alpha-0001" }
    }
}
