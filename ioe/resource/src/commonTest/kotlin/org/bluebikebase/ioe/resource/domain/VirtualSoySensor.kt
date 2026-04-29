package org.bluebikebase.ioe.resource.domain

import org.bluebikebase.core.foundation.ScalarL

interface VirtualSoySensor { fun measure(expect: Long): ScalarL }

// 実体A：真面目な仮想センサー
class NormalSoySensor : VirtualSoySensor {
    override fun measure(expect: Long) = ScalarL.of(expect)
}

// 実体B：常に倍の値を出す、ちょっと調子乗りなセンサー
class HyperSoySensor : VirtualSoySensor {
    override fun measure(expect: Long) = ScalarL.of(expect * 2)
}

// 実体C：乱数を混ぜる、気まぐれなセンサー
class RandomSoySensor : VirtualSoySensor {
    override fun measure(expect: Long) = ScalarL.of(expect + (0..10).random())
}
