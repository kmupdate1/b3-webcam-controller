package org.bluebikebase.ioe.resource.strategy

import org.bluebikebase.ioe.resource.vessel.LoneWolf
import org.bluebikebase.ioe.resource.vessel.lifecycle.Establish

internal class ClassicalLoneWolfVendor<T, R> : LoneWolfShipVendor<T, R> {
    override fun vend(ships: MutableSet<LoneWolf<T, R>>, establish: Establish<T>): LoneWolf<T, R> {
        TODO("Not yet implemented")
    }
}
