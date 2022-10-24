package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.ComponentsType
import it.nicolasfarabegoli.pulverization.core.DeviceID
import it.nicolasfarabegoli.pulverization.core.Export
import it.nicolasfarabegoli.pulverization.core.LogicalDevice
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication.RabbitmqBidirectionalCommunicator
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication.RabbitmqReceiverCommunicator
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication.RabbitmqSenderCommunicator
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication.SimpleRabbitmqSenderCommunicator
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KClass

/**
 * Abstract behaviour which enable, for a [device], all the communication with the other components.
 * This class doesn't implement any cycle logic.
 */
actual open class AbstractBehaviourComponent<S, E, W, A, I>(
    private val kState: KClass<S>,
    private val kExport: KClass<E>,
    private val kActuators: KClass<A>,
    private val kSensors: KClass<W>,
    private val device: LogicalDevice<I>,
) : DeviceComponent<I>, KoinComponent
    where S : StateRepresentation, E : Export, A : Any, W : Any, I : DeviceID {

    protected val behaviour: Behaviour<S, E, W, A, Unit, I> by inject()
    protected var stateComponent: RabbitmqBidirectionalCommunicator<S, S, I>? = null
    protected var sensorsComponent: RabbitmqReceiverCommunicator<W, I>? = null
    protected var actuatorsComponent: RabbitmqSenderCommunicator<A, I>? = null
    protected var communicationComponent: RabbitmqBidirectionalCommunicator<E, E, I>? = null

    companion object {
        inline operator fun <reified S, reified E, reified W, reified A, I : DeviceID> invoke(device: LogicalDevice<I>)
            where S : StateRepresentation, E : Export, A : Any, W : Any =
            AbstractBehaviourComponent(S::class, E::class, W::class, A::class, device)
    }

    init {
        device.components.forEach {
            when (it) {
                ComponentsType.ACTUATORS ->
                    actuatorsComponent =
                        SimpleRabbitmqSenderCommunicator(kActuators, id, "actuators/$id")

                ComponentsType.BEHAVIOUR -> error("The behaviour must not have a self referencing component")
                ComponentsType.COMMUNICATION -> TODO()
                ComponentsType.SENSORS -> TODO()
                ComponentsType.STATE -> TODO()
            }
        }
    }

    override val id: I = device.id
    override suspend fun cycle() {
        TODO("Not yet implemented")
    }
}
