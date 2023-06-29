package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.Nel
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
import it.unibo.pulvreakt.dsl.model.ComponentName
import it.unibo.pulvreakt.dsl.model.DeviceRuntimeConfiguration
import it.unibo.pulvreakt.dsl.model.DeviceStructure
import it.unibo.pulvreakt.dsl.model.ReconfigurationRules

class DeviceDeploymentSpecificationScope(private val deviceName: String, private val deviceStructure: DeviceStructure) {
    private val componentsStartupHosts = mutableListOf<Either<InvalidStartupHost, Pair<Component, Host>>>()
    private var deviceReconfigurationRules: Either<Nel<DeploymentConfigurationError>, ReconfigurationRules>? = null

    private fun Component.toName(): ComponentName = this::class.simpleName!!

    infix fun Component.startsOn(host: Host) {
        val componentName = this::class.simpleName!!
        val deviceCapabilities = deviceStructure.requiredCapabilities[componentName]!!
        val hostCapabilities = host.capabilities
        val result = either {
            ensure(containCapability(deviceCapabilities, hostCapabilities)) { InvalidStartupHost(componentName, host) }
            this@startsOn to host
        }
        componentsStartupHosts += result
    }

    fun reconfigurationRules(config: ReconfigurationRulesScope.() -> Unit) {
        val scope = ReconfigurationRulesScope().apply(config)
        deviceReconfigurationRules = scope.generate()
    }

    private fun containCapability(compCapability: Set<Capability>, hostCapability: Set<Capability>): Boolean =
        compCapability.intersect(hostCapability).isNotEmpty()

    private fun Raise<Nel<ComponentNotRegistered>>.ensureAllInstancesAreGiven(
        registeredComponents: List<Pair<Component, Host>>,
    ): List<Pair<Component, Host>> {
        val allComponents = deviceStructure.componentsGraph.keys + deviceStructure.componentsGraph.values.flatten()
        val nonConfiguredComponents = allComponents - registeredComponents.map { it.first.toName() }.toSet()
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
