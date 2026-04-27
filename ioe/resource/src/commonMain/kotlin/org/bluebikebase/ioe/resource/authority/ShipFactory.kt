package org.bluebikebase.ioe.resource.authority

import org.bluebikebase.ioe.resource.vessel.Ship

internal fun interface ShipFactory<T, R> { suspend fun create(): Ship<T, R> }
