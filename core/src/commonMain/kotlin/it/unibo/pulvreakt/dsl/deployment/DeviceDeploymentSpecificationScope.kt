package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptySet
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.zipOrAccumulate
import arrow.core.toNonEmptyListOrNull
import it.unibo.pulvreakt.core.component.Component
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.ComponentNotRegistered
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.InvalidStartupHost
import it.unibo.pulvreakt.dsl.model.Capability
import it.unibo.pulvreakt.dsl.model.ComponentType.Companion.getType
import it.unibo.pulvreakt.dsl.model.DeviceRuntimeConfiguration
import it.unibo.pulvreakt.dsl.model.DeviceStructure
import it.unibo.pulvreakt.dsl.model.ReconfigurationRules

/**
 * Scope for the deployment configuration of a device.
 */
class DeviceDeploymentSpecificationScope(
    private val deviceName: String,
    private val deviceStructure: DeviceStructure,
    private val infrastructure: NonEmptySet<Host>,
) {
    private val componentsStartupHosts = mutableListOf<Either<InvalidStartupHost, Pair<Component<*>, Host>>>()
    private var deviceReconfigurationRules: Either<Nel<DeploymentConfigurationError>, ReconfigurationRules>? = null

    /**
     * Configures a component of the device that starts on the given [host].
     */
    infix fun Component<*>.startsOn(host: Host) {
        val componentType = this.getType()
        val deviceCapabilities = deviceStructure.requiredCapabilities[componentType]!!
        val hostCapabilities = host.capabilities
        val result = either {
            ensure(containCapability(deviceCapabilities, hostCapabilities)) { InvalidStartupHost(componentType, host) }
            this@startsOn to host
        }
        componentsStartupHosts += result
    }

    /**
     * Configures the reconfiguration rules of the device.
     */
    fun reconfigurationRules(config: ReconfigurationRulesScope.() -> Unit) {
        val scope = ReconfigurationRulesScope(deviceStructure, infrastructure).apply(config)
        deviceReconfigurationRules = scope.generate()
    }

    private fun containCapability(compCapability: Set<Capability>, hostCapability: Set<Capability>): Boolean =
        compCapability.intersect(hostCapability).isNotEmpty()

    private fun Raise<Nel<ComponentNotRegistered>>.ensureAllInstancesAreGiven(
        registeredComponents: List<Pair<Component<*>, Host>>,
    ): List<Pair<Component<*>, Host>> {
        val allComponents = deviceStructure.componentsGraph.keys + deviceStructure.componentsGraph.values.flatten()
        val nonConfiguredComponents = allComponents - registeredComponents.map { it.first.getType() }.toSet()
        ensure(nonConfiguredComponents.isEmpty()) {
            nonConfiguredComponents.map { ComponentNotRegistered(it) }.toNonEmptyListOrNull()!!
        }
        return registeredComponents
    }

    internal fun generate(): Either<Nel<DeploymentConfigurationError>, DeviceRuntimeConfiguration> = either {
        zipOrAccumulate(
            { ensureAllInstancesAreGiven(either { componentsStartupHosts.bindAll() }.bind()) },
            { deviceReconfigurationRules?.bindNel() },
        ) { startUpHosts, reconfigRules -> DeviceRuntimeConfiguration(deviceName, startUpHosts.toMap(), reconfigRules) }
    }
}
