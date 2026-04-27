package org.bluebikebase.ioe.resource

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.bluebikebase.core.foundation.Identity
import org.bluebikebase.ioe.resource.error.B3IoeIllegalResourceException

class ResourceGuild<T, R> private constructor(
    private val context: ResourceContext<T, R>,
) : Reception<T, R> {
    override suspend fun inviteTo(resourceId: Identity): ResourceManager<T, R> {
        context.managers[resourceId]?.let { manager -> if (manager !is Ghost) return manager }
            ?: throw B3IoeIllegalResourceException("Resource not yet registered: $resourceId")

        return dockOrder.withLock {
            val recipe = context.recipes.getValue(resourceId)
            val cleanup = context.cleanups.getValue(resourceId)
            val dispose = context.disposes.getValue(resourceId)
            val container = ResourceContainer(recipe())

            val manager = if (context.withSingle.getValue(resourceId))
                LoneWolf<T, R>(container, cleanup, dispose) as ResourceManager<T, R>
            else
                Fleet<T, R>(container, cleanup, dispose) as ResourceManager<T, R>

            manager.also {
                context.managers.remove(resourceId)
                context.managers[resourceId] = it
            }
        }
    }

    internal suspend fun prepare(resourceId: Identity): ResourceManager<T, R> = inviteTo(resourceId)

    private val dockOrder = Mutex()

    /**
     * ギルド（シングルトンを想定）を開店するための足掛かり
     */
    class Builder<T, R> private constructor() {
        private val guild: ResourceGuild<T, R> = ResourceGuild(
            context = ResourceContext(
                withSingle = mutableMapOf(),
                recipes = mutableMapOf(),
                cleanups = mutableMapOf(),
                disposes = mutableMapOf(),
                managers = mutableMapOf(),
            )
        )

        fun register(
            strUuid: String, isSingle: Boolean = false,
            recipe: suspend () -> T,
            clean: suspend (T) -> Unit,
            delete: suspend (T) -> Unit,
        ): Builder<T, R> {
            guild.context.apply {
                val resourceId = Identity.fromString(strUuid)

                withSingle[resourceId] = isSingle
                recipes[resourceId] = Recipe(recipe)
                cleanups[resourceId] = Cleanup(clean)
                disposes[resourceId] = Dispose(delete)
                managers[resourceId] = boardOnTheFlyingDutchman(resourceId)
            }

            return this
        }

        fun open(): ResourceGuild<T, R> = guild

        @Suppress("UNCHECKED_CAST")
        private fun boardOnTheFlyingDutchman(resourceId: Identity): ResourceManager<T, R> =
            Ghost(resourceId) as ResourceManager<T, R>
    }
}
