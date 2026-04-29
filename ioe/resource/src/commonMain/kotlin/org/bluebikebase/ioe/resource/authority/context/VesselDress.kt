package org.bluebikebase.ioe.resource.authority.context

import org.bluebikebase.core.identity.UniqueID
import kotlin.coroutines.CoroutineContext

interface VesselDress : CoroutineContext.Element {
    companion object Key : CoroutineContext.Key<VesselDress>
    override val key: CoroutineContext.Key<*> get() = Key

    val destinationId: UniqueID
}
