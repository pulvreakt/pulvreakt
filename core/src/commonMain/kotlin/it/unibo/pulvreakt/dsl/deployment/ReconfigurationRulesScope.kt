package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptySet
import arrow.core.raise.either
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError
import it.unibo.pulvreakt.dsl.model.ReconfigurationRules

/**
 * Scope for the reconfiguration rules given the [infrastructure].
 */
class ReconfigurationRulesScope(@Suppress("UNUSED_PARAMETER") private val infrastructure: NonEmptySet<Host>) {

    // private var onDeviceRules: Either<Nel<DeploymentConfigurationError>, OnDeviceRules>? = null

    /**
     * Specifies the reconfiguration rules associated to the device.
     */
    fun onDevice(@Suppress("UNUSED_PARAMETER") config: OnDeviceScope.() -> Unit) {
        TODO("The reconfiguration needs to be rethought")
//        val scope = OnDeviceScope(deviceStructure, infrastructure).apply(config)
//        onDeviceRules = scope.generate()
    }

    internal fun generate(): Either<Nel<DeploymentConfigurationError>, ReconfigurationRules> = either {
        TODO("The reconfiguration needs to be rethought")
//        ensureNotNull(onDeviceRules) { nonEmptyListOf(EmptyOnDeviceReconfiguration) }
//        val deviceRules = onDeviceRules!!.bind()
//        ReconfigurationRules(deviceRules)
    }
}
