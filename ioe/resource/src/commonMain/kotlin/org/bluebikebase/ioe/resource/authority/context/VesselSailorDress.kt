package org.bluebikebase.ioe.resource.authority.context

import org.bluebikebase.core.identity.B3Hash
import org.bluebikebase.core.identity.UniqueID
import org.bluebikebase.ioe.resource.error.B3IoeIllegalResourceException
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.reflect.KClass

abstract class VesselSailorDress(
    override val dressType: DressType = DressType.SAILOR,
) : AbstractCoroutineContextElement(VesselDress.Key), VesselDress {
    companion object {
        fun createIdFromKlass(klass: KClass<out VesselSailorDress>): UniqueID {
            val name = klass.qualifiedName
                ?: throw B3IoeIllegalResourceException("Unknown class name: $klass")

            return B3Hash.fromBytes(name.encodeToByteArray())
        }
    }
}
