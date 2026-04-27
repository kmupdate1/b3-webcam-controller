package org.bluebikebase.ioe.resource.transaction

import org.bluebikebase.ioe.resource.vessel.lifecycle.ShipLifecycle

interface Dispatcher {
    suspend fun dispatch(transaction: ShipTransaction): Result<ShipLifecycle>
}
