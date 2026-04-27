package org.bluebikebase.ioe.resource

import kotlinx.coroutines.Job

class Fleet<T, R> internal constructor(
    private val container: ResourceContainer<T>,
    private val cleanup: Cleanup<T>,
    private val dispose: Dispose<T>,
) : ResourceManager<T, R> {
    override suspend fun drive(block: suspend (T) -> R): R {
        TODO("Not yet implemented")
    }

    private var userJob: Job? = null
}
