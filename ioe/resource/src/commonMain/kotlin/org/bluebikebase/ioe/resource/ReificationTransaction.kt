package org.bluebikebase.ioe.resource

class ReificationTransaction<T, R>(
    private val manager: ResourceManager<T, R>,
) : ManagerTransaction {
    override suspend fun execute(): Result<ManagerLifecycle> {
        TODO("Not yet implemented")
    }
}
