package org.bluebikebase.wcc.domain.camera

import org.bluebikebase.core.foundation.Identity
import kotlin.jvm.JvmInline

@JvmInline
value class CameraId private constructor(val id: Identity = Identity.gen())
