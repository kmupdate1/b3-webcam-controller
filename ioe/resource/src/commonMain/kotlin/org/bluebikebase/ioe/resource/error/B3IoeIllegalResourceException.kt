package org.bluebikebase.ioe.resource.error

class B3IoeIllegalResourceException(
    override val message: String?,
    override val cause: Throwable? = null,
) : B3IoeResourceException(message = message, cause = cause)
