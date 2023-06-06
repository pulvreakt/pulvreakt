package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.nonEmptyListOf
import arrow.core.raise.either
import arrow.core.raise.ensure
import it.unibo.pulvreakt.core.component.ComponentType
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.core.reconfiguration.event.ReconfigurationEvent
import it.unibo.pulvreakt.dsl.deployment.model.DeviceReconfigurationRule
import it.unibo.pulvreakt.dsl.deployment.model.NewConfiguration
import it.unibo.pulvreakt.dsl.system.model.LogicalDeviceSpecification

/**
 * Scope for the reconfiguration of a device.
 */
class OnDeviceScope(private val logicalDeviceSpecification: LogicalDeviceSpecification) {
    private val reconfigurationRules = mutableListOf<Either<NonEmptyList<String>, DeviceReconfigurationRule>>()

    /**
     * Specifies the [newConfiguration] associated to the [ReconfigurationEvent].
     */
    infix fun ReconfigurationEvent<*>.reconfigures(newConfiguration: NewConfiguration) {
        val (componentType, host) = newConfiguration
        val requiredCapabilities = logicalDeviceSpecification.componentsRequiredCapabilities[componentType] ?: emptySet()
        val isValidHost = requiredCapabilities.intersect(host.capabilities).isNotEmpty()
        val newReconfiguration = either {
            ensure(isValidHost) {
                nonEmptyListOf(
                    """
                        $componentType cannot be moved to $host.
                         Because $componentType requires at least one of the following capabilities: $requiredCapabilities.
                    """.trimIndent(),
                )
            }
            DeviceReconfigurationRule(this@reconfigures, newConfiguration)
        }
        reconfigurationRules.add(newReconfiguration)
    }

    /**
     * Utility method for create a [NewConfiguration].
     */
    infix fun ComponentType<*>.movesTo(host: Host): NewConfiguration = this to host

    internal fun generate(): Either<NonEmptyList<String>, List<DeviceReconfigurationRule>> = either { reconfigurationRules.bindAll() }
}
