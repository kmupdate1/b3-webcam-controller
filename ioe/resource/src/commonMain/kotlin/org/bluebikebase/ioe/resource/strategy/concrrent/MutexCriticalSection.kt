package org.bluebikebase.ioe.resource.strategy.concrrent

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MutexCriticalSection : CriticalSection {
    override suspend fun <T> run(block: suspend () -> T): T = mutex.withLock { block.invoke() }

    private val mutex = Mutex()
}
