package org.bluebikebase.ioe.resource.vessel.lifecycle

internal fun interface Cleanup<T> { suspend operator fun invoke(resource: T) }
