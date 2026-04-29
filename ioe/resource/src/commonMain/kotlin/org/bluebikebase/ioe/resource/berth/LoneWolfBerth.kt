package org.bluebikebase.ioe.resource.berth

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.bluebikebase.ioe.resource.foundation.Container
import org.bluebikebase.ioe.resource.vessel.LoneWolf
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose
import org.bluebikebase.ioe.resource.vessel.lifecycle.Establish

@PublishedApi
internal class LoneWolfBerth<T, R> {
    suspend fun invite(establish: Establish<T>, cleanup: Cleanup<T>, dispose: Dispose<T>): LoneWolf<T, R> =
        berthOrder.withLock {
            activeWolf ?: run {
                val resource = establish.invoke()
                LoneWolf<T, R>(Container(resource), cleanup, dispose)
                    .also { activeWolf = it }
            }
        }

    suspend fun terminate() = activeWolf?.terminate()
    suspend fun reject() = activeWolf?.reject()

    private var activeWolf: LoneWolf<T, R>? = null
    private val berthOrder = Mutex()
}
