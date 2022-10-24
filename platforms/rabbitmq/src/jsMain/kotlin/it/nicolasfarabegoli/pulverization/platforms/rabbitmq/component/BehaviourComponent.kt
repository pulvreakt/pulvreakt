package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.core.DeviceID
import it.nicolasfarabegoli.pulverization.core.Export
import it.nicolasfarabegoli.pulverization.core.LogicalDevice
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import org.koin.core.component.KoinComponent

/**
 * Abstract behaviour which enable, for a [device], all the communication with the other components.
 * This class doesn't implement any cycle logic.
 */
@Suppress("UnusedPrivateMember") // Remove when implementing the class
actual class BehaviourComponent<S, E, W, A, I>(private val device: LogicalDevice<I>) :
    DeviceComponent<I>, KoinComponent
    where S : StateRepresentation, E : Export, A : Any, W : Any, I : DeviceID {

    override val id: I
        get() = TODO("Not yet implemented")

    override suspend fun cycle() {
        TODO("Not yet implemented")
    }
}
