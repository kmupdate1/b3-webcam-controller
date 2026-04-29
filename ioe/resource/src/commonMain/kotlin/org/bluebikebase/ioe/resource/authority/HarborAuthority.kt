package org.bluebikebase.ioe.resource.authority

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import org.bluebikebase.core.identity.UniqueID
import org.bluebikebase.ioe.resource.authority.context.VesselDress
import org.bluebikebase.ioe.resource.berth.Berth
import org.bluebikebase.ioe.resource.error.B3IoeIllegalResourceException
import org.bluebikebase.ioe.resource.transaction.ShipTransaction
import org.bluebikebase.ioe.resource.vessel.Ship

class HarborAuthority<T, R> internal constructor(
    @PublishedApi internal val registry: Registry<T, R>,
    @PublishedApi internal val berths: MutableMap<UniqueID, Berth<T, R>>,
) : Authority<T, R> {
    override suspend fun welcomeToShip(): Ship<T, R> {
        val currentContext = currentCoroutineContext()
        val kDress = currentContext[VesselDress]
            ?: throw B3IoeIllegalResourceException("No Vessel Dress found")
        val destinationId = kDress.destinationId

        return berths[destinationId]?.run {
            val establish = registry.establishes.getValue(destinationId)
            val cleanup = registry.cleanups.getValue(destinationId)
            val dispose = registry.disposes.getValue(destinationId)

            invite(establish, cleanup, dispose)
        } ?: throw B3IoeIllegalResourceException("Not yet initialized: $destinationId")
    }

    override suspend fun dispatch(transaction: ShipTransaction<R>): Result<R> =
        withContext(Dispatchers.IO) {
            try { transaction.execute() }
            catch (e: Throwable) { Result.failure(e) }
        }

    override suspend fun terminate() = withContext(Dispatchers.IO) {
    }

    internal suspend fun prepare(): Ship<T, R> = welcomeToShip()
}
