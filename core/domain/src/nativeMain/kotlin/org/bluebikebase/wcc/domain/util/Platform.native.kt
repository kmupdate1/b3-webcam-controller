package org.bluebikebase.wcc.domain.util

import org.kotlincrypto.hash.sha2.SHA256

actual fun generateHash(seed: ByteArray): ByteArray = SHA256().digest(seed)
