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
import org.bluebikebase.ioe.resource.vessel.lifecycle.Recipe

internal class Berth<T, R>(
    private val capacity: ShipCapacity,
    private val ships: MutableSet<Ship<T, R>>,
) {
    suspend fun invite(
        recipe: Recipe<T>,
        cleanup: Cleanup<T>,
        dispose: Dispose<T>,
    ): Ship<T, R> = berthOrder.withLock {
        when (val masterShip = ships.first()) {
            is LoneWolf -> masterShip

            is Fleet -> if (ships.size < capacity.size.value)
                masterShip.replicate().also { ships.add(it) }
            else ships.last()

            else -> {
                val resource = recipe()
                val container = Container(resource)
                val wolf = LoneWolf<T, R>(container, cleanup, dispose) as Ship<T, R>
                val fleet = Fleet<T, R>(container, cleanup, dispose) as Ship<T, R>

                val newVessel = if (capacity.size == ScalarL.ONE) wolf else fleet
                newVessel.also {
                    ships.clear()
                    ships.add(it)
                }
            }
        }
    }

    private val berthOrder = Mutex()
}
