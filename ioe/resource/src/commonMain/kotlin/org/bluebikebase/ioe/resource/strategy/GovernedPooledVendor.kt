package org.bluebikebase.ioe.resource.strategy

import org.bluebikebase.ioe.resource.foundation.Container
import org.bluebikebase.ioe.resource.vessel.Fleet
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose

class GovernedPooledVendor<T, R> : FleetShipVendor<T, R> {
    override suspend fun vend(container: Container<T>, cleanup: Cleanup<T>, dispose: Dispose<T>): Fleet<T, R> {
        val masterFleet = _fleets.firstOrNull()

        return masterFleet?.replicate()?.also { _fleets.add(it) }
            ?: Fleet<T, R>(container, cleanup, dispose).also { _fleets.add(it) }
    }

    override val fleets: Set<Fleet<T, R>> get() = _fleets
    private val _fleets = mutableSetOf<Fleet<T, R>>()
}
