package org.bluebikebase.ioe.resource.berth

import kotlinx.coroutines.sync.Mutex
import org.bluebikebase.ioe.resource.vessel.Ship

internal class Berth<T, R>(
    private val capacity: ShipCapacity,
    private val ships: MutableSet<Ship<T, R>>,
) {
    suspend fun invite(): Ship<T, R> { TODO() }

    private val berthOrder = Mutex()
}
