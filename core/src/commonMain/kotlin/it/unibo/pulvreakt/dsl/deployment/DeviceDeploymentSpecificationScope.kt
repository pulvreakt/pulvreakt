package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.Nel
import arrow.core.raise.either
import it.unibo.pulvreakt.api.component.Component
import it.unibo.pulvreakt.api.infrastructure.Host
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError
import it.unibo.pulvreakt.dsl.model.DeviceRuntimeConfiguration
import it.unibo.pulvreakt.dsl.model.ReconfigurationRules

/**
 * Scope for the deployment configuration of a device.
 */
class DeviceDeploymentSpecificationScope<ID : Any>(private val deviceName: String) {
    private val componentsStartupHosts = mutableListOf<Pair<Component<ID>, Host>>()
    private var deviceReconfigurationRules: Either<Nel<DeploymentConfigurationError>, ReconfigurationRules>? = null

    /**
     * Configures a component of the device that starts on the given [host].
     */
    infix fun Component<ID>.startsOn(host: Host) {
        //        val componentType = getRef()
        //        val capabilityHostValidation = either {
        //            ensure(componentType in deviceStructure.requiredCapabilities.keys) {
        //                DeploymentConfigurationError.UnknownComponent(componentType)
        //            }
        //            val deviceCapabilities = deviceStructure.requiredCapabilities[componentType]!!
        //            val hostCapabilities = host.capabilities
        //            deviceCapabilities to hostCapabilities
        //        }
        //        val result = either {
        //            val (deviceCapabilities, hostCapabilities) = capabilityHostValidation.bind()
        //            ensure(containCapability(deviceCapabilities, hostCapabilities)) { InvalidStartupHost(componentType, host) }
        //            this@startsOn to host
        //        }
        componentsStartupHosts += this to host
    }

    /**
     * Configures the reconfiguration rules of the device.
     */
    fun reconfigurationRules(
        @Suppress("UNUSED_PARAMETER") config: ReconfigurationRulesScope<ID>.() -> Unit,
    ) {
        TODO("The reconfiguration needs to be rethought")
        //        val scope = ReconfigurationRulesScope(infrastructure).apply(config)
        //        deviceReconfigurationRules = scope.generate()
    }

    internal fun generate(): Either<Nel<DeploymentConfigurationError>, DeviceRuntimeConfiguration<ID>> =
        either {
            val reconfigurationRules = deviceReconfigurationRules?.bind()
            DeviceRuntimeConfiguration(deviceName, componentsStartupHosts.toMap(), reconfigurationRules)
        }
}
