package org.bluebikebase.ioe.resource.error

class B3IoeResourceNotFoundException(
    override val message: String = "Resource not found",
    override val cause: Throwable? = null,
) : B3IoeResourceException(message = message, cause = cause)
