package it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model

import it.nicolasfarabegoli.pulverization.dsl.v2.model.Actuators
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Communication
import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import it.nicolasfarabegoli.pulverization.dsl.v2.model.LogicalDeviceSpecification
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Sensors
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.Reconfigurator

/**
 * Configuration holding [componentsRuntimeConfiguration] and the [reconfigurationRules] belonging to that deployment
 * unit.
 */
data class ComponentsRuntimeConfiguration<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    val componentsRuntimeConfiguration: ComponentsRuntimeContainer<S, C, SS, AS, O>,
    val reconfigurationRules: ReconfigurationRules?,
    val communicatorProvider: () -> Communicator,
    val reconfiguratorProvider: () -> Reconfigurator,
)

/**
 * Complete configuration needed for the pulverization runtime.
 * Holds the [deviceSpecification], the [runtimeConfiguration] and the [hostCapabilityMapping].
 */
data class DeploymentUnitRuntimeConfiguration<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    val deviceSpecification: LogicalDeviceSpecification,
    val runtimeConfiguration: ComponentsRuntimeConfiguration<S, C, SS, AS, O>,
    val hostCapabilityMapping: HostCapabilityMapping,
) {
    fun getStartupComponents(host: Host): Set<ComponentType> {
        return with(runtimeConfiguration.componentsRuntimeConfiguration) {
            val components = mutableSetOf<ComponentType>()
            if (behaviourRuntime?.startupHost == host) components += Behaviour
            if (stateRuntime?.startupHost == host) components += State
            if (communicationRuntime?.startupHost == host) components += Communication
            if (sensorsRuntime?.startupHost == host) components += Sensors
            if (actuatorsRuntime?.startupHost == host) components += Actuators
            components
        }
    }
}

fun <S : Any, C : Any, SS : Any, AS : Any, O : Any>
    DeploymentUnitRuntimeConfiguration<S, C, SS, AS, O>.reconfigurationRules(): ReconfigurationRules? =
    runtimeConfiguration.reconfigurationRules
