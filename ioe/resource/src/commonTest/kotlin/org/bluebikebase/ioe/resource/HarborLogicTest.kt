package org.bluebikebase.ioe.resource

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.bluebikebase.core.foundation.Identity
import org.bluebikebase.core.foundation.ScalarL
import org.bluebikebase.ioe.resource.authority.AuthorityBuilder
import org.bluebikebase.ioe.resource.domain.VirtualSoySensor
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class HarborLogicTest {
    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `harbor logic test`() = runTest {
        val uuid = Uuid.random()
        val authority = AuthorityBuilder<VirtualSoySensor, ScalarL>()
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
}
