package org.bluebikebase.ioe.resource

object Ghost : ResourceManager<Nothing, Unit> {
    override suspend fun use(block: suspend (Nothing) -> Unit) =
        println("Captain: Welcome to The Flying Dutchman |-.-|")

    override suspend fun suspend() = println("To vanish into the sea, becoming flotsam and jetsam...")
}
