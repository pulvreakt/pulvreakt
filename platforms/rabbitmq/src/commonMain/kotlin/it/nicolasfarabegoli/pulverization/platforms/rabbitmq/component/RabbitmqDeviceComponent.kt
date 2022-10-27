package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.core.PulverizedComponent
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication.RabbitmqCommunicator
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

// data class StateRepr(val i: Int) : StateRepresentation
// class MyState : State<StateRepr> {
//    override fun get(): StateRepr {
//        TODO("Not yet implemented")
//    }
//
//    override fun update(newState: StateRepr): StateRepr {
//        TODO("Not yet implemented")
//    }
// }
//
// class Foo : RabbitmqDeviceComponent<MyState, SimpleRabbitmqBidirectionalCommunication<StateRepr, StateRepr>>() {
//    override val communicator: SimpleRabbitmqBidirectionalCommunication<StateRepr, StateRepr> by inject()
//    override val component: MyState by inject()
//
//    override suspend fun cycle() {
//        communicator.sendToComponent(component.get())
//    }
// }

/**
 * TODO.
 * Explore delegates opportunity.
 */
abstract class RabbitmqDeviceComponent<Component : PulverizedComponent, Communicator : RabbitmqCommunicator> :
    DeviceComponent<RabbitmqContext>, KoinComponent {
    override val context: RabbitmqContext by inject()

    @Suppress("UndocumentedPublicProperty")
    abstract val component: Component

    @Suppress("UndocumentedPublicProperty")
    abstract val communicator: Communicator
}
