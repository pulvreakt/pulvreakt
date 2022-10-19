package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.ComponentsType
import it.nicolasfarabegoli.pulverization.core.DeviceID
import it.nicolasfarabegoli.pulverization.core.Export
import it.nicolasfarabegoli.pulverization.core.LogicalDevice
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import reactor.rabbitmq.RabbitFlux
import reactor.rabbitmq.Receiver
import reactor.rabbitmq.Sender

actual abstract class AbstractBehaviourComponent<S, E, W, A, I>(private val device: LogicalDevice<I>) :
    DeviceComponent<I>, KoinComponent
    where S : StateRepresentation, E : Export, I : DeviceID {

    private val behaviour: Behaviour<S, E, W, A, Unit, I> by inject()
    private val sender: Sender = RabbitFlux.createSender()
    private val receiver: Receiver = RabbitFlux.createReceiver()
    private val queues = queuesToCreate(device.components, device.id)

    override val id: I = device.id
}

internal fun queuesToCreate(components: Set<ComponentsType>, deviceID: DeviceID): Set<String> {
    return components.map {
        when (it) {
            ComponentsType.ACTUATORS -> "actuators/$deviceID"
            ComponentsType.BEHAVIOUR -> "behaviour/$deviceID"
            ComponentsType.COMMUNICATION -> "communication/$deviceID"
            ComponentsType.SENSORS -> "sensors/$deviceID"
            ComponentsType.STATE -> "state/$deviceID"
        }
    }.toSet()
}
