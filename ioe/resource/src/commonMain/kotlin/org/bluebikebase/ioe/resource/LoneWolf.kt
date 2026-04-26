package org.bluebikebase.ioe.resource

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.bluebikebase.ioe.resource.error.B3IoeIllegalResourceException

class LoneWolf<T, R> internal constructor(
    private val container: ResourceContainer<T>,
    private val emergency: suspend () -> Unit,
) : ResourceManager<T, R> {
    override suspend fun use(block: suspend (T) -> R): R = mutex.withLock {
        userJob = currentCoroutineContext().job
        try { block.invoke(container.resource) }
        finally { userJob = null }
    }

    override suspend fun suspend() =
        try { userJob?.cancel() ?: throw B3IoeIllegalResourceException(message = "NaN job") }
        finally { emergency.invoke() }

    private var userJob: Job? = null
    private val mutex = Mutex()
}
