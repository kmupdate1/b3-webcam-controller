package org.bluebikebase.ioe.resource.strategy

import org.bluebikebase.ioe.resource.foundation.Container
import org.bluebikebase.ioe.resource.vessel.LoneWolf
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose

internal interface LoneWolfShipVendor<T, R> {
    val wolves: MutableSet<LoneWolf<T, R>>
    suspend fun vend(container: Container<T>, cleanup: Cleanup<T>, dispose: Dispose<T>): LoneWolf<T, R>
}
