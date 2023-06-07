package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.mapOrAccumulate
import arrow.core.raise.Raise
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import it.unibo.pulvreakt.core.component.ComponentType
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.core.reconfiguration.event.ReconfigurationEvent
import it.unibo.pulvreakt.dsl.deployment.errors.DeploymentDslError
import it.unibo.pulvreakt.dsl.deployment.errors.DeploymentDslError.InvalidReconfiguration
import it.unibo.pulvreakt.dsl.deployment.model.DeviceReconfigurationRule
import it.unibo.pulvreakt.dsl.deployment.model.NewConfiguration
import it.unibo.pulvreakt.dsl.system.model.LogicalDeviceSpecification

/**
 * Scope for the reconfiguration of a device.
 */
class OnDeviceScope(private val logicalDeviceSpecification: LogicalDeviceSpecification) {
    private val reconfigurationRules = mutableListOf<DeviceReconfigurationRule>()

    /**
     * Specifies the [newConfiguration] associated to the [ReconfigurationEvent].
     */
    infix fun ReconfigurationEvent<*>.reconfigures(newConfiguration: NewConfiguration) {
        reconfigurationRules.add(DeviceReconfigurationRule(this, newConfiguration))
    }

    /**
     * Utility method for create a [NewConfiguration].
     */
    infix fun ComponentType<*>.movesTo(host: Host): NewConfiguration = this to host

    private fun Raise<DeploymentDslError>.verify(rule: DeviceReconfigurationRule): DeviceReconfigurationRule =
        rule.also {
            val (component, host) = rule.newConfgiuration
            val requiredCapabilities = logicalDeviceSpecification.componentsRequiredCapabilities[component]
            ensureNotNull(requiredCapabilities) { DeploymentDslError.UnknownComponent(component) }
            val hostCapabilities = host.capabilities
            val commonCapabilities = requiredCapabilities.intersect(hostCapabilities)
            ensure(commonCapabilities.isNotEmpty()) { InvalidReconfiguration(component, host) }
        }

    internal fun generate(): Either<NonEmptyList<DeploymentDslError>, List<DeviceReconfigurationRule>> =
        reconfigurationRules.mapOrAccumulate { verify(it) }
}
