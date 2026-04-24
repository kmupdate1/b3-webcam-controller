package org.bluebikebase.wcc.ioe.camera

import kotlinx.coroutines.channels.Channel
import org.bluebikebase.core.algebra.Vector
import org.bluebikebase.core.geometry.ScalarDRange
import org.bluebikebase.wcc.domain.camera.agreement.Motion
import org.bluebikebase.wcc.domain.camera.agreement.Zoom
import org.bluebikebase.wcc.domain.camera.entity.Camera

class LocalPythonControlCommander : Motion, Zoom {
    override val horizontalLimit: ScalarDRange
        get() = TODO("Not yet implemented")
    override val verticalLimit: ScalarDRange
        get() = TODO("Not yet implemented")

    override fun move(
        target: Camera,
        horizontal: Vector,
        vertical: Vector
    ) {
        TODO("Not yet implemented")
    }

    override val zoomRange: ScalarDRange
        get() = TODO("Not yet implemented")

    override fun zoom(target: Camera, velocity: Vector) {
        TODO("Not yet implemented")
    }

    private val motionChannel = Channel<Pair<Vector, Vector>>(Channel.CONFLATED)
    private val zoomChannel = Channel<Vector>(Channel.CONFLATED)
}
