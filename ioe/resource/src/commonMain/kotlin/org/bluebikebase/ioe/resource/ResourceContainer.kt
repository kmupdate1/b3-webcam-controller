package org.bluebikebase.ioe.resource

import org.bluebikebase.core.foundation.Identity

internal class ResourceContainer<T>(
    val resource: T,
    val cleanup: suspend (T) -> Unit,
    val resourceIdentity: Identity = Identity.gen(),
) {
    suspend fun dispose() = cleanup(resource)
}
