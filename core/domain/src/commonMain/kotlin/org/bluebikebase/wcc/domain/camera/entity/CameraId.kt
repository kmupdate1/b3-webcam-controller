package org.bluebikebase.wcc.domain.camera.entity

import org.bluebikebase.core.foundation.Identity
import kotlin.jvm.JvmInline

@JvmInline
value class CameraId private constructor(val id: Identity = Identity.gen())
