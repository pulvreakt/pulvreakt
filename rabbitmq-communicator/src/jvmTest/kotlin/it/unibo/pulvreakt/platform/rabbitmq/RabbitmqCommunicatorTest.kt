package it.unibo.pulvreakt.platform.rabbitmq

import io.kotest.core.spec.style.FreeSpec

class RabbitmqCommunicatorTest : FreeSpec() {
    init {
        "A rabbitmq-based communicator" - {
            "should connect to the broker".config(enabled = false) {
                error("Not yet implemented")
//                shouldNotThrowUnit<Exception> {
//                    PulverizationKoinModule.koinApp = koinApplication { modules(module) }
//                    val communicator = RabbitmqCommunicator()
//                    val remotePlaceProvider = defaultRabbitMQRemotePlace()
//                    communicator.setup(State to Behaviour, remotePlaceProvider[Behaviour])
//                }
            }
            "should communicate with another communicator".config(enabled = false) {
                error("Not yet implemented")
//                PulverizationKoinModule.koinApp = koinApplication { modules(module) }
//                val stateComm = RabbitmqCommunicator()
//                val behaviourComm = RabbitmqCommunicator()
//                val remotePlaceProvider = defaultRabbitMQRemotePlace()
//                stateComm.setup(State to Behaviour, remotePlaceProvider[Behaviour])
//                behaviourComm.setup(Behaviour to State, remotePlaceProvider[State])
//                val stateJob = launch {
//                    stateComm.fireMessage("hello".toByteArray())
//                }
//                val behaviourJob = launch {
//                    behaviourComm.receiveMessage().first() shouldBe "hello".toByteArray()
//                }
//
//                stateJob.join()
//                behaviourJob.join()
            }
        }
    }
}
