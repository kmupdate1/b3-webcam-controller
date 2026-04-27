package org.bluebikebase.ioe.resource

internal fun interface Cleanup<T> { suspend operator fun invoke(resource: T) }
