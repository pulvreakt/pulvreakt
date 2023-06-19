package it.nicolasfarabegoli.pulverization.platforms.mqtt

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import it.nicolasfarabegoli.pulverization.dsl.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.model.State
import it.nicolasfarabegoli.pulverization.runtime.context.ExecutionContext
import it.nicolasfarabegoli.pulverization.runtime.dsl.model.Host
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.koin.dsl.koinApplication
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
class MqttCommunicatorTest : StringSpec({
    coroutineTestScope = true
    val module = module {
        single<ExecutionContext> {
            object : ExecutionContext {
                override val host: Host
                    get() = TODO("Not yet implemented")
                override val deviceID: String = "1"
            }
        }
    }
    "The communicator must connect to the broker".config(enabled = false) {
        shouldNotThrow<Exception> {
            PulverizationKoinModule.koinApp = koinApplication { modules(module) }
            val communicator = MqttCommunicator("192.168.8.1", 1883)
            communicator.setup(State to Behaviour, defaultMqttRemotePlace()[Behaviour])
            communicator.finalize()
        }
    }
    "The communicator should communicate with another one".config(enabled = false) {
        shouldNotThrow<Exception> {
            PulverizationKoinModule.koinApp = koinApplication { modules(module) }
            val stateComm = MqttCommunicator("192.168.8.1", 1883)
            val behaviourComm = MqttCommunicator("192.168.8.1", 1883)
            stateComm.setup(State to Behaviour, defaultMqttRemotePlace()[Behaviour])
            behaviourComm.setup(Behaviour to State, defaultMqttRemotePlace()[State])

            val behaviourJob = launch(UnconfinedTestDispatcher()) {
                behaviourComm.receiveMessage().first() shouldBe "hello".toByteArray()
            }

            stateComm.fireMessage("hello".toByteArray())

            behaviourJob.join()
            stateComm.finalize()
            behaviourComm.finalize()
        }
    }
})
