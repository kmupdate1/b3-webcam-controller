package org.bluebikebase.ioe.resource

import org.bluebikebase.core.foundation.Identity

internal data class Registry<T, R>(
    val withSingle: MutableMap<Identity, Boolean>,
    val recipes: MutableMap<Identity, Recipe<T>>,
    val cleanups: MutableMap<Identity, Cleanup<T>>,
    val disposes: MutableMap<Identity, Dispose<T>>,
)
