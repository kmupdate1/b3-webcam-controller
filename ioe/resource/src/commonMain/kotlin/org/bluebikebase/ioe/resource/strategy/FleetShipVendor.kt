package org.bluebikebase.ioe.resource.strategy

import org.bluebikebase.ioe.resource.foundation.Container
import org.bluebikebase.ioe.resource.vessel.Fleet
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose

internal interface FleetShipVendor<T, R> {
    val fleets: MutableSet<Fleet<T, R>>
    suspend fun vend(container: Container<T>, cleanup: Cleanup<T>, dispose: Dispose<T>): Fleet<T, R>
}
