package org.bluebikebase.ioe.resource.strategy.concrrent

class NoOpCriticalSection : CriticalSection {
    override suspend fun <T> run(block: suspend () -> T): T = block.invoke()
}
