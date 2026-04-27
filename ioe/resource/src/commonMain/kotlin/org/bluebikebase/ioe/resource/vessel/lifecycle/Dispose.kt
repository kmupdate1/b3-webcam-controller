package org.bluebikebase.ioe.resource.vessel.lifecycle

internal fun interface Dispose<T> { suspend operator fun invoke(resource: T) }
