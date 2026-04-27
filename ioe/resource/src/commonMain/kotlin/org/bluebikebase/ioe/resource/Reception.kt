package org.bluebikebase.ioe.resource

import org.bluebikebase.core.foundation.Identity

interface Reception<T, R> {
    suspend fun inviteTo(destinationId: Identity): Ship<T, R>
}
