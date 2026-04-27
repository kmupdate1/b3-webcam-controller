package org.bluebikebase.ioe.resource.vessel.lifecycle

sealed interface ShipLifecycle {
    object Dead: ShipLifecycle
    object Preparing: ShipLifecycle
    object Idling: ShipLifecycle
    object Sailing: ShipLifecycle
    object Retiring: ShipLifecycle
}
