package org.bluebikebase.ioe.resource.vessel

internal interface Replicable<T, R> { fun replicate(): Fleet<T, R> }
