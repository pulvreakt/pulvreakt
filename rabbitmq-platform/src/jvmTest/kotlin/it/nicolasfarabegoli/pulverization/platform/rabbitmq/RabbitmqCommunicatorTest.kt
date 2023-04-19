package it.nicolasfarabegoli.pulverization.platform.rabbitmq

import io.kotest.assertions.throwables.shouldNotThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.dsl.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.model.State
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.RabbitmqCommunicator
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.defaultRabbitMQRemotePlace
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import java.lang.Exception

class RabbitmqCommunicatorTest : FreeSpec() {
    private val module = module {
        single<Context> {
            object : Context {
                override val deviceID: String = "1"
            }
        }
    }

    init {
        "A rabbitmq-based communicator" - {
            "should connect to the broker".config(enabled = false) {
                shouldNotThrowUnit<Exception> {
                    PulverizationKoinModule.koinApp = koinApplication { modules(module) }
                    val communicator = RabbitmqCommunicator()
                    val remotePlaceProvider = defaultRabbitMQRemotePlace()
                    communicator.setup(State to Behaviour, remotePlaceProvider[Behaviour])
                }
            }
            "should communicate with another communicator".config(enabled = false) {
                PulverizationKoinModule.koinApp = koinApplication { modules(module) }
                val stateComm = RabbitmqCommunicator()
                val behaviourComm = RabbitmqCommunicator()
                val remotePlaceProvider = defaultRabbitMQRemotePlace()
                stateComm.setup(State to Behaviour, remotePlaceProvider[Behaviour])
                behaviourComm.setup(Behaviour to State, remotePlaceProvider[State])
                val stateJob = launch {
                    stateComm.fireMessage("hello".toByteArray())
                }
                val behaviourJob = launch {
                    behaviourComm.receiveMessage().first() shouldBe "hello".toByteArray()
                }

                stateJob.join()
                behaviourJob.join()
            }
        }
    }
}
