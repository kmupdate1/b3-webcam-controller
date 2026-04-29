package org.bluebikebase.ioe.resource.strategy

import org.bluebikebase.core.foundation.ScalarL
import org.bluebikebase.ioe.resource.foundation.Container
import org.bluebikebase.ioe.resource.vessel.Fleet
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose

class ClassicalFleetVendor<T, R>(
    override val fleets: MutableSet<Fleet<T, R>> = mutableSetOf(),
    private val scaleLimit: Long = 10L,
) : FleetShipVendor<T, R> {
    override suspend fun vend(container: Container<T>, cleanup: Cleanup<T>, dispose: Dispose<T>): Fleet<T, R> {
        val masterFleet = fleets.also { println("CLASSICAL: ${it.size}隻") }.firstOrNull()

        return masterFleet?.let {
            val limit = ScalarL.of(scaleLimit)
            if (fleets.size < limit.value) it.replicate()
            else it
        }?.also { fleets.add(it) }
            ?: Fleet<T, R>(container, cleanup, dispose).also { fleets.add(it) }
    }
}
