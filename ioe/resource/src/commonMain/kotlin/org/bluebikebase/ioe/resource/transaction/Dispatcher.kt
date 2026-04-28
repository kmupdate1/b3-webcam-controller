package org.bluebikebase.ioe.resource.transaction

interface Dispatcher<R> {
    suspend fun dispatch(transaction: ShipTransaction<R>): Result<R>
}
