package org.bluebikebase.ioe.resource.vessel

import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.job
import org.bluebikebase.ioe.resource.error.B3IoeIllegalResourceException
import org.bluebikebase.ioe.resource.foundation.Container
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose

data class LoneWolf<T, R>(
    private val container: Container<T>,
    private val cleanup: Cleanup<T>,
    private val dispose: Dispose<T>,
) {
    suspend fun operate(block: suspend (T) -> R): R {
        userJob = currentCoroutineContext().job

        return try {
            block.invoke(container.resource)
        } finally {
            cleanup(container.resource); userJob = null
        }
    }

    suspend fun reject() = try {
        userJob?.cancel()
            ?: throw B3IoeIllegalResourceException(message = "NaN job")
    } finally {
        cleanup(container.resource); userJob = null
    }

    suspend fun terminate() {
        if (userJob != null)
            throw B3IoeIllegalResourceException(message = "Cannot terminate while a guest is still on board")

        dispose(container.resource)
    }

    private var userJob: Job? = null
}
