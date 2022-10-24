package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.core.DeviceID
import it.nicolasfarabegoli.pulverization.core.Export
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import org.koin.core.component.KoinComponent

// sealed class BehaviourSendMessages {
//    data class ActuatorsMessage<P>(val payload: P) : BehaviourSendMessages()
//    data class StateMessage<P>(val payload: P) : BehaviourSendMessages()
//    data class CommunicationSendMessage<P>(val payload: P) : BehaviourSendMessages()
// }
//
// sealed class BehaviourReceiveMessage {
//    data class SensorsMessage<P>(val payload: P) : BehaviourReceiveMessage()
//    data class StateMessage<P>(val payload: P) : BehaviourReceiveMessage()
//    data class CommunicationReceiveMessage<P>(val payload: P) : BehaviourReceiveMessage()
// }

/**
 * Abstract behaviour which enable all the communication with the other components.
 * This class doesn't implement any cycle logic.
 */
expect class BehaviourComponent<S, E, W, A, I> :
    DeviceComponent<I>, KoinComponent
    where S : StateRepresentation, E : Export, A : Any, W : Any, I : DeviceID
