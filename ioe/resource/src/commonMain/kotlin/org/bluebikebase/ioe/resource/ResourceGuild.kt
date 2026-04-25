package org.bluebikebase.ioe.resource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class ResourceGuild<T>(
    private val scope: CoroutineScope,
    private val recipe: suspend () -> T,
    private val cleanup: (T) -> Unit,
    private val duration: Duration = 10_000.milliseconds,
) {
    suspend fun <R> use(block: suspend () -> R): R {
        TODO("Not yet implemented")
    }

    suspend fun prepare() = recipe

    private val currentBox: ResourceContainer<T>? = null
    private val locker = Mutex()
    private val cleanupJob: Job? = null
}
