package org.bluebikebase.ioe.resource

@ConsistentCopyVisibility
data class Cleanup<T> internal constructor(val function: suspend (T) -> Unit)
