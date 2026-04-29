package org.bluebikebase.ioe.resource.berth

import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.bluebikebase.core.foundation.ScalarL
import org.bluebikebase.ioe.resource.foundation.Container
import org.bluebikebase.ioe.resource.strategy.FleetShipVendor
import org.bluebikebase.ioe.resource.vessel.Fleet
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose
import org.bluebikebase.ioe.resource.vessel.lifecycle.Establish

@PublishedApi
internal class FleetBerth<T, R>(
    private val scaleSize: ShipScaleSize = ShipScaleSize(ScalarL.of(100L)),
    private val vendor: FleetShipVendor<T, R>,
) {
    suspend fun invite(establish: Establish<T>, cleanup: Cleanup<T>, dispose: Dispose<T>): Fleet<T, R> =
        berthOrder.withPermit {
            val resource = establish.invoke()

            vendor.vend(Container(resource), cleanup, dispose)
        }

    private val berthOrder = Semaphore(permits = scaleSize.limit.value.toInt())
}
