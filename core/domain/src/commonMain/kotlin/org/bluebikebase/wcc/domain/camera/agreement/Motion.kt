package org.bluebikebase.wcc.domain.camera.agreement

import org.bluebikebase.core.algebra.Vector
import org.bluebikebase.wcc.domain.camera.entity.Camera

interface Motion {
    /**
     * @param horizontal 水平方向のベクトル（Vector.STATIONARYで停止）
     * @param vertical 垂直方向のベクトル（Vector.STATIONARYで停止）
     */
    suspend fun move(target: Camera, horizontal: Vector, vertical: Vector)

    /**
     * 全ての軸を安全に停止させる
     */
    suspend fun stopMotion(target: Camera) = move(target, Vector.STATIONARY, Vector.STATIONARY)
}
