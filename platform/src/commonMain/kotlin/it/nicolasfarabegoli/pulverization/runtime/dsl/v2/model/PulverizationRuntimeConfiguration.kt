package it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model

import it.nicolasfarabegoli.pulverization.dsl.v2.model.LogicalDeviceSpecification
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
)

fun <S : Any, C : Any, SS : Any, AS : Any, O : Any>
    DeploymentUnitRuntimeConfiguration<S, C, SS, AS, O>.reconfigurationRules(): ReconfigurationRules? =
    runtimeConfiguration.reconfigurationRules
