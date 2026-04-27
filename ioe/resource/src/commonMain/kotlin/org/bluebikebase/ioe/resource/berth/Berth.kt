package org.bluebikebase.ioe.resource.berth

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.bluebikebase.ioe.resource.vessel.Fleet
import org.bluebikebase.ioe.resource.vessel.Ship

internal class Berth<T, R>(
    private val capacity: ShipCapacity,
    private val ships: MutableSet<Ship<T, R>>,
) {
    suspend fun invite(): Ship<T, R> = berthOrder.withLock {
        when (val masterShip = ships.first()) {
            is Fleet ->
                if (ships.size < capacity.size.value) masterShip.replicate().also { ships.add(it) }
                else ships.last()

            else -> masterShip
        }
    }

    private val berthOrder = Mutex()
}
