package it.nicolasfarabegoli.pulverization.core

import it.nicolasfarabegoli.pulverization.component.Context
import it.nicolasfarabegoli.pulverization.utils.PulverizationKoinModule
import org.koin.core.Koin
import org.koin.core.component.KoinComponent

/**
 * High level concept of pulverized component.
 */
interface PulverizedComponent<S, C, SS, AS, O> : Initializable, KoinComponent {
    override fun getKoin(): Koin = PulverizationKoinModule.koinApp?.koin ?: error("No Koin app defined")

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

/**
 * Utility extension method to return a string representation for each [PulverizedComponentType].
 */
fun PulverizedComponentType.show(): String =
    when (this) {
        ActuatorsComponent -> "actuators"
        BehaviourComponent -> "behaviour"
        CommunicationComponent -> "communication"
        SensorsComponent -> "sensors"
        StateComponent -> "state"
    }
