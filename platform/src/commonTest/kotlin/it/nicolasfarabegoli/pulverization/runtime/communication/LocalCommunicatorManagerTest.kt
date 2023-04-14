package it.nicolasfarabegoli.pulverization.runtime.communication

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.types.shouldBeSameInstanceAs
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.KoinTest

class LocalCommunicatorManagerTest : FreeSpec(), KoinTest {
    private val module = module { single { CommManager() } }

    init {
        "The local communicator manager" - {
            "should return the same instance for the same communication type" {
                PulverizationKoinModule.koinApp = koinApplication { modules(module) }
                val commManager: CommManager = PulverizationKoinModule.koinApp?.koin?.get()!!

                val stateInstance1 = commManager.stateInstance
                val stateInstance2 = commManager.stateInstance
                stateInstance1 shouldBeSameInstanceAs stateInstance2

                val actuatorsInstance1 = commManager.actuatorsInstance
                val actuatorsInstance2 = commManager.actuatorsInstance
                actuatorsInstance1 shouldBeSameInstanceAs actuatorsInstance2

                val communicationInstance1 = commManager.communicationInstance
                val communicationInstance2 = commManager.communicationInstance
                communicationInstance1 shouldBeSameInstanceAs communicationInstance2

                val sensorsInstance1 = commManager.sensorsInstance
                val sensorsInstance2 = commManager.sensorsInstance
                sensorsInstance1 shouldBeSameInstanceAs sensorsInstance2
            }
        }
    }
}
