package org.bluebikebase.ioe.resource

interface Ship<in T, out R> {
    suspend fun drive(block: suspend (@UnsafeVariance T) -> @UnsafeVariance R): R
    suspend fun reject()
}
