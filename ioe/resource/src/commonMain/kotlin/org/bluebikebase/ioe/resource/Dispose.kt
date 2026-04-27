package org.bluebikebase.ioe.resource

internal fun interface Dispose<T> { suspend operator fun invoke(resource: T) }
