package org.bluebikebase.ioe.resource.authority

import org.bluebikebase.core.foundation.Identity
import org.bluebikebase.core.foundation.ScalarL
import org.bluebikebase.ioe.resource.berth.Berth
import org.bluebikebase.ioe.resource.berth.ShipCapacity
import org.bluebikebase.ioe.resource.error.B3IoeIllegalResourceException
import org.bluebikebase.ioe.resource.transaction.Dispatcher
import org.bluebikebase.ioe.resource.transaction.ShipTransaction
import org.bluebikebase.ioe.resource.vessel.Ghost
import org.bluebikebase.ioe.resource.vessel.Ship
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose
import org.bluebikebase.ioe.resource.vessel.lifecycle.Recipe
import org.bluebikebase.ioe.resource.vessel.lifecycle.ShipLifecycle

class GuildAuthority<T, R> private constructor(
    private val registry: Registry<T, R>,
    private val berths: MutableMap<Identity, Berth<T, R>>,
) : Reception<T, R>, Dispatcher {
    override suspend fun welcomeTo(destinationId: Identity): Ship<T, R> =
        berths[destinationId]?.run {
            val recipe = registry.recipes.getValue(destinationId)
            val cleanup = registry.cleanups.getValue(destinationId)
            val dispose = registry.disposes.getValue(destinationId)

            invite(recipe, cleanup, dispose)
        } ?: throw B3IoeIllegalResourceException("Not yet initialized: $destinationId")

    override suspend fun dispatch(transaction: ShipTransaction): Result<ShipLifecycle> {
        TODO("Not yet implemented")
    }

    internal suspend fun prepare(destinationId: Identity): Ship<T, R> = welcomeTo(destinationId)

    /**
     * ギルド（シングルトンを想定）を開店するための足掛かり
     */
    class Builder<T, R> private constructor() {
        val instance: GuildAuthority<T, R> get() = guild

        fun register(
            strUuid: String, isSingle: Boolean = false,
            recipe: suspend () -> T,
            cleanup: suspend (T) -> Unit,
            dispose: suspend (T) -> Unit,
        ): Builder<T, R> {
            guild.apply {
                val destinationId = Identity.fromString(strUuid)

                registry.run {
                    withSingle[destinationId] = isSingle
                    recipes[destinationId] = Recipe(recipe)
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

        private val guild: GuildAuthority<T, R> = GuildAuthority(
            registry = Registry(
                withSingle = mutableMapOf(),
                recipes = mutableMapOf(),
                cleanups = mutableMapOf(),
                disposes = mutableMapOf(),
            ),
            berths = mutableMapOf(),
        )

        @Suppress("UNCHECKED_CAST")
        private fun boardOnTheFlyingDutchman(resourceId: Identity): Ship<T, R> =
            Ghost(resourceId) as Ship<T, R>
    }
}
