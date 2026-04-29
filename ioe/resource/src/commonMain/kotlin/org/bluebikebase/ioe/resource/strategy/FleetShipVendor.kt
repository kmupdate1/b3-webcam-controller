package org.bluebikebase.ioe.resource.strategy

import org.bluebikebase.ioe.resource.vessel.Fleet
import org.bluebikebase.ioe.resource.vessel.lifecycle.Establish

internal interface FleetShipVendor<T, R> {
    fun vend(ships: MutableSet<Fleet<T, R>>, establish: Establish<T>): Fleet<T, R>
}
