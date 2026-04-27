package org.bluebikebase.ioe.resource.vessel.lifecycle

fun interface Cleanup<T> { suspend operator fun invoke(resource: T) }
