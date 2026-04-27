package org.bluebikebase.ioe.resource

class ReificationTransaction<T, R>(
    private val manager: Ship<T, R>,
) : ShipTransaction {
    override suspend fun execute(): Result<ShipLifecycle> {
        TODO("Not yet implemented")
    }
}
