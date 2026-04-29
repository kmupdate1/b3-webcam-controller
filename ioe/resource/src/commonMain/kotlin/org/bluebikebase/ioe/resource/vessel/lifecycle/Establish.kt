package org.bluebikebase.ioe.resource.vessel.lifecycle

@PublishedApi
internal fun interface Establish<T> { suspend operator fun invoke(): T }
