package org.bluebikebase.ioe.resource

interface ManagerTransaction {
    suspend fun execute(): Result<ManagerLifecycle>
}
