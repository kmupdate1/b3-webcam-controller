package org.bluebikebase.ioe.resource

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.bluebikebase.core.foundation.ScalarL
import org.bluebikebase.ioe.resource.authority.AuthorityApplicable
import org.bluebikebase.ioe.resource.authority.context.VesselDress
import org.bluebikebase.ioe.resource.domain.HyperSoySensor
import org.bluebikebase.ioe.resource.domain.NormalSoySensor
import org.bluebikebase.ioe.resource.domain.RandomSoySensor
import org.bluebikebase.ioe.resource.domain.VirtualSoySensor
import org.bluebikebase.ioe.resource.dresses.KenSailor
import org.bluebikebase.ioe.resource.dresses.MihoPirate
import org.bluebikebase.ioe.resource.dresses.MopePirate
import kotlin.random.Random
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class AuthorityLogicTest {
    @Test
    fun `random access logic test`() = runBlocking {
        println()
        val jobs = (1..10).map { i ->
            async {
                // 1. バラバラのタイミングで現れるゲスト
                delay(Random.nextLong(0, 500).milliseconds)

                // ランダムにドレスを選択（Mope, Miho, Ken のいずれか）
                val dress = when (Random.nextInt(3)) {
                    0 -> MopePirate
                    1 -> MihoPirate
                    else -> KenSailor
                }

                try {
                    // 2. このドレス（Context）を着て港へ向かう
                    withContext(dress) {
                        // 3. 20秒でタイムアウト
                        withTimeout(20_000.milliseconds) {
                            val ship = authority.welcomeToShip()
                            val result = authority.dispatch {
                                val value = ship.operate { sensor ->
                                    // 4. 物理的なゆらぎ（計測に時間がかかる）
                                    delay(Random.nextLong(100, 1500).milliseconds)
                                    sensor.measure(30L).also {
                                        println("[Job $i] \"${dress::class.simpleName}\"センサが \'${it.value.toInt()}\' を観測")
                                    }
                                }

                                Result.success(value)
                            }

                            Result.success(result)
                        }
                    }
                } catch (e: TimeoutCancellationException) {
                    println("[Job $i] ${dress::class.simpleName}: あまりに長い！リジェクトして帰ります。")
                    Result.failure(e)
                } catch (e: Exception) {
                    println(e.message + "\n")
                    Result.failure(e)
                }
            }
        }

        val results = jobs.awaitAll()
        val successCount = results.count { it.isSuccess }
        println("--- 実験終了レポート ---")
        println("総ゲスト数: ${jobs.size}, 成功数: $successCount, 離脱数: ${jobs.size - successCount}\n")
    }


    // 予約票を港湾管理局に提出する
    private val authority = AuthorityApplicable<VirtualSoySensor, ScalarL>()
        .reserve<MopePirate>(
            establish = { NormalSoySensor().also { println("⚓️ Mope: Normal Sensor Ready.") } },
            cleanup = { println("--- Sensor[NORMAL] terminating... ---") },
            dispose = { println("---  Sensor[NORMAL] powered off   ---") },
        )
        .reserve<MihoPirate>(
            establish = { HyperSoySensor().also { println("🏴‍☠️ Miho: Hyper Sensor Active!") } },
            cleanup = { println("--- Sensor[HYPER] terminating... ---") },
            dispose = { println("---  Sensor[HYPER] powered off   ---") },
        )
        .reserve<KenSailor>(
            establish = { RandomSoySensor().also { println("🛳️ Ken: Random Fleet Sensor Deployed.") } },
            cleanup = { println("--- Sensor[RANDOM] terminating... ---") },
            dispose = { println("---  Sensor[RANDOM] powered off   ---") },
        )
        .applicate()
}
