package org.bluebikebase.ioe.resource

@ConsistentCopyVisibility
data class ResourceContainer<T> internal constructor(val resource: T)
