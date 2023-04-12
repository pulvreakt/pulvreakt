package it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model

import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Actuators
import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Sensors
import it.nicolasfarabegoli.pulverization.runtime.utils.ActuatorsLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.BehaviourLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.CommunicationLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.SensorsLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.StateLogicType
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour as BehaviourC
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State as StateC
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Communication as CommunicationC

/**
 * Runtime configuration that set up the [startupHost].
 */
interface RuntimeConfig {
    /**
     * The host on which the component should start on.
     */
    val startupHost: Host
}

/**
 * Partial configuration that associate [behaviourComponent] with its [behaviourLogic].
 * Note: This class should not be used directly.
 */
data class PartialBehaviourRuntimeConfig<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    val behaviourComponent: Behaviour<S, C, SS, AS, O>,
    val behaviourLogic: BehaviourLogicType<S, C, SS, AS, O>,
)

/**
 * Configuration that associate [behaviourComponent] with its [behaviourLogic] and the [startupHost].
 */
data class BehaviourRuntimeConfig<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    val behaviourComponent: Behaviour<S, C, SS, AS, O>,
    val behaviourLogic: BehaviourLogicType<S, C, SS, AS, O>,
    override val startupHost: Host,
) : RuntimeConfig

/**
 * Partial configuration that associate [stateComponent] with its [stateLogic].
 * Note: This class should not be used directly.
 */
data class PartialStateRuntimeConfig<S : Any>(
    val stateComponent: State<S>,
    val stateLogic: StateLogicType<S>,
)

/**
 * Configuration that associate [stateComponent] with its [stateLogic] and the [startupHost].
 */
data class StateRuntimeConfig<S : Any>(
    val stateComponent: State<S>,
    val stateLogic: StateLogicType<S>,
    override val startupHost: Host,
) : RuntimeConfig

/**
 * Partial configuration that associate [communicationComponent] with its [communicationLogic].
 * Note: This class should not be used directly.
 */
data class PartialCommunicationRuntimeConfig<C : Any>(
    val communicationComponent: Communication<C>,
    val communicationLogic: CommunicationLogicType<C>,
)

/**
 * Configuration that associate [communicationComponent] with its [communicationLogic] and the [startupHost].
 */
data class CommunicationRuntimeConfig<C : Any>(
    val communicationComponent: Communication<C>,
    val communicationLogic: CommunicationLogicType<C>,
    override val startupHost: Host,
) : RuntimeConfig

/**
 * Partial configuration that associate [sensorsComponent] with its [sensorsLogic].
 * Note: This class should not be used directly.
 */
data class PartialSensorsRuntimeConfig<SS : Any>(
    val sensorsComponent: SensorsContainer,
    val sensorsLogic: SensorsLogicType<SS>,
)

/**
 * Configuration that associate [sensorsComponent] with its [sensorsLogic] and the [startupHost].
 */
data class SensorsRuntimeConfig<SS : Any>(
    val sensorsComponent: SensorsContainer,
    val sensorsLogic: SensorsLogicType<SS>,
    override val startupHost: Host,
) : RuntimeConfig

/**
 * Partial configuration that associate [actuatorsComponent] with its [actuatorsLogic].
 * Note: This class should not be used directly.
 */
data class PartialActuatorsRuntimeConfig<AS : Any>(
    val actuatorsComponent: ActuatorsContainer,
    val actuatorsLogic: ActuatorsLogicType<AS>,
)

/**
 * Configuration that associate [actuatorsComponent] with its [actuatorsLogic] and the [startupHost].
 */
data class ActuatorsRuntimeConfig<AS : Any>(
    val actuatorsComponent: ActuatorsContainer,
    val actuatorsLogic: ActuatorsLogicType<AS>,
    override val startupHost: Host,
) : RuntimeConfig

/**
 * Hold all the configuration for all configured components.
 * [behaviourRuntime] holds the behaviour components with its corresponding logic and the startup host.
 * [stateRuntime] hold the state component with its corresponding logic and the startup host.
 * [communicationRuntime] hold the communication component with its corresponding logic and the startup host.
 * [sensorsRuntime] holds the sensors component with its corresponding logic and the startup host.
 * [actuatorsRuntime] holds the actuators components with its corresponding logic and the startup host.
 */
data class ComponentsRuntimeContainer<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    val behaviourRuntime: BehaviourRuntimeConfig<S, C, SS, AS, O>?,
    val stateRuntime: StateRuntimeConfig<S>?,
    val communicationRuntime: CommunicationRuntimeConfig<C>?,
    val sensorsRuntime: SensorsRuntimeConfig<SS>?,
    val actuatorsRuntime: ActuatorsRuntimeConfig<AS>?,
)
