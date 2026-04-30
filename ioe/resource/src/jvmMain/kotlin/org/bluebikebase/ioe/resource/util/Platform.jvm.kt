package org.bluebikebase.ioe.resource.util

import java.util.concurrent.ConcurrentHashMap

actual fun <T> createConcurrentSet(): MutableSet<T> = ConcurrentHashMap.newKeySet()
