package org.bluebikebase.ioe.resource.vessel

interface Ship<in T, out R> {
    suspend fun operate(block: suspend (@UnsafeVariance T) -> @UnsafeVariance R): R
    suspend fun reject()
    suspend fun terminate()
}
