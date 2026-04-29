package org.bluebikebase.ioe.resource.berth

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.bluebikebase.core.foundation.ScalarL
import org.bluebikebase.ioe.resource.foundation.Container
import org.bluebikebase.ioe.resource.strategy.LoneWolfShipVendor
import org.bluebikebase.ioe.resource.vessel.LoneWolf
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose
import org.bluebikebase.ioe.resource.vessel.lifecycle.Establish

@PublishedApi
internal class LoneWolfBerth<T, R>(
    private val scaleSize: ShipScaleSize = ShipScaleSize(ScalarL.ONE),
    private val vendor: LoneWolfShipVendor<T, R>,
) {
    suspend fun invite(establish: Establish<T>, cleanup: Cleanup<T>, dispose: Dispose<T>): LoneWolf<T, R> =
        berthOrder.withLock {
            val resource = establish.invoke()

            vendor.vend(Container(resource), cleanup, dispose)
        }

    private val berthOrder = Mutex()
}
