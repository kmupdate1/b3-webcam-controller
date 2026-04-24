package org.bluebikebase.wcc.domain.camera.agreement

import org.bluebikebase.core.algebra.Vector
import org.bluebikebase.core.geometry.ScalarDRange
import org.bluebikebase.wcc.domain.camera.entity.Camera

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
    fun zoom(target: Camera, velocity: Vector)

    /**
     * ズームを停止する
     */
    fun stopZoom(target: Camera) = zoom(target, Vector.STATIONARY)
}
