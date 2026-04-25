package org.bluebikebase.wcc.domain.camera.agreement

import org.bluebikebase.wcc.domain.camera.entity.Camera

interface Initializable {
    suspend fun startSession(target: Camera)
    suspend fun endSession()
    suspend fun fetchSpecs()
}
