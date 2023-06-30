package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptySet
import arrow.core.flatten
import arrow.core.mapOrAccumulate
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.zipOrAccumulate
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.core.reconfiguration.event.ReconfigurationEvent
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.InvalidReconfigurationComponent
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.InvalidReconfigurationHost
import it.unibo.pulvreakt.dsl.model.ComponentType
import it.unibo.pulvreakt.dsl.model.DeviceReconfigurationRule
import it.unibo.pulvreakt.dsl.model.DeviceStructure
import it.unibo.pulvreakt.dsl.model.NewConfiguration
import it.unibo.pulvreakt.dsl.model.OnDeviceRules

/**
 * Scope for the reconfiguration of a device.
 */
class OnDeviceScope(private val deviceStructure: DeviceStructure, private val infrastructure: NonEmptySet<Host>) {
    private val rules = mutableListOf<Either<Nel<DeploymentConfigurationError>, DeviceReconfigurationRule>>()

    /**
     * Specifies the [newConfiguration] associated to the [ReconfigurationEvent].
     */
    infix fun ReconfigurationEvent<*>.reconfigures(newConfiguration: NewConfiguration) {
        val (componentType, host) = newConfiguration
        val components = deviceStructure.componentsGraph.keys + deviceStructure.componentsGraph.values.flatten()

        val result = either<Nel<DeploymentConfigurationError>, DeviceReconfigurationRule> {
            zipOrAccumulate(
                { ensure(componentType in components) { InvalidReconfigurationComponent(componentType) } },
                { ensure(host in infrastructure) { InvalidReconfigurationHost(host) } },
                {
                    val componentCapabilities = deviceStructure.requiredCapabilities[componentType]!!
                    val hostCapability = host.capabilities
                    val isCompatible = componentCapabilities.intersect(hostCapability).isNotEmpty()
                    ensure(isCompatible) { DeploymentConfigurationError.InvalidReconfiguration(componentType, host) }
                },
            ) { _, _, _ -> DeviceReconfigurationRule(this@reconfigures, newConfiguration) }
        }
        rules += result
    }

    /**
     * Utility method for create a [NewConfiguration].
     */
    infix fun ComponentType.movesTo(host: Host): NewConfiguration = NewConfiguration(this, host)

    internal fun generate(): Either<Nel<DeploymentConfigurationError>, OnDeviceRules> = rules.mapOrAccumulate {
        when (it) {
            is Either.Right -> it.bind()
            is Either.Left -> raise(it.value)
        }
    }.mapLeft { it.flatten() }.map { it.toSet() }
}
