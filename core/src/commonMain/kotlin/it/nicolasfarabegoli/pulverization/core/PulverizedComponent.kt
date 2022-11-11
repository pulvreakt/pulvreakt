package it.nicolasfarabegoli.pulverization.core

import it.nicolasfarabegoli.pulverization.component.Context
import org.koin.core.component.KoinComponent

/**
 * High level concept of pulverized component.
 */
interface PulverizedComponent : KoinComponent {
    /**
     * The context in which the component live.
     */
    val context: Context

    /**
     * The type of the component.
     */
    val componentType: PulverizedComponentType
}

/**
 * ADT representing all the components type in a pulverized device.
 */
sealed interface PulverizedComponentType

/**
 * The [Communication] component.
 */
object CommunicationComponent : PulverizedComponentType

/**
 * The [Behaviour] component.
 */
object BehaviourComponent : PulverizedComponentType

/**
 * The [Actuator] component.
 */
object ActuatorsComponent : PulverizedComponentType

/**
 * The [Sensor] component.
 */
object SensorsComponent : PulverizedComponentType

/**
 * The [State] component.
 */
object StateComponent : PulverizedComponentType
