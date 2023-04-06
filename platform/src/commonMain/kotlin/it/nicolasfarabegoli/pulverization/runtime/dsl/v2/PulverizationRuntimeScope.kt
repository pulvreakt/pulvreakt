package it.nicolasfarabegoli.pulverization.runtime.dsl.v2

import it.nicolasfarabegoli.pulverization.core.ActuatorsContainer
import it.nicolasfarabegoli.pulverization.core.Behaviour
import it.nicolasfarabegoli.pulverization.core.Communication
import it.nicolasfarabegoli.pulverization.core.SensorsContainer
import it.nicolasfarabegoli.pulverization.core.State
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ActuatorsRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.BehaviourRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.CommunicationRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.ComponentsRuntimeContainer
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.Host
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.PartialActuatorsRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.PartialBehaviourRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.PartialCommunicationRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.PartialSensorsRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.PartialStateRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.SensorsRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model.StateRuntimeConfig
import it.nicolasfarabegoli.pulverization.runtime.utils.ActuatorsLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.BehaviourLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.CommunicationLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.SensorsLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.StateLogicType
import it.nicolasfarabegoli.pulverization.runtime.utils.defaultActuatorsLogic
import it.nicolasfarabegoli.pulverization.runtime.utils.defaultBehaviourLogic
import it.nicolasfarabegoli.pulverization.runtime.utils.defaultCommunicationLogic
import it.nicolasfarabegoli.pulverization.runtime.utils.defaultSensorsLogic
import it.nicolasfarabegoli.pulverization.runtime.utils.defaultStateLogic

/**
 * Scope for configuring the device with its components.
 */
@Suppress("TooManyFunctions")
class PulverizationRuntimeScope<S : Any, C : Any, SS : Any, AS : Any, O : Any> {
    private var componentsRuntime = ComponentsRuntimeContainer<S, C, SS, AS, O>(null, null, null, null, null)

    fun reconfigurationRules(config: ReconfigurationRulesScope.() -> Unit) { }

    infix fun Behaviour<S, C, SS, AS, O>.withLogic(
        logic: BehaviourLogicType<S, C, SS, AS, O>,
    ): PartialBehaviourRuntimeConfig<S, C, SS, AS, O> = PartialBehaviourRuntimeConfig(this, logic)

    infix fun State<S>.withLogic(
        logic: StateLogicType<S>,
    ): PartialStateRuntimeConfig<S> = PartialStateRuntimeConfig(this, logic)

    infix fun Communication<C>.withLogic(
        logic: CommunicationLogicType<C>,
    ): PartialCommunicationRuntimeConfig<C> = PartialCommunicationRuntimeConfig(this, logic)

    infix fun SensorsContainer.withLogic(
        logic: SensorsLogicType<SS>,
    ): PartialSensorsRuntimeConfig<SS> = PartialSensorsRuntimeConfig(this, logic)

    infix fun ActuatorsContainer.withLogic(
        logic: ActuatorsLogicType<AS>,
    ): PartialActuatorsRuntimeConfig<AS> = PartialActuatorsRuntimeConfig(this, logic)

    infix fun Behaviour<S, C, SS, AS, O>.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            behaviourRuntime = BehaviourRuntimeConfig(this, ::defaultBehaviourLogic, host),
        )
    }

    infix fun State<S>.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            stateRuntime = StateRuntimeConfig(this, ::defaultStateLogic, host),
        )
    }

    infix fun Communication<C>.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            communicationRuntime = CommunicationRuntimeConfig(this, ::defaultCommunicationLogic, host),
        )
    }

    infix fun SensorsContainer.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            sensorsRuntime = SensorsRuntimeConfig(this, ::defaultSensorsLogic, host),
        )
    }

    infix fun ActuatorsContainer.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            actuatorsRuntime = ActuatorsRuntimeConfig(this, ::defaultActuatorsLogic, host),
        )
    }

    infix fun PartialBehaviourRuntimeConfig<S, C, SS, AS, O>.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            behaviourRuntime = BehaviourRuntimeConfig(behaviourComponent, behaviourLogic, host),
        )
    }

    infix fun PartialStateRuntimeConfig<S>.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            stateRuntime = StateRuntimeConfig(stateComponent, stateLogic, host),
        )
    }

    infix fun PartialCommunicationRuntimeConfig<C>.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            communicationRuntime = CommunicationRuntimeConfig(communicationComponent, communicationLogic, host),
        )
    }

    infix fun PartialSensorsRuntimeConfig<SS>.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            sensorsRuntime = SensorsRuntimeConfig(sensorsComponent, sensorsLogic, host),
        )
    }

    infix fun PartialActuatorsRuntimeConfig<AS>.startsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            actuatorsRuntime = ActuatorsRuntimeConfig(actuatorsComponent, actuatorsLogic, host),
        )
    }
}
