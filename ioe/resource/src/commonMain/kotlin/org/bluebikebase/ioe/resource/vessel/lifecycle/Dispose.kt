package org.bluebikebase.ioe.resource.vessel.lifecycle

fun interface Dispose<T> { suspend operator fun invoke(resource: T) }
