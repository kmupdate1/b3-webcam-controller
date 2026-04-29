package org.bluebikebase.ioe.resource.authority

import org.bluebikebase.core.foundation.ScalarL
import org.bluebikebase.core.identity.B3Hash
import org.bluebikebase.core.identity.UniqueID
import org.bluebikebase.ioe.resource.authority.context.VesselDress
import org.bluebikebase.ioe.resource.authority.context.VesselPirateDress
import org.bluebikebase.ioe.resource.berth.Berth
import org.bluebikebase.ioe.resource.berth.ShipScaleSize
import org.bluebikebase.ioe.resource.error.B3IoeIllegalResourceException
import org.bluebikebase.ioe.resource.vessel.Ghost
import org.bluebikebase.ioe.resource.vessel.Ship
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose
import org.bluebikebase.ioe.resource.vessel.lifecycle.Establish

class AuthorityBuilder<T, R> {
    inline fun <reified D : VesselDress> reserve(
        noinline establish: suspend () -> T,
        noinline cleanup: suspend (T) -> Unit,
        noinline dispose: suspend (T) -> Unit,
    ): AuthorityBuilder<T, R> {
        val kDress = D::class
        val dressName = kDress.qualifiedName?.also { println("Instance type: $it") }
            ?: throw B3IoeIllegalResourceException("Anonymous dress is not allowed.")

        val destinationId = B3Hash.fromBytes(dressName.encodeToByteArray())

         authority.apply {
             val ships = mutableSetOf(boardOnTheFlyingDutchman(destinationId))
             val limit = if (kDress is VesselPirateDress) ScalarL.ONE else ScalarL.of(10L)

             registry.run {
                 establishes[destinationId] = Establish(establish)
                 cleanups[destinationId] = Cleanup(cleanup)
                 disposes[destinationId] = Dispose(dispose)
             }

             berths[destinationId] = Berth(
                 scaleSize = ShipScaleSize(limit = limit),
                 ships = ships,
             )
         }

        return this
    }

    fun build(): Authority<T, R> = authority

    @PublishedApi
    internal val authority: HarborAuthority<T, R> = HarborAuthority(
        registry = Registry(
            establishes = mutableMapOf(),
            cleanups = mutableMapOf(),
            disposes = mutableMapOf(),
        ),
        berths = mutableMapOf(),
    )

    @PublishedApi
    @Suppress("UNCHECKED_CAST")
    internal fun boardOnTheFlyingDutchman(destinationId: UniqueID): Ship<T, R> =
        Ghost(destinationId) as Ship<T, R>
}
