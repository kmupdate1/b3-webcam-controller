package org.bluebikebase.ioe.resource

import kotlinx.coroutines.*
import org.bluebikebase.core.foundation.ScalarL
import org.bluebikebase.ioe.resource.authority.AuthorityApplicable
import org.bluebikebase.ioe.resource.domain.HyperSoySensor
import org.bluebikebase.ioe.resource.domain.NormalSoySensor
import org.bluebikebase.ioe.resource.domain.RandomSoySensor
import org.bluebikebase.ioe.resource.domain.VirtualSoySensor
import org.bluebikebase.ioe.resource.dresses.KenSailor
import org.bluebikebase.ioe.resource.dresses.MihoPirate
import org.bluebikebase.ioe.resource.dresses.MihoSailor
import org.bluebikebase.ioe.resource.dresses.MopePirate
import org.bluebikebase.ioe.resource.dresses.MopeSailor
import kotlin.random.Random
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class AuthorityLogicTest {
    @Test
    fun `random access logic test`() = runBlocking {
        println()
        val jobs = (1..200).map { i ->
            async {
                // 1. バラバラのタイミングで現れるゲスト
                delay(Random.nextLong(500, 5_000).milliseconds)

                val block = withTimeout(1_000.milliseconds * 60 * 60 * 24) {
                    val result = authority.dispatch {
                        val value = ship.operate { sensor ->
                            // 4. 物理的なゆらぎ（計測に時間がかかる）
                            delay(Random.nextLong(10, 1500).milliseconds)
                            sensor.measure(30L).also {
                                println("[Job $i] \"${dress::class.simpleName}\"センサが \'${it.value.toInt()}\' を観測")
                            }
                        }

                        Result.success(value)
                    }

                    Result.success(result)
                }


                val dress = when (Random.nextInt(3)) {
                    0 -> withContext(MopeSailor) {
                        val ship = authority.welcomeToSailor()
                    }

                    1 -> withContext(MihoPirate) {
                        val ship = authority.welcomeToPirate()
                    }

                    else -> withContext(KenSailor) {
                        val ship = authority.welcomeToPirate()
                    }
                }

                try {
                    // 2. このドレス（Context）を着て港へ向かう
                    withContext(dress) {
                        // 3. 24h でタイムアウト
                    }
                } catch (e: TimeoutCancellationException) {
                    println("[Job $i] ${dress::class.simpleName}: あまりに長い！リジェクトして帰ります。")
                    Result.failure(e)
                } catch (e: Exception) {
                    println(e.message)
                    Result.failure(e)
                }
            }
        }

        val results = jobs.awaitAll()
        val successCount = results.count { it.isSuccess }

        println("\n--- 実験終了レポート ---")
        println("総ゲスト数: ${jobs.size}, 成功数: $successCount, 離脱数: ${jobs.size - successCount}\n")
    }


    // 予約票を港湾管理局に提出する
    private val authority = AuthorityApplicable<VirtualSoySensor, ScalarL>()
        .reserve<MopeSailor>(
            establish = { NormalSoySensor().also { println("⚓️ Mope: Normal Sensor Ready.") } },
            cleanup = { println("--- Sensor[NORMAL] terminating... ---") },
            dispose = { println("---  Sensor[NORMAL] powered off   ---") },
        )
        .reserve<MihoSailor>(
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
