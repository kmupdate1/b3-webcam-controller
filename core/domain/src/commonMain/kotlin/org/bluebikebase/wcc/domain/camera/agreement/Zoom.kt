package org.bluebikebase.wcc.domain.camera.agreement

import org.bluebikebase.core.algebra.Vector
import org.bluebikebase.core.geometry.ScalarDRange

interface Zoom {
    /**
     * ズームの倍率の器
     */
    val zoomRange: ScalarDRange

    /**
     * @param velocity ズームのベクトル
     *  - Positive: Tale (望遠・寄る)
     *  - Negative: Wide（広角・引く）
     *  - STATIONARY: 停止
     */
    fun zoom(velocity: Vector)

    /**
     * ズームを停止する
     */
    fun stop() = zoom(Vector.STATIONARY)
}
