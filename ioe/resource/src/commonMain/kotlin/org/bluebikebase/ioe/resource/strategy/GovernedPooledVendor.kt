package org.bluebikebase.ioe.resource.strategy

import org.bluebikebase.ioe.resource.foundation.Container
import org.bluebikebase.ioe.resource.vessel.Fleet
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose

class GovernedPooledVendor<T, R>(
    override val fleets: MutableSet<Fleet<T, R>> = mutableSetOf(),
) : FleetShipVendor<T, R> {
    override suspend fun vend(container: Container<T>, cleanup: Cleanup<T>, dispose: Dispose<T>): Fleet<T, R> {
        val masterFleet = fleets.also { println("GOVERNED: ${it.size}隻") }.firstOrNull()

        return masterFleet?.replicate()?.also { fleets.add(it) }
            ?: Fleet<T, R>(container, cleanup, dispose).also { fleets.add(it) }
    }
}
