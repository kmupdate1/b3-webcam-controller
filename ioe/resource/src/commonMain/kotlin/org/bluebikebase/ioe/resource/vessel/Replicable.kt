package org.bluebikebase.ioe.resource.vessel

internal interface Replicable<in T, out R> { fun replicate(): Ship<T, R> }
