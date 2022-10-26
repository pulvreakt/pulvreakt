package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.core.ComponentsType
import it.nicolasfarabegoli.pulverization.core.DeviceID
import it.nicolasfarabegoli.pulverization.core.Export
import it.nicolasfarabegoli.pulverization.core.LogicalDevice
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import it.nicolasfarabegoli.pulverization.platforms.rabbitmq.communication.RabbitmqCommunicator
import org.koin.core.component.KoinComponent
import kotlin.reflect.KClass

/**
 * Abstract behaviour which enable all the communication with the other components.
 * This class doesn't implement any cycle logic.
 */
expect class BehaviourComponent<S, E, W, A, I> :
    DeviceComponent<I>, KoinComponent
    where S : StateRepresentation, E : Export, A : Any, W : Any, I : DeviceID

/**
 * TODO.
 * Explore delegates opportunity.
 */
abstract class RabbitmqDeviceComponent<I : DeviceID>(device: LogicalDevice<I>) : DeviceComponent<I> {
    private var communicationComponents: Map<ComponentsType, RabbitmqCommunicator> = emptyMap()

    /**
     * TODO.
     */
    fun addCommunicator(elem: Pair<ComponentsType, RabbitmqCommunicator>) {
        communicationComponents = communicationComponents + elem
    }

    /**
     * TODO.
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <C : RabbitmqCommunicator> get(componentsType: ComponentsType, type: KClass<C>): C? =
        communicationComponents[componentsType].takeIf { type.isInstance(it) } as? C

    /**
     * TODO.
     */
    inline fun <reified C : RabbitmqCommunicator> get(componentsType: ComponentsType): C? =
        get(componentsType, C::class)

    @Suppress("UNCHECKED_CAST")
    fun <C : RabbitmqCommunicator> getValue(componentsType: ComponentsType, type: KClass<C>): C {
        val elem = communicationComponents[componentsType]
            .takeIf { type.isInstance(it) } ?: throw NoSuchElementException("")
        return elem as C
    }

    /**
     * TODO.
     */
    inline fun <reified C : RabbitmqCommunicator> getValue(componentsType: ComponentsType): C =
        getValue(componentsType, C::class)

    override val id: I = device.id

    override suspend fun initialize() {
        super.initialize()
    }
}
