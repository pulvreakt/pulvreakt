package it.nicolasfarabegoli.pulverization.component

import it.nicolasfarabegoli.pulverization.communication.BidirectionalCommunicator

/**
 * A device component with the ability of a [BidirectionalCommunicator] to communicate with other
 * [DeviceComponent].
 */
interface DeviceComponent<Send, Receive, I> : BidirectionalCommunicator<Send, Receive, I> {
    suspend fun cycle()
}

// /**
// * TODO.
// */
// interface DeviceComponent {
//    suspend fun cycle()
// }
//
// abstract class StateComponent<S, Send, Receive, I> : KoinComponent, DeviceComponent {
//    protected val state: State<S> by inject()
//    protected abstract val behaviourCommunicator: BidirectionalCommunicator<Send, Receive, I>
// }
//
// abstract class SensorsComponent<Send, I> : KoinComponent, DeviceComponent {
//    protected val componentsContainer: SensorsContainer<I> by inject()
//    protected abstract val behaviourCommunicator: SenderCommunicator<Send, I>
// }
//
// abstract class ActuatorsComponent<Receive, I> : KoinComponent, DeviceComponent {
//    protected val actuatorsContainer: ActuatorsContainer<I> by inject()
//    protected abstract val behaviourCommunicator: ReceiverCommunicator<Receive, I>
// }
//
// abstract class CommunicateComponent<PS, PR, Send, Receive, I> : KoinComponent, DeviceComponent {
//    protected val neighboursCommunication: Communication<PS, PR> by inject()
//    protected abstract val behaviourCommunicator: BidirectionalCommunicator<Send, Receive, I>
// }
