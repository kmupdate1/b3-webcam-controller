package org.bluebikebase.ioe.resource.transaction

import org.bluebikebase.ioe.resource.vessel.Ship
import org.bluebikebase.ioe.resource.vessel.lifecycle.ShipLifecycle

class ReificationTransaction<T, R>(
    private val manager: Ship<T, R>,
) : ShipTransaction {
    override suspend fun execute(): Result<ShipLifecycle> {
        TODO("Not yet implemented")
    }
}
