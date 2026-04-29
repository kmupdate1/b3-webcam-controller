package org.bluebikebase.ioe.resource.strategy

import org.bluebikebase.ioe.resource.vessel.Fleet
import org.bluebikebase.ioe.resource.vessel.lifecycle.Establish

internal class ClassicalFleetVendor<T, R> : FleetShipVendor<T, R> {
    override fun vend(ships: MutableSet<Fleet<T, R>>, establish: Establish<T>): Fleet<T, R> {
        TODO("Not yet implemented")
    }
}
