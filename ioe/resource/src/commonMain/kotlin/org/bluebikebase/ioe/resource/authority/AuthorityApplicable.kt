package org.bluebikebase.ioe.resource.authority

import org.bluebikebase.core.identity.B3Hash
import org.bluebikebase.ioe.resource.authority.context.DressType
import org.bluebikebase.ioe.resource.authority.context.VesselDress
import org.bluebikebase.ioe.resource.berth.FleetBerth
import org.bluebikebase.ioe.resource.berth.LoneWolfBerth
import org.bluebikebase.ioe.resource.error.B3IoeIllegalResourceException
import org.bluebikebase.ioe.resource.strategy.ClassicalFleetVendor
import org.bluebikebase.ioe.resource.strategy.ClassicalLoneWolfVendor
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose
import org.bluebikebase.ioe.resource.vessel.lifecycle.Establish

class AuthorityApplicable<T, R> {
    inline fun <reified D : VesselDress> reserve(
        noinline establish: suspend () -> T,
        noinline cleanup: suspend (T) -> Unit,
        noinline dispose: suspend (T) -> Unit,
        dressType: DressType,
    ): AuthorityApplicable<T, R> {
        val dressName = D::class.qualifiedName
            ?: throw B3IoeIllegalResourceException("No instance QFN found")
        val destinationId = B3Hash.fromBytes(dressName.encodeToByteArray())

         authority.apply {
             registry.run {
                 establishes[destinationId] = Establish(establish)
                 cleanups[destinationId] = Cleanup(cleanup)
                 disposes[destinationId] = Dispose(dispose)
             }

             when (dressType) {
                 DressType.SAILOR ->
                     fleetBerths[destinationId] = FleetBerth(
                         vendor = ClassicalFleetVendor(mutableSetOf()),
                     )

                 DressType.PIRATES ->
                     lwBerths[destinationId] = LoneWolfBerth(
                         vendor = ClassicalLoneWolfVendor(mutableSetOf()),
                     )
             }
         }

        return this
    }

    fun applicate(): Authority<T, R> = authority

    @PublishedApi
    internal val authority: HarborAuthority<T, R> = HarborAuthority(
        registry = Registry(
            establishes = mutableMapOf(),
            cleanups = mutableMapOf(),
            disposes = mutableMapOf(),
        ),
        lwBerths = mutableMapOf(),
        fleetBerths = mutableMapOf(),
    )
}
