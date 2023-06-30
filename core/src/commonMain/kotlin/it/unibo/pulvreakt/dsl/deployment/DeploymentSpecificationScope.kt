package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.Nel
import arrow.core.NonEmptySet
import arrow.core.nonEmptyListOf
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import arrow.core.toNonEmptySetOrNull
import it.unibo.pulvreakt.core.infrastructure.Host
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.MismatchWithSystemConfiguration
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError.NoDeviceFound
import it.unibo.pulvreakt.dsl.model.ConfiguredDeviceStructure
import it.unibo.pulvreakt.dsl.model.ConfiguredDevicesRuntimeConfiguration
import it.unibo.pulvreakt.dsl.model.DeviceRuntimeConfiguration

/**
 * Scope for the deployment DSL configuration.
 */
class DeploymentSpecificationScope(private val systemConfiguration: ConfiguredDeviceStructure, private val infrastructure: NonEmptySet<Host>) {
    private val devicesConfiguration =
        mutableListOf<Either<Nel<DeploymentConfigurationError>, DeviceRuntimeConfiguration>>()

    fun device(name: String, config: DeviceDeploymentSpecificationScope.() -> Unit) {
        val configuration = either {
            val deviceConfig = systemConfiguration.firstOrNull { it.deviceName == name }
            ensureNotNull(deviceConfig) { nonEmptyListOf(NoDeviceFound(name)) }
            val scope = DeviceDeploymentSpecificationScope(name, deviceConfig, infrastructure).apply(config)
            scope.generate().bind()
        }
        devicesConfiguration += configuration
    }

    internal fun generate(): Either<Nel<DeploymentConfigurationError>, ConfiguredDevicesRuntimeConfiguration> = either {
        ensure(systemConfiguration.size == devicesConfiguration.size) { nonEmptyListOf(MismatchWithSystemConfiguration) }
        devicesConfiguration.bindAll()
    }.map { it.toNonEmptySetOrNull()!! }
}
