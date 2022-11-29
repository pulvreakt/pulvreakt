package it.nicolasfarabegoli.pulverization.runtime.communication

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.types.shouldBeSameInstanceAs

class LocalCommunicatorManagerTest : ShouldSpec(
    {
        context("The local communicator manager") {
            should("return the same instance for the same communication type") {
                val stateInstance1 = LocalCommunicatorManager.stateInstance
                val stateInstance2 = LocalCommunicatorManager.stateInstance
                stateInstance1 shouldBeSameInstanceAs stateInstance2

                val actuatorsInstance1 = LocalCommunicatorManager.actuatorsInstance
                val actuatorsInstance2 = LocalCommunicatorManager.actuatorsInstance
                actuatorsInstance1 shouldBeSameInstanceAs actuatorsInstance2

                val communicationInstance1 = LocalCommunicatorManager.communicationInstance
                val communicationInstance2 = LocalCommunicatorManager.communicationInstance
                communicationInstance1 shouldBeSameInstanceAs communicationInstance2

                val sensorsInstance1 = LocalCommunicatorManager.sensorsInstance
                val sensorsInstance2 = LocalCommunicatorManager.sensorsInstance
                sensorsInstance1 shouldBeSameInstanceAs sensorsInstance2
            }
        }
    },
)
