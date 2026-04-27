package org.bluebikebase.ioe.resource.authority

import org.bluebikebase.core.foundation.Identity
import org.bluebikebase.ioe.resource.vessel.Ship

interface Reception<T, R> {
    suspend fun welcomeTo(destinationId: Identity): Ship<T, R>
}
