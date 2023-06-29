package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.Nel
import arrow.core.nonEmptyListOf
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.EmptyOnDeviceReconfiguration
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.InvalidReconfiguration
import it.unibo.pulvreakt.dsl.model.OnDeviceRules
import it.unibo.pulvreakt.dsl.model.ReconfigurationRules

/**
 * Scope for the reconfiguration rules.
 */
class ReconfigurationRulesScope {

    private val onDeviceRules: Either<Nel<InvalidReconfiguration>, OnDeviceRules>? = null

    /**
     * Specifies the reconfiguration rules associated to the device.
     */
    fun onDevice(config: OnDeviceScope.() -> Unit): Nothing = TODO()

    internal fun generate(): Either<Nel<DeploymentConfigurationError>, ReconfigurationRules> = either {
        ensureNotNull(onDeviceRules) { nonEmptyListOf(EmptyOnDeviceReconfiguration) }
        val deviceRules = onDeviceRules.bind()
        ReconfigurationRules(deviceRules)
    }
}
