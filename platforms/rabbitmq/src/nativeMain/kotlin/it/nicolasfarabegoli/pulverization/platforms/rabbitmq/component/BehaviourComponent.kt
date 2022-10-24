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
actual abstract class AbstractBehaviourComponent<S, E, W, A, I>(private val device: LogicalDevice<I>) :
    DeviceComponent<I>, KoinComponent
    where S : StateRepresentation, E : Export, I : DeviceID
