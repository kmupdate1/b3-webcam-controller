package org.bluebikebase.ioe.resource

@ConsistentCopyVisibility
data class Recipe<T> internal constructor(val function: suspend () -> T)
