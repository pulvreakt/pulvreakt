package it.unibo.pulvreakt.modularization.dsl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.modularization.fixtures.ProducerModule
import it.unibo.pulvreakt.modularization.fixtures.SensorModule
import it.unibo.pulvreakt.modularization.fixtures.SqrtModule
import it.unibo.pulvreakt.modularization.fixtures.SumModule

class ModularizationDslTest : FreeSpec(
    {
        "The modularization DSL" - {
            "should allows to bind two modules" {
                val config = modularization {
                    SensorModule() connectedTo SqrtModule()
                }
                config shouldBe mapOf(
                    SensorModule() to setOf(SqrtModule()),
                )
            }
            "should allows to bind multiple modules as input of another module" {
                val config = modularization {
                    SqrtModule() and SensorModule() connectedTo SqrtModule()
                }
                config shouldBe mapOf(
                    SqrtModule() to setOf(SqrtModule()),
                    SensorModule() to setOf(SqrtModule()),
                )
            }
            "should allows to bind a module as input of multiple modules" {
                val config = modularization {
                    SensorModule() connectedTo (SqrtModule() andIn SumModule())
                }
                config shouldBe mapOf(
                    SensorModule() to setOf(SqrtModule(), SumModule()),
                )
            }
            "should allows to bind multiple modules as input of multiple modules" {
                val config = modularization {
                    ProducerModule() and SensorModule() connectedTo (SqrtModule() andIn SumModule())
                }
                config shouldBe mapOf(
                    ProducerModule() to setOf(SqrtModule(), SumModule()),
                    SensorModule() to setOf(SqrtModule(), SumModule()),
                )
            }
        }
    },
)
