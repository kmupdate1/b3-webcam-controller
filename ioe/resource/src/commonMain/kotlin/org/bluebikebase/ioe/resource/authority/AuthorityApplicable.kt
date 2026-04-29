package org.bluebikebase.ioe.resource.authority

import org.bluebikebase.core.foundation.ScalarL
import org.bluebikebase.core.identity.B3Hash
import org.bluebikebase.ioe.resource.authority.context.VesselDress
import org.bluebikebase.ioe.resource.authority.context.VesselPirateDress
import org.bluebikebase.ioe.resource.authority.context.VesselSailorDress
import org.bluebikebase.ioe.resource.berth.FleetBerth
import org.bluebikebase.ioe.resource.berth.LoneWolfBerth
import org.bluebikebase.ioe.resource.berth.ShipScaleSize
import org.bluebikebase.ioe.resource.error.B3IoeIllegalResourceException
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose
import org.bluebikebase.ioe.resource.vessel.lifecycle.Establish

class AuthorityApplicable<T, R> {
    inline fun <reified D : VesselDress> reserve(
        noinline establish: suspend () -> T,
        noinline cleanup: suspend (T) -> Unit,
        noinline dispose: suspend (T) -> Unit,
    ): AuthorityApplicable<T, R> {
        val kDress = D::class
        val dressName = kDress.qualifiedName
            ?: throw B3IoeIllegalResourceException("Anonymous dress is not allowed.")

        val destinationId = B3Hash.fromBytes(dressName.encodeToByteArray())

         authority.apply {
             registry.run {
                 establishes[destinationId] = Establish(establish)
                 cleanups[destinationId] = Cleanup(cleanup)
                 disposes[destinationId] = Dispose(dispose)
             }

             when (kDress) {
                 is VesselSailorDress -> {
                     fleetBerths[destinationId] = FleetBerth(
                         scaleSize = ShipScaleSize(ScalarL.of(10L)),
                         fleets = mutableSetOf()
                     )
                 }

                 is VesselPirateDress -> {
                     lwBerths[destinationId] = LoneWolfBerth(
                         wolves = mutableSetOf(),
                     )
                 }
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
