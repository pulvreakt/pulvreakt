package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.raise.either
import it.unibo.pulvreakt.dsl.deployment.errors.DeploymentDslError
import it.unibo.pulvreakt.dsl.deployment.model.DeviceReconfigurationRule
import it.unibo.pulvreakt.dsl.deployment.model.ReconfigurationRules
import it.unibo.pulvreakt.dsl.system.model.LogicalDeviceSpecification

/**
 * Scope for the reconfiguration rules.
 */
class ReconfigurationRulesScope(private val logicalDeviceSpecification: LogicalDeviceSpecification) {
    private var deviceRules: Either<NonEmptyList<DeploymentDslError>, List<DeviceReconfigurationRule>>? = null

    /**
     * Specifies the reconfiguration rules associated to the device.
     */
    fun onDevice(config: OnDeviceScope.() -> Unit) {
        val onDeviceScope = OnDeviceScope(logicalDeviceSpecification).apply(config)
        deviceRules = onDeviceScope.generate()
    }

    internal fun generate(): Either<NonEmptyList<DeploymentDslError>, ReconfigurationRules> = either {
        val deviceRules = deviceRules?.map { it.toSet() }?.bind()
        ReconfigurationRules(deviceRules ?: emptySet())
    }
}
