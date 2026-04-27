package org.bluebikebase.ioe.resource.authority

import org.bluebikebase.core.foundation.Identity
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose
import org.bluebikebase.ioe.resource.vessel.lifecycle.Recipe

internal data class Registry<T, R>(
    val withSingle: MutableMap<Identity, Boolean>,
    val recipes: MutableMap<Identity, Recipe<T>>,
    val cleanups: MutableMap<Identity, Cleanup<T>>,
    val disposes: MutableMap<Identity, Dispose<T>>,
)
