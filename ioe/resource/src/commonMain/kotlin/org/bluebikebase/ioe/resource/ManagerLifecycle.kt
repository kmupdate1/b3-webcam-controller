package org.bluebikebase.ioe.resource

sealed interface ManagerLifecycle {
    object Dead: ManagerLifecycle
    object Preparing: ManagerLifecycle
    object Idling: ManagerLifecycle
    object Sailing: ManagerLifecycle
    object Retiring: ManagerLifecycle
}
