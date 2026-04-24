package org.bluebikebase.wcc.domain.error

import org.bluebikebase.core.error.B3InvalidValidationException

class B3WCCInvalidValidationException(
    override val message: String?,
    override val cause: Throwable? = null,
) : B3InvalidValidationException(message = message, cause = cause)
