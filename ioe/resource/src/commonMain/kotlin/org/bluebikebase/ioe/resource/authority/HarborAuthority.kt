package org.bluebikebase.ioe.resource.authority

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import org.bluebikebase.core.foundation.Identity
import org.bluebikebase.ioe.resource.berth.Berth
import org.bluebikebase.ioe.resource.error.B3IoeIllegalResourceException
import org.bluebikebase.ioe.resource.transaction.Dispatcher
import org.bluebikebase.ioe.resource.transaction.ShipTransaction
import org.bluebikebase.ioe.resource.vessel.Ship

class HarborAuthority<T, R> internal constructor(
    internal val registry: Registry<T, R>,
    internal val berths: MutableMap<Identity, Berth<T, R>>,
) : Reception<T, R>, Dispatcher<R> {
    override suspend fun welcomeTo(destinationId: Identity): Ship<T, R> =
        berths[destinationId]?.run {
            val establish = registry.establishes.getValue(destinationId)
            val cleanup = registry.cleanups.getValue(destinationId)
            val dispose = registry.disposes.getValue(destinationId)

            invite(establish, cleanup, dispose)
        } ?: throw B3IoeIllegalResourceException("Not yet initialized: $destinationId")

    override suspend fun dispatch(transaction: ShipTransaction<R>): Result<R> =
        withContext(currentCoroutineContext()) {
            try { transaction.execute() }
            catch (e: Throwable) { Result.failure(e) }
        }

    internal suspend fun prepare(destinationId: Identity): Ship<T, R> = welcomeTo(destinationId)
}
