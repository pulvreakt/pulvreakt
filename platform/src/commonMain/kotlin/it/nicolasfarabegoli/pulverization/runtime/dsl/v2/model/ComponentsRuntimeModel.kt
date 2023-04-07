package it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model

import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.runtime.utils.ActuatorsLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.BehaviourLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.CommunicationLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.SensorsLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.StateLogicType

interface RuntimeConfig {
    val startupHost: Host
}

data class PartialBehaviourRuntimeConfig<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    val behaviourComponent: Behaviour<S, C, SS, AS, O>,
    val behaviourLogic: BehaviourLogicType<S, C, SS, AS, O>,
)

data class BehaviourRuntimeConfig<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    val behaviourComponent: Behaviour<S, C, SS, AS, O>,
    val behaviourLogic: BehaviourLogicType<S, C, SS, AS, O>,
    override val startupHost: Host,
) : RuntimeConfig

data class PartialStateRuntimeConfig<S : Any>(
    val stateComponent: State<S>,
    val stateLogic: StateLogicType<S>,
)

data class StateRuntimeConfig<S : Any>(
    val stateComponent: State<S>,
    val stateLogic: StateLogicType<S>,
    override val startupHost: Host,
) : RuntimeConfig

data class PartialCommunicationRuntimeConfig<C : Any>(
    val communicationComponent: Communication<C>,
    val communicationLogic: CommunicationLogicType<C>,
)

data class CommunicationRuntimeConfig<C : Any>(
    val communicationComponent: Communication<C>,
    val communicationLogic: CommunicationLogicType<C>,
    override val startupHost: Host,
) : RuntimeConfig

data class PartialSensorsRuntimeConfig<SS : Any>(
    val sensorsComponent: SensorsContainer,
    val sensorsLogic: SensorsLogicType<SS>,
)

data class SensorsRuntimeConfig<SS : Any>(
    val sensorsComponent: SensorsContainer,
    val sensorsLogic: SensorsLogicType<SS>,
    override val startupHost: Host,
) : RuntimeConfig

data class PartialActuatorsRuntimeConfig<AS : Any>(
    val actuatorsComponent: ActuatorsContainer,
    val actuatorsLogic: ActuatorsLogicType<AS>,
)

data class ActuatorsRuntimeConfig<AS : Any>(
    val actuatorsComponent: ActuatorsContainer,
    val actuatorsLogic: ActuatorsLogicType<AS>,
    override val startupHost: Host,
) : RuntimeConfig

data class ComponentsRuntimeContainer<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    val behaviourRuntime: BehaviourRuntimeConfig<S, C, SS, AS, O>?,
    val stateRuntime: StateRuntimeConfig<S>?,
    val communicationRuntime: CommunicationRuntimeConfig<C>?,
    val sensorsRuntime: SensorsRuntimeConfig<SS>?,
    val actuatorsRuntime: ActuatorsRuntimeConfig<AS>?,
)
