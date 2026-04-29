package org.bluebikebase.ioe.resource.berth

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.bluebikebase.core.foundation.ScalarL
import org.bluebikebase.ioe.resource.foundation.Container
import org.bluebikebase.ioe.resource.vessel.Fleet
import org.bluebikebase.ioe.resource.vessel.LoneWolf
import org.bluebikebase.ioe.resource.vessel.Ship
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose
import org.bluebikebase.ioe.resource.vessel.lifecycle.Establish

@PublishedApi
internal class Berth<T, R>(
    private val scaleSize: ShipScaleSize,
    private val ships: MutableSet<Ship<T, R>>,
) {
    suspend fun invite(establish: Establish<T>, cleanup: Cleanup<T>, dispose: Dispose<T>): Ship<T, R> =
        berthOrder.withLock {
            when (val masterShip = ships.first()) {
                is LoneWolf -> masterShip
                is Fleet ->
                    if (ships.size < scaleSize.limit.value) masterShip.replicate().also { ships.add(it) }
                    else ships.last()

                else -> {
                    val resource = establish.invoke()

                    val newVessel = if (scaleSize.limit == ScalarL.ONE)
                        LoneWolf<T, R>(Container(resource), cleanup, dispose) as Ship<T, R>
                    else
                        Fleet<T, R>(Container(resource), cleanup, dispose) as Ship<T, R>

                    newVessel.also {
                        ships.clear()
                        ships.add(it)
                    }
                }
            }
        }

    suspend fun terminate() {

    }

    private val berthOrder = Mutex()
}
