package org.bluebikebase.ioe.resource.strategy

import org.bluebikebase.ioe.resource.foundation.Container
import org.bluebikebase.ioe.resource.vessel.LoneWolf
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose

@PublishedApi
internal class ClassicalLoneWolfVendor<T, R>(
    override val wolves: MutableSet<LoneWolf<T, R>>,
) : LoneWolfShipVendor<T, R> {
    override suspend fun vend(container: Container<T>, cleanup: Cleanup<T>, dispose: Dispose<T>): LoneWolf<T, R> =
        wolves.firstOrNull() ?: LoneWolf<T, R>(container, cleanup, dispose)
            .also { wolves += it }
}
