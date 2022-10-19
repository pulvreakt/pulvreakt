package it.nicolasfarabegoli.pulverization.platforms.rabbitmq.component

import it.nicolasfarabegoli.pulverization.component.DeviceComponent
import it.nicolasfarabegoli.pulverization.core.DeviceID
import it.nicolasfarabegoli.pulverization.core.Export
import it.nicolasfarabegoli.pulverization.core.LogicalDevice
import it.nicolasfarabegoli.pulverization.core.StateRepresentation
import org.koin.core.component.KoinComponent

actual abstract class AbstractBehaviourComponent<S, E, W, A, I>(private val device: LogicalDevice<I>) :
    DeviceComponent<I>, KoinComponent
    where S : StateRepresentation, E : Export, I : DeviceID
