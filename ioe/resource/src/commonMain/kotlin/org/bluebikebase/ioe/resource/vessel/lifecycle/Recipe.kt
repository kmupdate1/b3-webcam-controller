package org.bluebikebase.ioe.resource.vessel.lifecycle

internal fun interface Recipe<T> { suspend operator fun invoke(): T }
