package org.bluebikebase.wcc.domain.util

import java.security.MessageDigest

actual fun generateHash(seed: ByteArray): ByteArray =
    MessageDigest.getInstance(HashAlgorithms.SHA256).digest(seed)

object HashAlgorithms {
    const val SHA256 = "SHA-256"
}
