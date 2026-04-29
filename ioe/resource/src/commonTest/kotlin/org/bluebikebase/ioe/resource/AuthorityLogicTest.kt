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

                val minute = SECOND * 60

                when (val rand = Random.nextInt(3)) {
                    0 -> try {
                        withContext(MopeSailor) {
                            withTimeout(minute) {
                                authority.dispatch {
                                    val ship = authority.welcomeToSailor()
                                    val value = ship.operate { sensor ->
                                        delay(Random.nextLong(10, 1500).milliseconds)

                                        sensor.measure(30L).also {
                                            println("[Job $i] \"${MopeSailor::class.simpleName}\"センサが \'${it.value.toInt()}\' を観測")
                                        }
                                    }

                                    Result.success(value)
                                }
                            }
                        }
                    } catch (e: TimeoutCancellationException) {
                        println("[Job $i] ${MopeSailor::class.simpleName}: あまりに長い！リジェクトして帰ります。: ${e.message}")
                        Result.failure(e)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Result.failure(e)
                    }

                    1 -> try {
                        withContext(MihoPirate) {
                            withTimeout(minute * 3) {
                                authority.dispatch {
                                    val ship = authority.welcomeToPirate()
                                    val value = ship.operate { sensor ->
                                        delay(Random.nextLong(10, 1500).milliseconds)

                                        sensor.measure(30L).also {
                                            println("[Job $i] \"${MihoPirate::class.simpleName}\"センサが \'${it.value.toInt()}\' を観測")
                                        }
                                    }

                                    Result.success(value)
                                }
                            }
                        }
                    } catch (e: TimeoutCancellationException) {
                        println("[Job $i] ${MihoPirate::class.simpleName}: あまりに長い！リジェクトして帰ります。: ${e.message}")
                        Result.failure(e)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Result.failure(e)
                    }


                    else -> try {
                        withContext(KenSailor) {
                            withTimeout(minute / 20) {
                                authority.dispatch {
                                    val ship = authority.welcomeToSailor()
                                    val value = ship.operate { sensor ->
                                        delay(Random.nextLong(10, 1500).milliseconds)

                                        sensor.measure(30L).also {
                                            println("[Job $i] \"${MopeSailor::class.simpleName}\"センサが \'${it.value.toInt()}\' を観測")
                                        }
                                    }

                                    Result.success(value)
                                }
                            }
                        }
                    } catch (e: TimeoutCancellationException) {
                        println("[Job $i] ${KenSailor::class.simpleName}: あまりに長い！リジェクトして帰ります。: ${e.message}")
                        Result.failure(e)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Result.failure(e)
                    }
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
            dressType = MopeSailor.dressType,
        )
        .reserve<MihoPirate>(
            establish = { HyperSoySensor().also { println("🏴‍☠️ Miho: Hyper Sensor Active!") } },
            cleanup = { println("--- Sensor[HYPER] terminating... ---") },
            dispose = { println("---  Sensor[HYPER] powered off   ---") },
            dressType = MihoPirate.dressType,
        )
        .reserve<KenSailor>(
            establish = { RandomSoySensor().also { println("🛳️ Ken: Random Fleet Sensor Deployed.") } },
            cleanup = { println("--- Sensor[RANDOM] terminating... ---") },
            dispose = { println("---  Sensor[RANDOM] powered off   ---") },
            dressType = KenSailor.dressType,
        )
        .applicate()

    companion object {
        val SECOND = 1_000.milliseconds
    }
}
