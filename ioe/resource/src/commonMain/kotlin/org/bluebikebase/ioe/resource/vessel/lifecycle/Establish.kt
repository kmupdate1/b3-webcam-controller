package org.bluebikebase.ioe.resource.vessel.lifecycle

internal fun interface Establish<T> { suspend operator fun invoke(): T }
