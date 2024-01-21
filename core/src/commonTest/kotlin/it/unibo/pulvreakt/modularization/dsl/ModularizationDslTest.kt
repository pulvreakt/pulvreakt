package it.unibo.pulvreakt.modularization.dsl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import it.unibo.pulvreakt.modularization.api.module.module
import it.unibo.pulvreakt.modularization.fixtures.ProducerModule
import it.unibo.pulvreakt.modularization.fixtures.SensorModule
import it.unibo.pulvreakt.modularization.fixtures.SqrtModule
import it.unibo.pulvreakt.modularization.fixtures.SumModule

class ModularizationDslTest : FreeSpec({
    "The modularization DSL" - {
        "should allows to bind two modules" {
            val configResult = modularization(SensorModule(), SqrtModule()) {
                module<SensorModule>() wiredTo module<SqrtModule>()
            }
            configResult.isRight() shouldBe true
            // TODO make validation assertions
        }
        "should allows to bind multiple modules as input of another module" {
            val configResult = modularization(SqrtModule(), SensorModule(), SqrtModule()) {
                module<SqrtModule>() and module<SensorModule>() wiredTo module<SqrtModule>()
            }
            configResult.isRight() shouldBe true
            // TODO make validation assertions
        }
        "should allows to bind a module as input of multiple modules" {
            val configResult = modularization(SensorModule(), SqrtModule(), SumModule()) {
                module<SensorModule>() wiredTo (module<SqrtModule>() and module<SumModule>())
            }
            configResult.isRight() shouldBe true
            // TODO make validation assertions
        }
        "should allows to bind multiple modules as input of multiple modules" {
            val configResult = modularization(ProducerModule(), SensorModule(), SqrtModule(), SumModule()) {
                module<ProducerModule>() and module<SensorModule>() wiredTo (module<SqrtModule>() and module<SumModule>())
            }
            configResult.isRight() shouldBe true
        }
    }
})
