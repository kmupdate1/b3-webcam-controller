package org.bluebikebase.ioe.resource.authority

import org.bluebikebase.ioe.resource.vessel.Ship

interface Reception<T, R> {
    suspend fun welcomeToShip(): Ship<T, R>
}
