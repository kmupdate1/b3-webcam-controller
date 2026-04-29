package org.bluebikebase.ioe.resource.authority

import org.bluebikebase.core.identity.UniqueID
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose
import org.bluebikebase.ioe.resource.vessel.lifecycle.Establish

@PublishedApi
internal data class Registry<T, R>(
    val establishes: MutableMap<UniqueID, Establish<T>>,
    val cleanups: MutableMap<UniqueID, Cleanup<T>>,
    val disposes: MutableMap<UniqueID, Dispose<T>>,
)
