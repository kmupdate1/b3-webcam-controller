package org.bluebikebase.wcc.domain.camera

import org.bluebikebase.core.quantity.HeartBeat
import org.bluebikebase.wcc.domain.common.validate
import org.bluebikebase.wcc.domain.error.B3WCCInvalidValidationException
import org.bluebikebase.wcc.domain.util.generateHash
import kotlin.io.encoding.Base64
import kotlin.jvm.JvmInline

@JvmInline
value class CameraName private constructor(val name: String) {
    companion object {
        private const val DELIMITER = '@'

        @Throws(B3WCCInvalidValidationException::class)
        fun of(input: String?): CameraName  {
            val row = validate(input = input, lazyMessage = { "Usage symbol only '-' or '_'" }) {
                it.all { c -> c.isLetterOrDigit() || c == '_' || c == '-' || c.isWhitespace() }
            }
                .trim()
                .replace(Regex("\\s*-\\s*"), "-")
                .replace(Regex("\\s*_\\s*"), "_")
                .replace(Regex("\\s+"), "-")
                .lowercase()

            val hash = generateHash(seed = (row + HeartBeat.now()).encodeToByteArray())
            val suffix = Base64.UrlSafe.encode(hash.take(10).toByteArray())
                .replace("=", "")

            val finalName = row + DELIMITER + suffix
            val valid = validate(input = finalName, lazyMessage = { "Invalid format : [$finalName]" }) {
                it.all { c -> c.isLetterOrDigit() || c == '_' || c == '-' || c == DELIMITER }
            }

            return CameraName(name = valid)
        }
    }

    val simpleName get() = name.substringBefore(DELIMITER)
}
