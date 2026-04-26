package org.bluebikebase.ioe.resource

import kotlinx.coroutines.Job

class Fleet<T, R> internal constructor(
    private val container: ResourceContainer<T>,
    private val emergency: suspend () -> Unit,
) : ResourceManager<T, R> {
    override suspend fun use(block: suspend (T) -> R): R {
        TODO("Not yet implemented")
    }

    override suspend fun suspend() {
        TODO("Not yet implemented")
    }

    private var userJob: Job? = null
}
