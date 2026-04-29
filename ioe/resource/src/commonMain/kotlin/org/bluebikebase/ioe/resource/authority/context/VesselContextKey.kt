package org.bluebikebase.ioe.resource.authority.context

import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

class VesselContextKey<E : VesselDress>(
    val klass: KClass<E>,
) : CoroutineContext.Key<E>
