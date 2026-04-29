package org.bluebikebase.ioe.resource.authority

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import org.bluebikebase.core.identity.B3Hash
import org.bluebikebase.core.identity.UniqueID
import org.bluebikebase.ioe.resource.authority.context.VesselDress
import org.bluebikebase.ioe.resource.berth.FleetBerth
import org.bluebikebase.ioe.resource.berth.LoneWolfBerth
import org.bluebikebase.ioe.resource.error.B3IoeIllegalResourceException
import org.bluebikebase.ioe.resource.transaction.ShipTransaction
import org.bluebikebase.ioe.resource.vessel.Fleet
import org.bluebikebase.ioe.resource.vessel.LoneWolf

class HarborAuthority<T, R> internal constructor(
    @PublishedApi internal val registry: Registry<T, R>,
    @PublishedApi internal val lwBerths: MutableMap<UniqueID, LoneWolfBerth<T, R>>,
    @PublishedApi internal val fleetBerths: MutableMap<UniqueID, FleetBerth<T, R>>,
) : Authority<T, R> {
    override suspend fun welcomeToPirate(): LoneWolf<T, R> {
        val currentContext = currentCoroutineContext()
        val kDress = currentContext[VesselDress]?.get(VesselDress)
            ?: throw B3IoeIllegalResourceException("No Pirate Dress found")
        val destinationId = getHash(kDress)
            ?: throw B3IoeIllegalResourceException("No Destination ID found")

        return lwBerths[destinationId]?.run {
            val establish = registry.establishes.getValue(destinationId)
            val cleanup = registry.cleanups.getValue(destinationId)
            val dispose = registry.disposes.getValue(destinationId)

            invite(establish, cleanup, dispose)
        } ?: throw B3IoeIllegalResourceException("Not reserved destination ID: $destinationId")
    }

    override suspend fun welcomeToSailor(): Fleet<T, R> {
        val currentContext = currentCoroutineContext()
        val kDress = currentContext[VesselDress]?.get(VesselDress)
            ?: throw B3IoeIllegalResourceException("No Sailor Dress found")
        val destinationId = getHash(kDress)
            ?: throw B3IoeIllegalResourceException("No Destination ID found")

        return fleetBerths[destinationId]?.run {
            val establish = registry.establishes.getValue(destinationId)
            val cleanup = registry.cleanups.getValue(destinationId)
            val dispose = registry.disposes.getValue(destinationId)

            invite(establish, cleanup, dispose).also { println("invite()したね") }
        } ?: throw B3IoeIllegalResourceException("Not reserved destination ID: $destinationId")
    }

    override suspend fun dispatch(transaction: ShipTransaction<R>): Result<R> =
        withContext(Dispatchers.IO) {
            try { transaction.execute() }
            catch (e: Throwable) { Result.failure(e) }
        }

    override suspend fun terminate() = withContext(Dispatchers.IO) {
    }

    internal suspend fun prepareFleet(): Fleet<T, R> = welcomeToSailor()
    internal suspend fun prepareWolf(): LoneWolf<T, R> = welcomeToPirate()

    private fun getHash(kDress: VesselDress): B3Hash? =
        kDress::class.qualifiedName?.run { B3Hash.fromBytes(this.encodeToByteArray()) }
}
