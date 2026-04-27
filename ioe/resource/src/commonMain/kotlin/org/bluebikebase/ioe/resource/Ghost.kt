package org.bluebikebase.ioe.resource

object Ghost : ResourceManager<Nothing, Unit> {
    override suspend fun drive(block: suspend (Nothing) -> Unit) =
        println("Captain: Welcome to The Flying Dutchman |~.~| " +
                "To vanish into the sea, becoming flotsam and jetsam...")
}
