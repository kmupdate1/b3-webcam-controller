package org.bluebikebase.ioe.resource

@ConsistentCopyVisibility
data class Container<T> internal constructor(val resource: T)
