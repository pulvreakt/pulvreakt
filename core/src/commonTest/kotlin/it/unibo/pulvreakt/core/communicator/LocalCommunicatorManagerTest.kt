package it.unibo.pulvreakt.core.communicator

import io.kotest.core.spec.style.FreeSpec

class LocalCommunicatorManagerTest : FreeSpec({
    "The LocalCommunicatorManager" - {
        "should create a communicator" {
            val manager = LocalCommunicatorManager()
            manager.getLocalCommunicator("component1", "component2")
        }
    }
})
