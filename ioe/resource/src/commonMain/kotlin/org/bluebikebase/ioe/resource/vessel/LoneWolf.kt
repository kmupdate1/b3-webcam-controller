package org.bluebikebase.ioe.resource.vessel

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose
import org.bluebikebase.ioe.resource.foundation.Container
import org.bluebikebase.ioe.resource.error.B3IoeIllegalResourceException

class LoneWolf<T, R> internal constructor(
    private val container: Container<T>,
    private val cleanup: Cleanup<T>,
    private val dispose: Dispose<T>,
) : Ship<T, R> {
    override suspend fun drive(block: suspend (T) -> R): R = boardingOrder.withLock {
        userJob = currentCoroutineContext().job

        try { block.invoke(container.resource) }
        finally { cleanup(container.resource); userJob = null }
    }

    override suspend fun reject() = boardingOrder.withLock {
        try { userJob?.cancel() ?: throw B3IoeIllegalResourceException(message = "NaN job") }
        finally { cleanup(container.resource); userJob = null }
    }

    internal suspend fun dispose() = boardingOrder.withLock {
        if (userJob != null) Unit
        dispose(container.resource)
    }

    private var userJob: Job? = null
    private val boardingOrder = Mutex()
}
