package org.bluebikebase.ioe.resource.authority

import org.bluebikebase.core.foundation.Identity
import org.bluebikebase.core.foundation.ScalarL
import org.bluebikebase.ioe.resource.berth.Berth
import org.bluebikebase.ioe.resource.berth.ShipCapacity
import org.bluebikebase.ioe.resource.vessel.Ghost
import org.bluebikebase.ioe.resource.vessel.Ship
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose
import org.bluebikebase.ioe.resource.vessel.lifecycle.Establish

class AuthorityBuilder<T, R> {
    fun register(
        strUuid: String, isSingle: Boolean = false,
        establish: suspend () -> T,
        cleanup: suspend (T) -> Unit,
        dispose: suspend (T) -> Unit,
    ): AuthorityBuilder<T, R> {
        authority.apply {
            val destinationId = Identity.fromString(strUuid)

            registry.run {
                establishes[destinationId] = Establish(establish)
                cleanups[destinationId] = Cleanup(cleanup)
                disposes[destinationId] = Dispose(dispose)
            }

            val ships = mutableSetOf(boardOnTheFlyingDutchman(destinationId))
            val capacity = if (isSingle) ScalarL.ONE else ScalarL.of(10L)
            berths[destinationId] = Berth(
                capacity = ShipCapacity(size = capacity),
                ships = ships,
            )
        }

        return this
    }

    fun build(): HarborAuthority<T, R> = authority

    private val authority: HarborAuthority<T, R> = HarborAuthority(
        registry = Registry(
            establishes = mutableMapOf(),
            cleanups = mutableMapOf(),
            disposes = mutableMapOf(),
        ),
        berths = mutableMapOf(),
    )

    @Suppress("UNCHECKED_CAST")
    private fun boardOnTheFlyingDutchman(resourceId: Identity): Ship<T, R> =
        Ghost(resourceId) as Ship<T, R>
}
