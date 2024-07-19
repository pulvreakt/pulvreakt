package it.unibo.pulvreakt.dsl

import arrow.core.right
import io.kotest.core.spec.style.FreeSpec

class PulverizationDslTest : FreeSpec({
    "The pulverization dsl should allow to define an application with a component inlined" {
        val configuration = pulverization {
            val simpleComponent by component<Int, Int> {
                2.right()
            }.noRequirements()
            val s by component<Int, Int> {
                2.right()
            } requires { }
        }
    }
})
