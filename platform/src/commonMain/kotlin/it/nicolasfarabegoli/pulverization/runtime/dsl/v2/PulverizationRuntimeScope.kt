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

    infix fun Host.or(other: Host): Set<Host> = setOf(this, other)
    infix fun Set<Host>.or(other: Host): Set<Host> = this + other

    infix fun Behaviour<S, C, SS, AS, O>.withLogic(
        logic: BehaviourLogicType<S, C, SS, AS, O>,
    ): BehaviourRuntimeConfig<S, C, SS, AS, O> = BehaviourRuntimeConfig(this, logic, emptySet(), null)

    infix fun State<S>.withLogic(logic: StateLogicType<S>): StateRuntimeConfig<S> =
        StateRuntimeConfig(this, logic, emptySet(), null)

    infix fun Communication<C>.withLogic(logic: CommunicationLogicType<C>): CommunicationRuntimeConfig<C> =
        CommunicationRuntimeConfig(this, logic, emptySet(), null)

    infix fun SensorsContainer.withLogic(logic: SensorsLogicType<SS>): SensorsRuntimeConfig<SS> =
        SensorsRuntimeConfig(this, logic, emptySet(), null)

    infix fun ActuatorsContainer.withLogic(logic: ActuatorsLogicType<AS>): ActuatorsRuntimeConfig<AS> =
        ActuatorsRuntimeConfig(this, logic, emptySet(), null)

    infix fun Behaviour<S, C, SS, AS, O>.runsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            behaviourRuntime = BehaviourRuntimeConfig(this, ::defaultBehaviourLogic, setOf(host), host),
        )
    }

    infix fun State<S>.runsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            stateRuntime = StateRuntimeConfig(this, ::defaultStateLogic, setOf(host), host),
        )
    }

    infix fun Communication<C>.runsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            communicationRuntime = CommunicationRuntimeConfig(this, ::defaultCommunicationLogic, setOf(host), host),
        )
    }

    infix fun SensorsContainer.runsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            sensorsRuntime = SensorsRuntimeConfig(this, ::defaultSensorsLogic, setOf(host), host),
        )
    }

    infix fun ActuatorsContainer.runsOn(host: Host) {
        componentsRuntime = componentsRuntime.copy(
            actuatorsRuntime = ActuatorsRuntimeConfig(this, ::defaultActuatorsLogic, setOf(host), host),
        )
    }

    infix fun Behaviour<S, C, SS, AS, O>.runsOn(hosts: Set<Host>): BehaviourRuntimeConfig<S, C, SS, AS, O> =
        BehaviourRuntimeConfig(this, ::defaultBehaviourLogic, hosts, hosts.random())

    infix fun State<S>.runsOn(hosts: Set<Host>): StateRuntimeConfig<S> =
        StateRuntimeConfig(this, ::defaultStateLogic, hosts, hosts.random())

    infix fun Communication<C>.runsOn(hosts: Set<Host>): CommunicationRuntimeConfig<C> =
        CommunicationRuntimeConfig(this, ::defaultCommunicationLogic, hosts, hosts.random())

    infix fun SensorsContainer.runsOn(hosts: Set<Host>): SensorsRuntimeConfig<SS> =
        SensorsRuntimeConfig(this, ::defaultSensorsLogic, hosts, hosts.random())

    infix fun ActuatorsContainer.runsOn(hosts: Set<Host>): ActuatorsRuntimeConfig<AS> =
        ActuatorsRuntimeConfig(this, ::defaultActuatorsLogic, hosts, hosts.random())

    infix fun BehaviourRuntimeConfig<S, C, SS, AS, O>.runsOn(
        hosts: Set<Host>,
    ): BehaviourRuntimeConfig<S, C, SS, AS, O> = this.copy(runnableHosts = hosts, startupHost = hosts.random())

    infix fun StateRuntimeConfig<S>.runsOn(hosts: Set<Host>): StateRuntimeConfig<S> =
        this.copy(runnableHosts = hosts, startupHost = hosts.random())

    infix fun CommunicationRuntimeConfig<C>.runsOn(hosts: Set<Host>): CommunicationRuntimeConfig<C> =
        this.copy(runnableHosts = hosts, startupHost = hosts.random())

    infix fun SensorsRuntimeConfig<SS>.runsOn(hosts: Set<Host>): SensorsRuntimeConfig<SS> =
        this.copy(runnableHosts = hosts, startupHost = hosts.random())

    infix fun ActuatorsRuntimeConfig<AS>.runsOn(hosts: Set<Host>): ActuatorsRuntimeConfig<AS> =
        this.copy(runnableHosts = hosts, startupHost = hosts.random())

    infix fun BehaviourRuntimeConfig<S, C, SS, AS, O>.runsOn(hosts: Host) {
        componentsRuntime =
            componentsRuntime.copy(behaviourRuntime = this.copy(runnableHosts = setOf(hosts), startupHost = hosts))
    }

    infix fun StateRuntimeConfig<S>.runsOn(hosts: Host) {
        componentsRuntime =
            componentsRuntime.copy(stateRuntime = this.copy(runnableHosts = setOf(hosts), startupHost = hosts))
    }

    infix fun CommunicationRuntimeConfig<C>.runsOn(hosts: Host) {
        componentsRuntime =
            componentsRuntime.copy(communicationRuntime = this.copy(runnableHosts = setOf(hosts), startupHost = hosts))
    }

    infix fun SensorsRuntimeConfig<SS>.runsOn(hosts: Host) {
        componentsRuntime =
            componentsRuntime.copy(sensorsRuntime = this.copy(runnableHosts = setOf(hosts), startupHost = hosts))
    }

    infix fun ActuatorsRuntimeConfig<AS>.runsOn(hosts: Host) {
        componentsRuntime =
            componentsRuntime.copy(actuatorsRuntime = this.copy(runnableHosts = setOf(hosts), startupHost = hosts))
    }

    infix fun BehaviourRuntimeConfig<S, C, SS, AS, O>.startOn(hosts: Host) {
        componentsRuntime = componentsRuntime.copy(behaviourRuntime = this.copy(startupHost = hosts))
    }

    infix fun StateRuntimeConfig<S>.startOn(hosts: Host) {
        componentsRuntime = componentsRuntime.copy(stateRuntime = this.copy(startupHost = hosts))
    }

    infix fun CommunicationRuntimeConfig<C>.startOn(hosts: Host) {
        componentsRuntime = componentsRuntime.copy(communicationRuntime = this.copy(startupHost = hosts))
    }

    infix fun SensorsRuntimeConfig<SS>.startOn(hosts: Host) {
        componentsRuntime = componentsRuntime.copy(sensorsRuntime = this.copy(startupHost = hosts))
    }

    infix fun ActuatorsRuntimeConfig<AS>.startOn(hosts: Host) {
        componentsRuntime = componentsRuntime.copy(actuatorsRuntime = this.copy(startupHost = hosts))
    }
}
