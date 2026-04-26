package org.bluebikebase.ioe.resource.error

import org.bluebikebase.core.error.B3Exception

abstract class B3IoeResourceException(
    override val message: String?,
    override val cause: Throwable? = null,
) : B3Exception(message = message, cause = cause)
