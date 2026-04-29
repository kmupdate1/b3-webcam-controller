package org.bluebikebase.ioe.resource.authority.context

import org.bluebikebase.core.identity.B3Hash
import org.bluebikebase.core.identity.UniqueID
import org.bluebikebase.ioe.resource.error.B3IoeIllegalResourceException
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.reflect.KClass

abstract class VesselPirateDress : AbstractCoroutineContextElement(VesselDress.Key), VesselDress {
    companion object {
        fun createIdFromKlass(klass: KClass<out VesselPirateDress>): UniqueID {
            val name = klass.qualifiedName
                ?: throw B3IoeIllegalResourceException("Unknown class name: $klass")

            return B3Hash.fromBytes(name.encodeToByteArray())
        }
    }
}
