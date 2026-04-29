package org.bluebikebase.ioe.resource.berth

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.bluebikebase.ioe.resource.vessel.Fleet
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose
import org.bluebikebase.ioe.resource.vessel.lifecycle.Establish

@PublishedApi
internal class FleetBerth<T, R>(
    val scaleSize: ShipScaleSize,
    val fleets: MutableSet<Fleet<T, R>>,
) {
    suspend fun invite(establish: Establish<T>, cleanup: Cleanup<T>, dispose: Dispose<T>): Fleet<T, R> =
        berthOrder.withLock {
            TODO("Not yet implemented")
        }

    private val berthOrder = Mutex()
}
