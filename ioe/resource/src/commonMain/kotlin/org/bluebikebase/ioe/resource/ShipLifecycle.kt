package org.bluebikebase.ioe.resource

sealed interface ShipLifecycle {
    object Dead: ShipLifecycle
    object Preparing: ShipLifecycle
    object Idling: ShipLifecycle
    object Sailing: ShipLifecycle
    object Retiring: ShipLifecycle
}
