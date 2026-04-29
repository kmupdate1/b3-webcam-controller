package org.bluebikebase.ioe.resource.berth

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.bluebikebase.core.foundation.ScalarL
import org.bluebikebase.ioe.resource.vessel.LoneWolf
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose
import org.bluebikebase.ioe.resource.vessel.lifecycle.Establish

@PublishedApi
internal class LoneWolfBerth<T, R>(
    val scaleSize: ShipScaleSize = ShipScaleSize(ScalarL.ONE),
    val wolves: MutableSet<LoneWolf<T, R>>,
) {
    suspend fun invite(establish: Establish<T>, cleanup: Cleanup<T>, dispose: Dispose<T>): LoneWolf<T, R> =
        berthOrder.withLock {
            val masterShip = wolves.first()
            TODO("Not yet implemented")
        }

    private val berthOrder = Mutex()
}
