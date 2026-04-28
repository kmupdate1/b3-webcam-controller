package org.bluebikebase.ioe.resource.domain

import org.bluebikebase.core.foundation.ScalarL

class VirtualSoySensor {
    fun measure(expect: Long): ScalarL = ScalarL.of(expect)
}
