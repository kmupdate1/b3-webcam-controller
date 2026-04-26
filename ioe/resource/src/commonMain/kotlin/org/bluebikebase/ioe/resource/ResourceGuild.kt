package org.bluebikebase.ioe.resource

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.bluebikebase.core.foundation.Identity
import org.bluebikebase.ioe.resource.error.B3IoeResourceNotFoundException

class ResourceGuild<T, R> private constructor() : Reception<T, R> {
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

    private val idOfTheFlyingDutchman = Identity.gen()
    private val withSingle: MutableMap<Identity, Boolean> = mutableMapOf(idOfTheFlyingDutchman to true)
    private val recipes: MutableMap<Identity, suspend () -> T> =
        mutableMapOf(idOfTheFlyingDutchman to buildTheFlyingDutchman())
    private val disposes: MutableMap<Identity, suspend () -> Unit> =
        mutableMapOf(idOfTheFlyingDutchman to {})
    private val managers: MutableMap<Identity, ResourceManager<T, R>> =
        mutableMapOf(idOfTheFlyingDutchman to boardOnTheFlyingDutchman())

    @Suppress("UNCHECKED_CAST")
    private fun buildTheFlyingDutchman(): suspend () -> T = { null as T }
    @Suppress("UNCHECKED_CAST")
    private fun boardOnTheFlyingDutchman(): ResourceManager<T, R> = Ghost as ResourceManager<T, R>

    /**
     * ギルド（シングルトンを想定）を開店するための足掛かり
     */
    class Builder<T, R> {
        private val guild: ResourceGuild<T, R> = ResourceGuild()

        fun set(uuid: String, isSingle: Boolean = false, recipe: suspend () -> T, dispose: suspend () -> Unit): Builder<T, R> {
            guild.apply {
                val resourceId = Identity.fromString(uuid)
                withSingle[resourceId] = isSingle
                recipes[resourceId] = recipe
                disposes[resourceId] = dispose
            }

            return this
        }

        fun open(): ResourceGuild<T, R> = guild
    }
}
