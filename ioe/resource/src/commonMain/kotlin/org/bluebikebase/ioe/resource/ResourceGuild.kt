package org.bluebikebase.ioe.resource

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.bluebikebase.core.foundation.Identity

class ResourceGuild<T, R> private constructor(
    private val withSingle: MutableMap<Identity, Boolean>,
    private val recipes: MutableMap<Identity, Recipe<T>>,
    private val cleanups: MutableMap<Identity, Cleanup<T>>,
    private val disposes: MutableMap<Identity, Dispose<T>>,
    private val managers: MutableMap<Identity, ResourceManager<T, R>>,
) : Reception<T, R> {
    override suspend fun inviteTo(resourceId: Identity): ResourceManager<T, R> = managers[resourceId] ?: Mutex().run {
        withLock {
            val resource = recipes.getValue(resourceId).function.invoke()
            val cleanup = cleanups.getValue(resourceId)
            val dispose = disposes.getValue(resourceId)
            val container = ResourceContainer(resource)

            val manager = if (withSingle.getValue(resourceId))
                LoneWolf<T, R>(container, cleanup, dispose) as ResourceManager<T, R>
            else
                Fleet<T, R>(container, cleanup, dispose) as ResourceManager<T, R>

            manager.also { managers[resourceId] = it }
        }
    }

    internal suspend fun prepare(resourceId: Identity): ResourceManager<T, R> = inviteTo(resourceId)

    /**
     * ギルド（シングルトンを想定）を開店するための足掛かり
     */
    class Builder<T, R> private constructor() {
        private val guild: ResourceGuild<T, R> = ResourceGuild(
            withSingle = mutableMapOf(),
            recipes = mutableMapOf(),
            cleanups = mutableMapOf(),
            disposes = mutableMapOf(),
            managers = mutableMapOf(),
        )

        fun register(
            strUuid: String, isSingle: Boolean = false,
            recipe: suspend () -> T,
            cleanup: suspend (T) -> Unit,
            dispose: suspend (T) -> Unit,
        ): Builder<T, R> {
            guild.apply {
                val resourceId = Identity.fromString(strUuid)

                withSingle[resourceId] = isSingle
                recipes[resourceId] = Recipe(recipe)
                cleanups[resourceId] = Cleanup(cleanup)
                disposes[resourceId] = Dispose(dispose)
                managers[resourceId] = boardOnTheFlyingDutchman()
            }

            return this
        }

        fun open(): ResourceGuild<T, R> = guild

        @Suppress("UNCHECKED_CAST")
        private fun boardOnTheFlyingDutchman(): ResourceManager<T, R> = Ghost as ResourceManager<T, R>
    }
}
