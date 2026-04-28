package org.bluebikebase.ioe.resource.authority

import kotlinx.coroutines.withContext
import org.bluebikebase.core.foundation.Identity
import org.bluebikebase.ioe.resource.berth.Berth
import org.bluebikebase.ioe.resource.error.B3IoeIllegalResourceException
import org.bluebikebase.ioe.resource.transaction.Dispatcher
import org.bluebikebase.ioe.resource.transaction.ShipTransaction
import org.bluebikebase.ioe.resource.vessel.Ship
import kotlin.coroutines.coroutineContext

class HarborAuthority<T, R> internal constructor(
    internal val registry: Registry<T, R>,
    internal val berths: MutableMap<Identity, Berth<T, R>>,
) : Reception<T, R>, Dispatcher<R> {
    override suspend fun welcomeTo(destinationId: Identity): Ship<T, R> =
        berths[destinationId]?.run { invite() }
            ?: throw B3IoeIllegalResourceException("Not yet initialized: $destinationId")

    override suspend fun dispatch(transaction: ShipTransaction<R>): Result<R> =
        withContext(coroutineContext) {
            try { transaction.execute() }
            catch (e: Throwable) { Result.failure(e) }
        }

    internal suspend fun prepare(destinationId: Identity): Ship<T, R> = welcomeTo(destinationId)
}
