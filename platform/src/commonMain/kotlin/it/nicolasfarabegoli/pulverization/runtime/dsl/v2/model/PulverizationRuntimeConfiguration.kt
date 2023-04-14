package it.nicolasfarabegoli.pulverization.runtime.dsl.v2.model

import it.nicolasfarabegoli.pulverization.dsl.v2.model.Actuators
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Behaviour
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Communication
import it.nicolasfarabegoli.pulverization.dsl.v2.model.ComponentType
import it.nicolasfarabegoli.pulverization.dsl.v2.model.LogicalDeviceSpecification
import it.nicolasfarabegoli.pulverization.dsl.v2.model.Sensors
import it.nicolasfarabegoli.pulverization.dsl.v2.model.State
import it.nicolasfarabegoli.pulverization.runtime.communication.Communicator
import it.nicolasfarabegoli.pulverization.runtime.communication.RemotePlaceProvider
import it.nicolasfarabegoli.pulverization.runtime.reconfiguration.Reconfigurator

/**
 * Configuration holding [componentsRuntimeConfiguration] and the [reconfigurationRules] belonging to that deployment
 * unit.
 * Save also [communicatorProvider] and [reconfiguratorProvider] and [remotePlaceProvider].
 */
data class ComponentsRuntimeConfiguration<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    val componentsRuntimeConfiguration: ComponentsRuntimeContainer<S, C, SS, AS, O>,
    val reconfigurationRules: ReconfigurationRules?,
    val communicatorProvider: () -> Communicator,
    val reconfiguratorProvider: () -> Reconfigurator,
    val remotePlaceProvider: RemotePlaceProvider,
)

/**
 * Complete configuration needed for the pulverization runtime.
 * Holds the [deviceSpecification], the [runtimeConfiguration], the [hostCapabilityMapping] and [availableHosts].
 */
data class DeploymentUnitRuntimeConfiguration<S : Any, C : Any, SS : Any, AS : Any, O : Any>(
    val deviceSpecification: LogicalDeviceSpecification,
    val runtimeConfiguration: ComponentsRuntimeConfiguration<S, C, SS, AS, O>,
    val hostCapabilityMapping: HostCapabilityMapping,
    val availableHosts: Set<Host>,
) {
    /**
     * Given a [host], return the set of [ComponentType] belonging to it.
     */
    fun startupComponent(host: Host): Set<ComponentType> {
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

    /**
     * Returns a map containing the [ComponentType] related to its startup [Host].
     */
    fun hostComponentsStartupMap(): Map<ComponentType, Host> {
        return with(runtimeConfiguration.componentsRuntimeConfiguration) {
            val componentMap = mutableMapOf<ComponentType, Host>()
            behaviourRuntime?.startupHost?.run { componentMap += Behaviour to this }
            stateRuntime?.startupHost?.run { componentMap += State to this }
            communicationRuntime?.startupHost?.run { componentMap += Communication to this }
            sensorsRuntime?.startupHost?.run { componentMap += Sensors to this }
            actuatorsRuntime?.startupHost?.run { componentMap += Actuators to this }
            componentMap
        }
    }

    /**
     * Return the [Host] that match the [hostname], if any.
     */
    fun getHost(hostname: String): Host? = availableHosts.firstOrNull { it.hostname == hostname }
}

/**
 * Retrieve the configuration rules, if any.
 */
fun <S, C, SS, AS, O> DeploymentUnitRuntimeConfiguration<S, C, SS, AS, O>.reconfigurationRules(
): ReconfigurationRules? where S : Any, C : Any, SS : Any, AS : Any, O : Any = runtimeConfiguration.reconfigurationRules
