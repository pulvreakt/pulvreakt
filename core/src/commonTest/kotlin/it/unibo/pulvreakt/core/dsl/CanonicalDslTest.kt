package it.unibo.pulvreakt.core.dsl

import io.kotest.core.spec.style.FreeSpec
import it.unibo.pulvreakt.dsl.pulverization

class CanonicalDslTest : FreeSpec({
    "The DSL should allow to create a logic device by specifying its components' logic in-place" {
        val configuration = pulverization {
            val device by logicDevice<Int, Unit, Unit, Unit> {
                withBehavior { _, _, _, _ ->
                    TODO()
                } // requires { "C1" and "C2"}
            }
        }
    }
})
