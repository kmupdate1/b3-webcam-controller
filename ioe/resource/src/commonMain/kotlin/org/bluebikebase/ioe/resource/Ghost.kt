package org.bluebikebase.ioe.resource

import org.bluebikebase.core.foundation.Identity

data class Ghost(val resourceId: Identity) : Ship<Nothing, Unit> {
    override suspend fun drive(block: suspend (Nothing) -> Unit) =
        println("Captain: Welcome to The Flying Dutchman |~.~| " +
                "To vanish into the sea, becoming flotsam and jetsam...")

    override suspend fun reject() {
        TODO("Not yet implemented")
    }
}
