package org.bluebikebase.wcc.domain.error

import org.bluebikebase.core.error.B3Exception

abstract class B3WCCException(
    override val message: String?,
    override val cause: Throwable?,
) : B3Exception(message = message, cause = cause)
