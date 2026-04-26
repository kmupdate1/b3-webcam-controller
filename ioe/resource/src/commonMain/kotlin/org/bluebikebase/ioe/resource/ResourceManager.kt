package org.bluebikebase.ioe.resource

interface ResourceManager<in T, out R> {
    suspend fun use(block: suspend (@UnsafeVariance T) -> @UnsafeVariance R): R
    suspend fun suspend()
}
