package org.bluebikebase.ioe.resource

internal fun interface Recipe<T> { suspend operator fun invoke(): T }
