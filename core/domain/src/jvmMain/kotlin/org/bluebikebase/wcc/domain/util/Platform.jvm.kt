package org.bluebikebase.wcc.domain.util

import java.security.MessageDigest

actual fun generateHash(seed: ByteArray): ByteArray =
    MessageDigest.getInstance("SHA-1").digest(seed)
