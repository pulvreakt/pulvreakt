package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.config

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.config.get
import it.nicolasfarabegoli.pulverization.core.DeviceIDOps.toID
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import it.nicolasfarabegoli.pulverization.core.get
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component.RabbitmqContext
import org.koin.core.component.inject

data class StateRepr(val i: Int) : StateRepresentation
class MyState : State<StateRepr> {
    override fun get(): StateRepr = StateRepr(1)

    override fun update(newState: StateRepr): StateRepr {
        TODO("Not yet implemented")
    }
}

class StateComponent : DeviceComponent<RabbitmqContext> {
    override val context: RabbitmqContext by inject()

    private val state: MyState by inject()

    override suspend fun cycle() {
        state.get()
    }
}

class PulverizationSetupTest : FunSpec(
    {
        context("Testing the component setup") {
            test("Basic component creation") {
                val config = pulverizationConfig {
                    logicalDevice("device-1") {
                        component(MyState())
                    }
                }
                pulverizationSetup("device-instance-1".toID()) {
                    registerComponent(config["device-1"]?.get<MyState>())
                }

                shouldNotThrow<Exception> { StateComponent().cycle() }
            }
            test("An exception should be thrown if no component is given to the setup") {
                val config = pulverizationConfig {
                    logicalDevice("device-1") {
                        component(MyState())
                    }
                }
                shouldThrow<IllegalStateException> {
                    pulverizationSetup("device-instance-1".toID()) {
                        registerComponent(config["no-device"]?.get<MyState>())
                    }
                }
            }
        }
    },
)
