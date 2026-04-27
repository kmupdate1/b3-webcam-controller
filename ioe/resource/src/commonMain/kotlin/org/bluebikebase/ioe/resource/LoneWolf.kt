package org.bluebikebase.ioe.resource

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.bluebikebase.ioe.resource.error.B3IoeIllegalResourceException

class LoneWolf<T, R> internal constructor(
    private val container: ResourceContainer<T>,
    private val cleanup: Cleanup<T>,
    private val dispose: Dispose<T>,
) : ResourceManager<T, R> {
    override suspend fun drive(block: suspend (T) -> R): R = mutex.withLock {
        userJob = currentCoroutineContext().job
        try { block.invoke(container.resource) }
        finally { cleanup.function(container.resource); userJob = null }
    }

    internal suspend fun suspend() =
        try { userJob?.cancel() ?: throw B3IoeIllegalResourceException(message = "NaN job") }
        finally { dispose.function(container.resource) }

    private var userJob: Job? = null
    private val mutex = Mutex()
}
