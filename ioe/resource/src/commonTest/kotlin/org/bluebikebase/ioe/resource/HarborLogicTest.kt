package org.bluebikebase.ioe.resource

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.bluebikebase.core.foundation.Identity
import org.bluebikebase.core.foundation.ScalarL
import org.bluebikebase.ioe.resource.authority.AuthorityBuilder
import org.bluebikebase.ioe.resource.domain.VirtualSoySensor
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class HarborLogicTest {
    @Test
    fun `harbor logic test`() = runTest {
        val shipA = withContext(currentCoroutineContext()) { authority.welcomeTo(Identity.fromString(uuid.toString())) }
        val shipB = withContext(currentCoroutineContext()) { authority.welcomeTo(Identity.fromString(uuid.toString())) }
        val shipC = withContext(currentCoroutineContext()) { authority.welcomeTo(Identity.fromString(uuid.toString())) }
        val shipD = withContext(currentCoroutineContext()) { authority.welcomeTo(Identity.fromString(uuid.toString())) }
        val shipE = withContext(currentCoroutineContext()) { authority.welcomeTo(Identity.fromString(uuid.toString())) }

        val transactionA = authority.dispatch {
            val value = shipA.operate { sensor ->
                sensor.measure().also {
                    println("Value: $it times")
                }
            }

            Result.success(value)
        }
        val transactionB = authority.dispatch {
            val value = shipB.operate { sensor ->
                sensor.measure().also {
                    println("Value: $it times")
                }
            }

            Result.success(value)
        }
        val transactionC = authority.dispatch {
            val value = shipC.operate { sensor ->
                sensor.measure().also {
                    println("Value: $it times")
                }
            }

            Result.success(value)
        }
        val transactionD = authority.dispatch {
            val value = shipD.operate { sensor ->
                sensor.measure().also {
                    println("Value: $it times")
                }
            }

            Result.success(value)
        }
        val transactionE = authority.dispatch {
            val value = shipE.operate { sensor ->
                sensor.measure().also {
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

    @Test
    fun `harbor logic concurrent test`() = runBlocking { //runTest {
        println("--- Start: Concurrent Voyage Test ---")

        // 1. 同時に5つの異なる場所からアクセスが来たと仮定する
        val jobs = (1..5).map { i ->
            async {
                // 受付を通る（最初の一人だけが実体化をキックし、他は待機するはず）
                val ship = authority.welcomeTo(Identity.fromString(uuid.toString()))

                // 出航命令を出す
                authority.dispatch {
                    val value = ship.operate { sensor ->
                        println("[Job $i] センサー操作開始（ハッチ内）...")

                        println("[Job $i] 物理操作の時間をシミュレート (runTest 内なので delay は実際には 500ms 待たず、仮想時間を進める)")
                        delay(5_000)

                        sensor.measure().also { println("[Job $i] 計測完了: $it") }
                    }

                    Result.success(value)
                }
            }
        }

        // 2. 全員の帰還を待つ
        val results = jobs.awaitAll()

        // 3. 全ての結果が成功しており、順序が守られていたか確認
        results.forEachIndexed { index, result ->
            println("Result ${index + 1}: ${result.getOrNull()}")
            // assert(result.isSuccess)
        }

        println("--- End: Concurrent Voyage Test ---")
    }
    private val uuid = Uuid.random()
    private val authority = AuthorityBuilder<VirtualSoySensor, ScalarL>()
        .register(
            strUuid = uuid.toString(),
            isSingle = true,
            establish = {
                println("--- Sensor establishing... ---")
                VirtualSoySensor()
            },
            cleanup = { println("--- Sensor terminating... ---") },
            dispose = { println("---   Sensor power off    ---") },
        )
        .build()

}
