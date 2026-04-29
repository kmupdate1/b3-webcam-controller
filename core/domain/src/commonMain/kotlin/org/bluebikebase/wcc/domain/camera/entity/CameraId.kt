package org.bluebikebase.wcc.domain.camera.entity

import org.bluebikebase.core.identity.B3Uuid
import org.bluebikebase.core.identity.UniqueID
import kotlin.jvm.JvmInline

@JvmInline
value class CameraId private constructor(val id: UniqueID = B3Uuid.gen()) {
    companion object {
        fun generate(): CameraId = CameraId()
    }
}
