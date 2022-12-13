package it.nicolasfarabegoli.pulverization.platform.rabbitmq

import io.kotest.assertions.throwables.shouldNotThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.core.BehaviourComponent
import it.nicolasfarabegoli.pulverization.core.StateComponent
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
                    communicator.setup(StateComponent to BehaviourComponent, remotePlaceProvider[BehaviourComponent])
                }
            }
            "should communicate with another communicator".config(enabled = false) {
                PulverizationKoinModule.koinApp = koinApplication { modules(module) }
                val stateComm = RabbitmqCommunicator()
                val behaviourComm = RabbitmqCommunicator()
                val remotePlaceProvider = defaultRabbitMQRemotePlace()
                stateComm.setup(StateComponent to BehaviourComponent, remotePlaceProvider[BehaviourComponent])
                behaviourComm.setup(BehaviourComponent to StateComponent, remotePlaceProvider[StateComponent])
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
