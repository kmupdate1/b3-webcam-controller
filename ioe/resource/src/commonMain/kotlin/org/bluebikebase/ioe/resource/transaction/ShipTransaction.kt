package org.bluebikebase.ioe.resource.transaction

import org.bluebikebase.ioe.resource.vessel.lifecycle.ShipLifecycle

interface ShipTransaction {
    suspend fun execute(): Result<ShipLifecycle>
}
