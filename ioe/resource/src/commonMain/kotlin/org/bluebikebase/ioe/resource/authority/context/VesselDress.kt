package org.bluebikebase.ioe.resource.authority.context

import kotlin.coroutines.CoroutineContext

interface VesselDress : CoroutineContext.Element {
    companion object Key : CoroutineContext.Key<VesselDress>
}
