package org.bluebikebase.wcc.usecase.camera

import org.bluebikebase.core.algebra.Vector
import org.bluebikebase.wcc.domain.camera.agreement.Motion
import org.bluebikebase.wcc.domain.camera.agreement.Zoom

class CameraControlUseCase(
    private val motion: Motion,
    private val zoom: Zoom,
) {
    /**
     * ジョイスティックの生入力をベクトルに変換
     * カメラを動作させる
     * @param p 水平方向の値をベクトルで受け取る
     * @param t 垂直方向の値をベクトルで受け取る
     * @param z ズームの値をベクトルで受け取る
     */
    operator fun invoke(
        p: Vector = Vector.STATIONARY,
        t: Vector = Vector.STATIONARY,
        z: Vector = Vector.STATIONARY,
    ) {
        motion.move(horizontal = p, vertical = t)
        zoom.zoom(velocity = z)
    }
}
