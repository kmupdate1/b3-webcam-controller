package org.bluebikebase.wcc.domain.camera.agreement

import org.bluebikebase.core.algebra.Vector
import org.bluebikebase.core.geometry.ScalarDRange

interface Motion {
    /**
     * カメラの動作範囲の器
     */
    val horizontalLimit: ScalarDRange
    val verticalLimit: ScalarDRange

    /**
     * @param horizontal 水平方向のベクトル（Vector.STATIONARYで停止）
     * @param vertical 垂直方向のベクトル（Vector.STATIONARYで停止）
     */
    fun move(horizontal: Vector, vertical: Vector)

    /**
     * 全ての軸を安全に停止させる
     */
    fun stopMotion() = move(Vector.STATIONARY, Vector.STATIONARY)
}
