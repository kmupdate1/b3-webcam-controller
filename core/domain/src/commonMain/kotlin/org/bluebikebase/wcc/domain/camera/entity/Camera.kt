package org.bluebikebase.wcc.domain.camera.entity

data class Camera(
    val cameraId: CameraId,
    val cameraName: CameraName,
    val username: String,
    val password: String,
    val ipv4Address: String,
    val port: Int,
)
