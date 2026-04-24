package org.bluebikebase.wcc.ioe.camera

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TapoCamSpecs(
    @SerialName("pan") val horizontalRange: HorizontalRange,
    @SerialName("tilt") val verticalRange: VerticalRange,
    @SerialName("zoom_supported") val isZoomSupported: Boolean,
)
