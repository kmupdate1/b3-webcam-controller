package org.bluebikebase.ioe.resource.authority

import org.bluebikebase.ioe.resource.vessel.Fleet
import org.bluebikebase.ioe.resource.vessel.LoneWolf

interface Reception<T, R> {
    suspend fun welcomeToPirate(): LoneWolf<T, R>
    suspend fun welcomeToSailor(): Fleet<T, R>
}
