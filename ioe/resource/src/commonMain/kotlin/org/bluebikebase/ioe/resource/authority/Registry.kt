package org.bluebikebase.ioe.resource.authority

import org.bluebikebase.core.foundation.Identity
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose
import org.bluebikebase.ioe.resource.vessel.lifecycle.Establish

internal data class Registry<T, R>(
    val establishes: MutableMap<Identity, Establish<T>>,
    val cleanups: MutableMap<Identity, Cleanup<T>>,
    val disposes: MutableMap<Identity, Dispose<T>>,
)
