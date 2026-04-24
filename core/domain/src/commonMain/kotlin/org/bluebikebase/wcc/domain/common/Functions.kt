package org.bluebikebase.wcc.domain.common

import org.bluebikebase.wcc.domain.error.B3WCCInvalidValidationException

@Throws(B3WCCInvalidValidationException::class)
inline fun validate(
    input: String?,
    lazyMessage: () -> String? = { null },
    validate: (String) -> Boolean = { true },
): String {
    if (input.isNullOrBlank())
        throw B3WCCInvalidValidationException(message = "Input cannot be null or empty")

    val isValid = validate.invoke(input)
    if (!isValid)
        throw B3WCCInvalidValidationException(message = lazyMessage.invoke())

    return input
}
