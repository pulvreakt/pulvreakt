package it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model

import it.nicolasfarabegoli.pulverization.dsl.v2.model.LogicalDeviceSpecification

/**
 * Configuration holding [componentsRuntimeConfiguration] and the [reconfigurationRules] belonging to that deployment
 * unit.
 */
data class ComponentsRuntimeConfiguration<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    val componentsRuntimeConfiguration: ComponentsRuntimeContainer<S, C, SS, AS, O>,
    val reconfigurationRules: ReconfigurationRules?,
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
