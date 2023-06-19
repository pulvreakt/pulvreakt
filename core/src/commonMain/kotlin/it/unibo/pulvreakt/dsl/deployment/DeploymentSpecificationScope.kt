package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.Nel
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError
import it.unibo.pulvreakt.dsl.model.ConfiguredDevicesRuntimeConfiguration

/**
 * Scope for the deployment DSL configuration.
 */
class DeploymentSpecificationScope {

    fun device(name: String, config: DeviceDeploymentSpecificationScope.() -> Unit): Nothing = TODO()

    internal fun generate(): Either<Nel<DeploymentConfigurationError>, ConfiguredDevicesRuntimeConfiguration> = TODO()
}
