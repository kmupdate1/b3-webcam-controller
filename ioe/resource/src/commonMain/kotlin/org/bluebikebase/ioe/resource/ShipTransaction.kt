package org.bluebikebase.ioe.resource

interface ShipTransaction {
    suspend fun execute(): Result<ShipLifecycle>
}
