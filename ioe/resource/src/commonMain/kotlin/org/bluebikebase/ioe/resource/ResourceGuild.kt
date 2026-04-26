package org.bluebikebase.ioe.resource

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.bluebikebase.core.foundation.Identity
import org.bluebikebase.ioe.resource.error.B3IoeResourceNotFoundException

class ResourceGuild<T, R> private constructor(
    private val withSingle: MutableMap<Identity, Boolean>,
    private val recipes: MutableMap<Identity, suspend () -> T>,
    private val disposes: MutableMap<Identity, suspend () -> Unit>,
    private val managers: MutableMap<Identity, ResourceManager<T, R>>,
) : Reception<T, R> {
    override suspend fun inviteTo(resourceId: Identity): ResourceManager<T, R> = managers[resourceId] ?: Mutex().run {
        withLock {
            val resource = recipes.getValue(resourceId).invoke()
            val dispose = disposes.getValue(resourceId)
            val container = ResourceContainer(resource)

            if (withSingle.getValue(resourceId))
                LoneWolf<T, R>(container = container, emergency = dispose) as ResourceManager<T, R>
            else
                Fleet<T, R>(container = container, emergency = dispose) as ResourceManager<T, R>
        }
    }

    internal suspend fun prepare(resourceId: Identity): T = recipes[resourceId]?.invoke()
        ?: throw B3IoeResourceNotFoundException()
    internal suspend fun dispose(resourceId: Identity) = disposes[resourceId]?.invoke()
        ?: throw B3IoeResourceNotFoundException()

    /**
     * ギルド（シングルトンを想定）を開店するための足掛かり
     */
    class Builder<T, R> private constructor() {
        private val guild: ResourceGuild<T, R> = ResourceGuild(
            withSingle = mutableMapOf(),
            recipes = mutableMapOf(),
            disposes = mutableMapOf(),
            managers = mutableMapOf(),
        )

        fun set(
            strUuid: String, isSingle: Boolean = false,
            recipe: suspend () -> T,
            dispose: suspend () -> Unit,
        ): Builder<T, R> {
            guild.apply {
                val resourceId = Identity.fromString(strUuid)

                withSingle[resourceId] = isSingle
                recipes[resourceId] = recipe
                disposes[resourceId] = dispose
                managers[resourceId] = boardOnTheFlyingDutchman()
            }

            return this
        }

        fun open(): ResourceGuild<T, R> = guild

        @Suppress("UNCHECKED_CAST")
        private fun boardOnTheFlyingDutchman(): ResourceManager<T, R> = Ghost as ResourceManager<T, R>
    }
}
