package org.bluebikebase.ioe.resource

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.bluebikebase.core.foundation.ScalarL
import org.bluebikebase.ioe.resource.authority.AuthorityBuilder
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
    /*
    @Test
    fun `harbor logic test`() = runTest {
        val shipA = withContext(currentCoroutineContext()) { authority.welcomeTo(Identity.fromString(uuid1.toString())) }
        val shipB = withContext(currentCoroutineContext()) { authority.welcomeTo(Identity.fromString(uuid1.toString())) }
        val shipC = withContext(currentCoroutineContext()) { authority.welcomeTo(Identity.fromString(uuid1.toString())) }
        val shipD = withContext(currentCoroutineContext()) { authority.welcomeTo(Identity.fromString(uuid1.toString())) }
        val shipE = withContext(currentCoroutineContext()) { authority.welcomeTo(Identity.fromString(uuid1.toString())) }

        val transactionA = authority.dispatch {
            val value = shipA.operate { sensor ->
                sensor.measure(100L).also {
                    println("Value: $it times")
                }
            }

            Result.success(value)
        }
        val transactionB = authority.dispatch {
            val value = shipB.operate { sensor ->
                sensor.measure(100L).also {
                    println("Value: $it times")
                }
            }

            Result.success(value)
        }
        val transactionC = authority.dispatch {
            val value = shipC.operate { sensor ->
                sensor.measure(100L).also {
                    println("Value: $it times")
                }
            }

            Result.success(value)
        }
        val transactionD = authority.dispatch {
            val value = shipD.operate { sensor ->
                sensor.measure(100L).also {
                    println("Value: $it times")
                }
            }

            Result.success(value)
        }
        val transactionE = authority.dispatch {
            val value = shipE.operate { sensor ->
                sensor.measure(100L).also {
                    println("Value: $it times")
                }
            }

            Result.success(value)
        }

        println("Voyage:")
        println("TransactionA: ${transactionA.getOrNull()}")
        println("TransactionB: ${transactionB.getOrNull()}")
        println("TransactionC: ${transactionC.getOrNull()}")
        println("TransactionD: ${transactionD.getOrNull()}")
        println("TransactionE: ${transactionE.getOrNull()}")
    }
    */

    /*
    @Test
    fun `harbor logic concurrent test`() = runBlocking { //runTest {
        println("--- Start: Concurrent Voyage Test ---")

        // 1. 同時に5つの異なる場所からアクセスが来たと仮定する
        val jobs = (1..5).map { i ->
            async {
                // 受付を通る（最初の一人だけが実体化をキックし、他は待機するはず）
                val shipA = authority.welcomeTo(Identity.fromString(uuid1.toString()))
                val shipB = authority.welcomeTo(Identity.fromString(uuid2.toString()))
                val shipC = authority.welcomeTo(Identity.fromString(uuid3.toString()))

                // 出航命令を出す
                authority.run {
                    dispatch {
                        val value = shipA.operate { sensor ->
                            println("[Job $i] センサー操作開始（ハッチ内）...")

                            println("[Job $i] 物理操作の時間をシミュレート (runTest 内なので delay は実際には 500ms 待たず、仮想時間を進める)")
                            delay(5_000)

                            sensor.measure(3_000L).also { println("[Job $i] 計測完了: $it") }
                        }

                        Result.success(value)
                    }
                    dispatch {
                        val value = shipB.operate { sensor ->
                            println("[Job $i] センサー操作開始（ハッチ内）...")

                            println("[Job $i] 物理操作の時間をシミュレート (runTest 内なので delay は実際には 500ms 待たず、仮想時間を進める)")
                            delay(500)

                            sensor.measure(100L).also { println("[Job $i] 計測完了: $it") }
                        }

                        Result.success(value)
                    }
                    dispatch {
                        val value = shipC.operate { sensor ->
                            println("[Job $i] センサー操作開始（ハッチ内）...")

                            println("[Job $i] 物理操作の時間をシミュレート (runTest 内なので delay は実際には 500ms 待たず、仮想時間を進める)")
                            delay(5_000)

                            sensor.measure(30L).also { println("[Job $i] 計測完了: $it") }
                        }

                        Result.success(value)
                    }
                }
            }
        }

        // 2. 全員の帰還を待つ
        val results = jobs.awaitAll()

        // 3. 全ての結果が成功しており、順序が守られていたか確認
        results.forEachIndexed { index, result ->
            println("Result ${index + 1}: ${result.getOrNull()}")
        }



        println("--- End: Concurrent Voyage Test ---")
    }
    */

    /*
    @Test
    fun `random access logic test`() = runTest {
        val jobs = (1..20).map { i ->
            async {
                // 1. バラバラのタイミングで現れるゲスト
                delay(Random.nextLong(0, 1000).milliseconds)

                try {
                    // 2. せっかちなゲスト（2秒でリジェクト）
                    withTimeout(2000.milliseconds) {
                        val ship = authority.welcomeTo()
                        authority.dispatch {
                            val value = ship.operate { sensor ->
                                // 3. 物理的なゆらぎ
                                delay(Random.nextLong(100, 3000).milliseconds)
                                sensor.measure(30L)
                            }

                            Result.success(value)
                        }
                    }
                } catch (e: TimeoutCancellationException) {
                    println("[Job $i] あまりに長い！リジェクトして帰ります。")
                    Result.failure(e)
                }
            }
        }
    }
    */

    @Test
    fun `random access logic test`() = runTest {
        val jobs = (1..20).map { i ->
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
                        // 3. せっかちなゲスト（2秒でタイムアウト）
                        withTimeout(2000.milliseconds) {
                            val ship = authority.welcomeToShip()
                            val result = authority.dispatch {
                                val value = ship.operate { sensor ->
                                    // 4. 物理的なゆらぎ（計測に時間がかかる）
                                    delay(Random.nextLong(100, 1500).milliseconds)
                                    sensor.measure(30L).also {
                                        println("[Job $i] ${dress::class.simpleName} が ${it}L を計測しました。")
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
                    Result.failure(e)
                }
            }
        }

        val results = jobs.awaitAll()
        val successCount = results.count { it.isSuccess }
        println("\n--- 実験終了レポート ---")
        println("総ゲスト数: 20, 成功数: $successCount, 離脱数: ${20 - successCount}")
    }

    private val authority = AuthorityBuilder<VirtualSoySensor, ScalarL>()
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
        .build()
}
