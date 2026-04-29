package org.bluebikebase.ioe.resource.strategy

import org.bluebikebase.ioe.resource.foundation.Container
import org.bluebikebase.ioe.resource.vessel.Fleet
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose

class LazyScaleVendor<T, R>(
    override val fleets: MutableSet<Fleet<T, R>>,
) : FleetShipVendor<T, R> {
    override suspend fun vend(container: Container<T>, cleanup: Cleanup<T>, dispose: Dispose<T>): Fleet<T, R> {
        TODO("Not yet implemented")
    }
}
