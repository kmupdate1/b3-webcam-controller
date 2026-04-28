package org.bluebikebase.ioe.resource.transaction

fun interface ShipTransaction<R> { suspend fun execute(): Result<R> }
