package org.bluebikebase.ioe.resource.authority.context

import org.bluebikebase.core.identity.B3Hash
import org.bluebikebase.core.identity.UniqueID
import org.bluebikebase.ioe.resource.error.B3IoeIllegalResourceException
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

abstract class VesselPirateDress(
    val purpose: String = "PIRATE",
    override val key: CoroutineContext.Key<VesselPirateDress> = VesselContextKey(VesselPirateDress::class),
) : AbstractCoroutineContextElement(key), VesselDress {
    override val destinationId: UniqueID get() {
        val kName = this::class.qualifiedName
            ?: throw B3IoeIllegalResourceException("Anonymous dress is not allowed in Authority.")
        val bytes = kName.encodeToByteArray()

        return B3Hash.fromBytes(bytes)
    }
}
