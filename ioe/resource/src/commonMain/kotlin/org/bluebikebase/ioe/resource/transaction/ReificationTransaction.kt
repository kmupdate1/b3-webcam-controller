package org.bluebikebase.ioe.resource.transaction

import org.bluebikebase.ioe.resource.vessel.Ship
import org.bluebikebase.ioe.resource.vessel.lifecycle.ShipLifecycle

class ReificationTransaction<T, R>(
    private val manager: Ship<T, R>,
) : ShipTransaction<R> {
    override suspend fun execute(): Result<R> {
        TODO("Not yet implemented")
    }
}
