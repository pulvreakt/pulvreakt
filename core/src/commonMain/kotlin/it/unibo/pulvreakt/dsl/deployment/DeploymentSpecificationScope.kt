package it.unibo.pulvreakt.dsl.deployment

import arrow.core.Either
import arrow.core.Nel
import arrow.core.nonEmptyListOf
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.toNonEmptySetOrNull
import it.unibo.pulvreakt.dsl.LogicDeviceType
import it.unibo.pulvreakt.dsl.errors.DeploymentConfigurationError
import it.unibo.pulvreakt.dsl.errors.EmptyDeploymentConfiguration
import it.unibo.pulvreakt.dsl.model.ConfiguredDevicesRuntimeConfiguration
import it.unibo.pulvreakt.dsl.model.DeviceRuntimeConfiguration

/**
 * Scope for the deployment DSL configuration.
 */
class DeploymentSpecificationScope {
    private val devicesConfiguration =
        mutableListOf<Either<Nel<DeploymentConfigurationError>, DeviceRuntimeConfiguration>>()

    /**
     * Configures a (logical) device with the given [logicDeviceType].
     * [config] is the configuration scope for configuring the device.
     */
    fun device(logicDeviceType: LogicDeviceType, config: DeviceDeploymentSpecificationScope.() -> Unit) {
        val configuration = either {
            val scope = DeviceDeploymentSpecificationScope(logicDeviceType.name).apply(config)
            scope.generate().bind()
        }
        devicesConfiguration += configuration
    }

    internal fun generate(): Either<Nel<DeploymentConfigurationError>, ConfiguredDevicesRuntimeConfiguration> = either {
        val devicesDeploymentConfig = either { devicesConfiguration.bindAll() }.bind()
        ensure(devicesDeploymentConfig.isNotEmpty()) { nonEmptyListOf(EmptyDeploymentConfiguration) }
        devicesDeploymentConfig.toNonEmptySetOrNull()!!
    }
}
