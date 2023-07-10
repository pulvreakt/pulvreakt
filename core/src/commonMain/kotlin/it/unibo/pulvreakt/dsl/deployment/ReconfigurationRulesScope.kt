package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptySet
import arrow.core.nonEmptyListOf
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.EmptyOnDeviceReconfiguration
import it.unibo.pulvreakt.dsl.model.DeviceStructure
import it.unibo.pulvreakt.dsl.model.OnDeviceRules
import it.unibo.pulvreakt.dsl.model.ReconfigurationRules

/**
 * Scope for the reconfiguration rules.
 */
class ReconfigurationRulesScope(
    private val deviceStructure: DeviceStructure,
    private val infrastructure: NonEmptySet<Host>,
) {

    private var onDeviceRules: Either<Nel<DeploymentConfigurationError>, OnDeviceRules>? = null

    /**
     * Specifies the reconfiguration rules associated to the device.
     */
    fun onDevice(config: OnDeviceScope.() -> Unit) {
        val scope = OnDeviceScope(deviceStructure, infrastructure).apply(config)
        onDeviceRules = scope.generate()
    }

    internal fun generate(): Either<Nel<DeploymentConfigurationError>, ReconfigurationRules> = either {
        ensureNotNull(onDeviceRules) { nonEmptyListOf(EmptyOnDeviceReconfiguration) }
        val deviceRules = onDeviceRules!!.bind()
        ReconfigurationRules(deviceRules)
    }
}
