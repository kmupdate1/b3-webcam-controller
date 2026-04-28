package org.bluebikebase.ioe.resource.domain

import org.bluebikebase.core.foundation.ScalarL

class VirtualSoySensor {
    fun measure(): ScalarL = ScalarL.of(5_000L)
}
