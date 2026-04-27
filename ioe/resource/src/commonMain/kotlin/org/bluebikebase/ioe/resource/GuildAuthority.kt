package org.bluebikebase.ioe.resource

import org.bluebikebase.core.foundation.Identity
import org.bluebikebase.core.foundation.ScalarL
import org.bluebikebase.ioe.resource.error.B3IoeIllegalResourceException

class GuildAuthority<T, R> private constructor(
    private val context: ShipContext<T, R>,
    private val berths: MutableMap<Identity, Berth<T, R>>,
    private val dispatcher: Dispatcher = Dispatcher,
) : Reception<T, R> {
    override suspend fun welcomeTo(destinationId: Identity): Ship<T, R> = berths[destinationId]?.invite()
         ?: throw B3IoeIllegalResourceException("Not yet initialized: $destinationId")

    /*
        context.managers[destinationId]?.let { manager -> if (manager !is Ghost) return manager }
            ?: throw B3IoeIllegalResourceException("Resource not yet registered: $destinationId")

        return dockOrder.withLock {
            val recipe = context.recipes.getValue(destinationId)
            val cleanup = context.cleanups.getValue(destinationId)
            val dispose = context.disposes.getValue(destinationId)
            val container = Container(recipe())

            val manager = if (context.withSingle.getValue(destinationId))
                LoneWolf<T, R>(container, cleanup, dispose) as Ship<T, R>
            else
                Fleet<T, R>(container, cleanup, dispose) as Ship<T, R>

            manager.also {
                context.managers.remove(destinationId)
                context.managers[destinationId] = it
            }
        }
        */

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

                context.run {
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
            context = ShipContext(
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
