package org.bluebikebase.ioe.resource.berth

import org.bluebikebase.ioe.resource.foundation.Container
import org.bluebikebase.ioe.resource.strategy.FleetShipVendor
import org.bluebikebase.ioe.resource.vessel.Fleet
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose
import org.bluebikebase.ioe.resource.vessel.lifecycle.Establish

@PublishedApi
internal class FleetBerth<T, R>(
    private val vendor: FleetShipVendor<T, R>,
) {
    suspend fun invite(establish: Establish<T>, cleanup: Cleanup<T>, dispose: Dispose<T>): Fleet<T, R> {
        val resource = establish.invoke()

        return vendor.vend(Container(resource), cleanup, dispose)
    }
}
