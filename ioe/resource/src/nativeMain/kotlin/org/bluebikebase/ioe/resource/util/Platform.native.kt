package org.bluebikebase.ioe.resource.util

import co.touchlab.stately.collections.IsoMutableSet

actual fun <T> createConcurrentSet(): MutableSet<T> = IsoMutableSet()
