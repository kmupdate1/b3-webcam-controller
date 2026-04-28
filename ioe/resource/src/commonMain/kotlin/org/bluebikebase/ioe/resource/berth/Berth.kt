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

internal class Berth<T, R>(
    private val capacity: ShipCapacity,
    private val ships: MutableSet<Ship<T, R>>,
) {
    suspend fun invite(establish: Establish<T>, cleanup: Cleanup<T>, dispose: Dispose<T>): Ship<T, R> =
        berthOrder.withLock {
            when (val masterShip = ships.first()) {
                is LoneWolf -> masterShip
                is Fleet ->
                    if (ships.size < capacity.size.value) masterShip.replicate().also { ships.add(it) }
                    else ships.last()

                else -> {
                    val resource = establish.invoke()
                    if (capacity.size == ScalarL.ONE) LoneWolf(Container(resource), cleanup, dispose)
                    else Fleet(Container(resource), cleanup, dispose)
                }
            }
        }

    private val berthOrder = Mutex()
}
