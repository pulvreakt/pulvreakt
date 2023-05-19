package it.unibo.pulvreakt.core

import io.kotest.assertions.failure
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import it.unibo.pulvreakt.component.Context
import it.unibo.pulvreakt.core.SensorsFixtures.MySensor1
import it.unibo.pulvreakt.core.SensorsFixtures.MySensor2
import org.koin.core.Koin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

class SensorsContainerTest : FreeSpec(), KoinTest {
    override fun getKoin(): Koin = koinApplication {
        module {
            single {
                object : Context {
                    override val deviceID: String = "test"
                }
            }
        }
    }.koin

    init {
        "SensorsContainer test" - {
            "To the container can be added multiple sensors at once" {
                val container = object : SensorsContainer() {
                    override val context: Context by inject()
                }.apply {
                    addAll(
                        MySensor1(),
                        MySensor1(),
                        MySensor2(),
                    )
                }
                container.getAll<MySensor1>().size shouldBe 2
                container.getAll<MySensor2>().size shouldBe 1
            }
            "To the container can be added a single sensor" {
                val container = object : SensorsContainer() {
                    override val context: Context by inject()
                }
                container += MySensor1()
                container.get<MySensor1>() shouldNotBe null
                container.getAll<MySensor1>().size shouldBe 1
            }
            "The container can be queried using the KClass" {
                val container = object : SensorsContainer() {
                    override val context: Context by inject()
                }.apply {
                    addAll(
                        MySensor1(),
                        MySensor1(),
                        MySensor2(),
                    )
                }
                container[MySensor2::class] shouldNotBe null
                container.getAll(MySensor1::class).size shouldBe 2
            }
            "The container, when queried, should return the sensor in the lambda" {
                val sensor2 = MySensor2()
                val container = object : SensorsContainer() {
                    override val context: Context by inject()
                }.apply { addAll(MySensor1(), MySensor1(), sensor2) }
                container.get<MySensor2> { this shouldBeSameInstanceAs sensor2 }
                container.getAll<MySensor1> { size shouldBe 2 }
            }
            "The container, when queried with an invalid class, should not execute the lambda" {
                val container = object : SensorsContainer() {
                    override val context: Context by inject()
                }
                container.get<MySensor1> {
                    failure("This lambda should not be executed since no ${MySensor1::class} is available")
                }
            }
        }
    }
}
