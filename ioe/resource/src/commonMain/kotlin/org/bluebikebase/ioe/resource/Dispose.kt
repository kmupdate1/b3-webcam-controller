package org.bluebikebase.ioe.resource

@ConsistentCopyVisibility
data class Dispose<T> internal constructor(val function: suspend (T) -> Unit)
