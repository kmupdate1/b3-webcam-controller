package org.bluebikebase.ioe.resource.vessel

import org.bluebikebase.core.foundation.Identity

data class Ghost(val resourceId: Identity) : Ship<Nothing, Unit> {
    override suspend fun operate(block: suspend (Nothing) -> Unit) =
        println("Captain:\nWelcome to The Flying Dutchman |~.~|\n" +
                "Do you afraid of DEAD? To vanish into the sea, becoming flotsam and jetsam...\n")

    override suspend fun reject() {
        TODO("Not yet implemented")
    }
}
