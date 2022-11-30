package it.nicolasfarabegoli.pulverization.runtime.communication

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.types.shouldBeSameInstanceAs

class LocalCommunicatorManagerTest : ShouldSpec(
    {
        context("The local communicator manager") {
            should("return the same instance for the same communication type") {
                val stateInstance1 = CommManager.stateInstance
                val stateInstance2 = CommManager.stateInstance
                stateInstance1 shouldBeSameInstanceAs stateInstance2

                val actuatorsInstance1 = CommManager.actuatorsInstance
                val actuatorsInstance2 = CommManager.actuatorsInstance
                actuatorsInstance1 shouldBeSameInstanceAs actuatorsInstance2

                val communicationInstance1 = CommManager.communicationInstance
                val communicationInstance2 = CommManager.communicationInstance
                communicationInstance1 shouldBeSameInstanceAs communicationInstance2

                val sensorsInstance1 = CommManager.sensorsInstance
                val sensorsInstance2 = CommManager.sensorsInstance
                sensorsInstance1 shouldBeSameInstanceAs sensorsInstance2
            }
        }
    },
)
