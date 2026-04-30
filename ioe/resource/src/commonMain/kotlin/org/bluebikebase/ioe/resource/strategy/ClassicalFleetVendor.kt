package org.bluebikebase.ioe.resource.strategy

import org.bluebikebase.core.foundation.ScalarL
import org.bluebikebase.ioe.resource.foundation.Container
import org.bluebikebase.ioe.resource.strategy.concrrent.CriticalSection
import org.bluebikebase.ioe.resource.strategy.concrrent.MutexCriticalSection
import org.bluebikebase.ioe.resource.util.createConcurrentSet
import org.bluebikebase.ioe.resource.vessel.Fleet
import org.bluebikebase.ioe.resource.vessel.lifecycle.Cleanup
import org.bluebikebase.ioe.resource.vessel.lifecycle.Dispose

class ClassicalFleetVendor<T, R>(
    private val scaleLimit: Long = 10L,
) : FleetShipVendor<T, R> {
    override suspend fun vend(container: Container<T>, cleanup: Cleanup<T>, dispose: Dispose<T>): Fleet<T, R> =
        section.run {
            val snapshot = _fleets.toList()
            val masterFleet = snapshot.firstOrNull()

            masterFleet?.let {
                val limit = ScalarL.of(scaleLimit)
                if (_fleets.size < limit.value) it.replicate() else it
            }?.also { _fleets.add(it) }
                ?: Fleet<T, R>(container, cleanup, dispose).also { _fleets.add(it) }
        }

    override val fleets: Set<Fleet<T, R>> get() = _fleets
    private val _fleets = createConcurrentSet<Fleet<T, R>>()

    private val section: CriticalSection = MutexCriticalSection()
}
