package org.bluebikebase.wcc.ioe.camera

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bluebikebase.core.foundation.ScalarD

@Serializable
data class HorizontalRange(
    @SerialName("min") val min: ScalarD,
    @SerialName("max") val max: ScalarD,
)
