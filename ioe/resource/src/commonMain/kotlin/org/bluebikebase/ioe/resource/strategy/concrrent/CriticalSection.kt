package org.bluebikebase.ioe.resource.strategy.concrrent

interface CriticalSection {
    suspend fun <T> run(block: suspend () -> T): T
}
